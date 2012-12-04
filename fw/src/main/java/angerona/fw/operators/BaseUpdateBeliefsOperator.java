package angerona.fw.operators;

import angerona.fw.logic.Beliefs;
import angerona.fw.operators.parameter.EvaluateParameter;

/**
 * Base class for different change operators
 * @author Tim Janus
 */
public abstract class BaseUpdateBeliefsOperator extends 
	Operator<EvaluateParameter, Beliefs> {

	@Override
	protected EvaluateParameter getEmptyParameter() {
		return new EvaluateParameter();
	}

	@Override
	protected Beliefs defaultReturnValue() {
		return null;
	}

}
