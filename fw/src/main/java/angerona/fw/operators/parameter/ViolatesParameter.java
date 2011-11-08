package angerona.fw.operators.parameter;

import angerona.fw.Action;
import angerona.fw.logic.base.Beliefs;

public class ViolatesParameter {
	/** the beliefs of the agent */
	private Beliefs  beliefs;
	
	/** the action applied before proofing for violation */
	private Action action;
	
	/**
	 * @param beliefs 	The belief base.
	 * @param na		The NegotiationAct which will be apply on the beliefbase
	 */
	public ViolatesParameter(Beliefs beliefs, Action na) {
		this.beliefs = beliefs;
		this.action = na;
	}
	
	/** @return the beliefs of the agent */
	Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return the action applied before proofing for violation */
	Action getAction() {
		return action;
	}
}
