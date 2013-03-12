package angerona.fw.operators;

import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.ConversionException;
import angerona.fw.operators.parameter.OperatorParameter;
import angerona.fw.report.ReportPoster;
import angerona.fw.util.Pair;

/**
 * An abstract generic base class for operators implementing an operation type.
 * Direct sub classes of this class are typically abstract classes to define
 * the generic type parameters and implement the getEmptyParameter() and 
 * defaultReturnValue() methods. Thus classes define the operation type but give no
 * implementation for the operation type yet.
 *
 * TODO: Move the <String, String> parameters to another place, they must be saved
 * on caller side.
 *
 * @param <TCaller>	Type of the owner of the operator 
 * @param <IN>		Type of the input parameter of the operator.
 * @param <OUT>		Type of the output (return value) of the operator.
 * 
 * @author Tim Janus
 */
public abstract class Operator<TCaller extends OperatorStack, IN extends OperatorParameter, OUT extends Object> 
	implements 
	BaseOperator,
	ReportPoster {
	
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(Operator.class);
	
	/** a map containing parameters for the operator in a generic representation */
	protected Map<String, String> parameters = new HashMap<String, String>();
	
	/**
	 * Define as abstract method to fix errors in different jars which does not realize
	 * that this method is defined in the BaseOperator interface.
	 */
	@Override
	public abstract Pair<String, Class<?>> getOperationType();
	
	/** 
	 * sub classes have to implement this method to provide a default input parameter.
	 * @return	A default instance of type IN representing a input parameter structure 
	 * 			for the operation type.
	 */
	protected abstract IN getEmptyParameter();
	
	/**
	 * sub classes have to implement this method to provide the operation 
	 * encapsulate by the operator.
	 * @param preprocessedParameters	A data structure containing the input
	 * 									parameters of type IN
	 * @return	An instance of the output object of type OUT.
	 */
	protected abstract OUT processInternal(IN preprocessedParameters);
	
	/**
	 * sub classes have to implement this method to provide a default return
	 * value of the operator which is given if any error occur during the
	 * operation
	 * @return	A default instance of the output object of type OUT.
	 */
	protected abstract OUT defaultReturnValue();
	
	/**
	 * Starts the processing of the operator with the given generic operator parameters.
	 * 
	 * @param genericParams
	 * @return
	 */
	@Override
	public OUT process(GenericOperatorParameter genericParams) {
		genericParams.getCaller().pushOperator(this);
		IN preparedParams = getEmptyParameter();
		OUT reval = null;
		try {
			preparedParams.fromGenericParameter(genericParams);
			preparedParams.visit(this);
			prepare(preparedParams);
			reval = processInternal(preparedParams);
		} catch(AttributeNotFoundException | ConversionException ex) {
			reval = defaultReturnValue();
			LOG.error("Operator '{}' is not able to fetch the parameters: '{}'",
					this.getClass().getName(), ex.getMessage());
			throw new RuntimeException();
		} finally { 
			genericParams.getCaller().popOperator();
		}
		return reval;
	}
	
	/**
	 * process version which used the specialized input parameters as argument.
	 * This allows easier invocation from the java side. The other method defined
	 * in the interface is useful for ASML invocation.
	 * @param params	The parameters for the operator invocation in specialized version.
	 * @return	
	 */
	public OUT process(IN params) {
		params.getCaller().pushOperator(this);
		params.visit(this);
		prepare(params);
		OUT reval = processInternal(params);
		params.getCaller().popOperator();
		return reval;
	}
	
	@Override
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	@Override
	public Map<String, String> getParameters() {
		return this.parameters;
	}
	
	@Override
	public String getParameter(String name, String def) {
		if(!this.parameters.containsKey(name)) {
			this.parameters.put(name, def);
			return def;
		} else {
			return this.parameters.get(name);
		}
	}
	
	@Override
	public String getPosterName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	public String getNameAndParameters() {
		return this.getClass().getSimpleName() + ":"
				+ this.parameters.toString();
	}
	
	/**
	 * This method gives sub classes which are no leafes the ability to do further
	 * preparation before invoking processInternal.
	 * @param params	The input parameters
	 */
	protected void prepare(IN params) {
		
	}
}
