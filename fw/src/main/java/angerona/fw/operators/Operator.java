package angerona.fw.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseOperator;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.parameter.OperatorParameter;



/**
 * This is the base class for every user defined operator.
 * @author Tim Janus
 *
 * @param <IN>		Type of the input parameter for the real process internal method
 * @param <OUT>		Type of the output (return value) of the operator.
 */
public abstract class Operator<IN extends OperatorParameter, OUT> extends BaseOperator {
	
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(Operator.class);
	
	public OUT process(GenericOperatorParameter genericParams) {
		getOwner().pushOperator(this);
		IN preparedParams = getEmptyParameter();
		OUT reval = null;
		try {
			preparedParams.fromGenericParameter(genericParams);
			reval = processInternal(preparedParams);
		} catch(ConversionException ex) {
			reval = defaultReturnValue();
			LOG.error("Operator '{}' is not able to fetch the parameters: '{}'",
					this.getClass().getName(), ex.getMessage());
		} finally { 
			getOwner().popOperator();
		}
		return reval;
	}
	
	protected abstract IN getEmptyParameter();
		
	protected abstract OUT processInternal(IN preprocessedParameters);
	
	protected abstract OUT defaultReturnValue();
}
