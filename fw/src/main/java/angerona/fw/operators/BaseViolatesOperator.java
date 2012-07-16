package angerona.fw.operators;

import java.util.List;

import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.operators.parameter.ViolatesParameter;

/**
 * Base class for violates tests. 
 * @author Tim Janus
 */
public abstract class BaseViolatesOperator extends 
	Operator<ViolatesParameter, Boolean> {
	protected List<SecrecyStrengthPair> weakenings = null;
	public List<SecrecyStrengthPair> weakenings() {
		// TODO Auto-generated method stub
		return this.weakenings;
	}

}
