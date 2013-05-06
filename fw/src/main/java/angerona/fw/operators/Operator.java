package angerona.fw.operators;

import javax.management.AttributeNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.OperatorCaller;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.parameter.OperatorParameter;
import angerona.fw.util.Pair;

/**
 * An abstract generic base class for operators implementing an operation type.
 * Direct sub classes of this class are typically abstract classes to define
 * the generic type parameters and implement the getEmptyParameter() and 
 * defaultReturnValue() methods. Thus classes define the operation type but give no
 * implementation for the operation type yet.
 * 
 * @remark 	Every instance of an operator is stateless this means the developer subclassing
 * 			the Operator is not allowed to use member variables. To save data between the
 * 			prepare(), proccessInternal() and finish() method one can use the input parameter data
 * 			structure of type IN.
 *
 * @tparam <TCaller>	Type of the owner of the operator 
 * @tparam <IN>			Type of the input parameter of the operator.
 * @tparam <OUT>		Type of the output (return value) of the operator.
 * 
 * @author Tim Janus
 */
public abstract class Operator<TCaller extends OperatorCaller, IN extends OperatorParameter, OUT extends Object> 
	implements 
	BaseOperator {
	
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(Operator.class);

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
	 * This method is used by ASML.
	 * @param genericParams	A generic version of the data structure representing the input parameters.
	 * @return	The result of the operation as type OUT.
	 */
	@Override
	public OUT process(GenericOperatorParameter genericParams) {
		OperatorStack stack = genericParams.getCaller().getStack();
		stack.pushOperator(this);
		
		IN preparedParams = getEmptyParameter();
		OUT reval = null;
		try {
			preparedParams.fromGenericParameter(genericParams);
			prepare(preparedParams);
			reval = processInternal(preparedParams);
		} catch(AttributeNotFoundException | ConversionException ex) {
			reval = defaultReturnValue();
			LOG.error("Operator '{}' is not able to fetch the parameters: '{}'",
					this.getClass().getName(), ex.getMessage());
			throw new RuntimeException();
		} finally { 
			stack.popOperator();
		}
		return reval;
	}
	
	/**
	 * process version which used the specialized input parameters as argument.
	 * This allows easier invocation from the java code. 
	 * @param params	The parameters for the operator invocation in specialized version.
	 * @return	
	 */
	public OUT process(OperatorParameter params) {
		params.getStack().pushOperator(this);
		@SuppressWarnings("unchecked")
		IN params2 = (IN)params;
		prepare(params2);
		OUT reval = processInternal((IN)params2);
		params.getStack().popOperator();
		return reval;
	}
	
	@Override
	public String getPosterName() {
		return this.getClass().getSimpleName();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * This method can be overridden by sub classes to do further
	 * preparation before invoking processInternal.
	 * @remark 	The operator instances are stateless this means if this
	 * 			method shall save something it has to use the data 
	 * 			structure given as parameter.
	 * @param params	The input parameter data structure.
	 */
	protected void prepare(IN params) {
		
	}
	
	/**
	 * This method can be overridden by sub classes to do further
	 * processing after the processInternal method is finished.
	 * @remark 	The operator instances are stateless this means if this
	 * 			method shall use something processed earlier in prepare() or
	 * 			processInternal it has to use the params parameter.
	 * @param params	The input parameter data structure.
	 */
	protected void finish(IN params) {
		
	}
}
