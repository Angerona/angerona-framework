package angerona.fw.operators;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseOperator;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.parameter.OperatorParameter;
import angerona.fw.report.ReportPoster;

/**
 * An abstract generic base class for operators implementing an operation type.
 * Direct sub classes of this class are typically abstract classes to but define
 * the generic type parameters and implement the getEmptyParameter() and 
 * defaultReturnValue() methods. Thus classes define the operation type but give no
 * implementation for the operation type yet.
 *
 * @param <TCaller>	Type of the owner of the operator 
 * @param <IN>		Type of the input parameter for the real process internal method
 * @param <OUT>		Type of the output (return value) of the operator.
 * 
 * @author Tim Janus
 */
public abstract class Operator<TCaller extends OperatorVisitor, IN extends OperatorParameter, OUT extends Object> 
	implements 
	BaseOperator,
	ReportPoster {
	
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(Operator.class);
	
	/** a map containing parameters for the operator in a generic representation */
	protected Map<String, String> parameters = new HashMap<String, String>();
	
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
			reval = processInternal(preparedParams);
		} catch(ConversionException ex) {
			reval = defaultReturnValue();
			LOG.error("Operator '{}' is not able to fetch the parameters: '{}'",
					this.getClass().getName(), ex.getMessage());
			throw new RuntimeException();
		} finally { 
			genericParams.getCaller().popOperator();
		}
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
}
