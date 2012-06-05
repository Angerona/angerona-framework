package angerona.fw.operators.parameter;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;
import angerona.fw.logic.Beliefs;

public class ViolatesParameter extends GenericOperatorParameter {
	private Agent agent;
	
	/** the action applied before proofing for violation */
	private AngeronaAtom action;
	
	/**
	 * @param beliefs 	The belief base.
	 * @param na		The NegotiationAct which will be apply on the beliefbase
	 */
	public ViolatesParameter(Agent agent, Action na) {
		super(agent);
		this.agent = agent;
		this.action = na;
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	/** @return the beliefs of the agent */
	public Beliefs getBeliefs() {
		return agent.getBeliefs();
	}
	
	/** @return the action applied before proofing for violation */
	public AngeronaAtom getAction() {
		return action;
	}
}
