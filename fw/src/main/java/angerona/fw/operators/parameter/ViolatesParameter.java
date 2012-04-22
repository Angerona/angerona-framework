package angerona.fw.operators.parameter;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;
import angerona.fw.logic.base.Beliefs;

public class ViolatesParameter extends GenericOperatorParameter {
	/** the beliefs of the agent */
	private Beliefs  beliefs;
	
	/** the action applied before proofing for violation */
	private AngeronaAtom action;
	
	/**
	 * @param beliefs 	The belief base.
	 * @param na		The NegotiationAct which will be apply on the beliefbase
	 */
	public ViolatesParameter(Agent agent, Action na) {
		super(agent.getEnvironment());
		this.beliefs = agent.getBeliefs();
		this.action = na;
	}
	
	/** @return the beliefs of the agent */
	Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return the action applied before proofing for violation */
	AngeronaAtom getAction() {
		return action;
	}
}
