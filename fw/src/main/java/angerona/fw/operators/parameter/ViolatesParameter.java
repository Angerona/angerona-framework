package angerona.fw.operators.parameter;

import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;
import angerona.fw.logic.Beliefs;

public class ViolatesParameter extends GenericOperatorParameter {
	/** reference to the agent which will apply the action */
	private Agent agent;
	
	/** the action applied before proofing for violation */
	private AngeronaAtom action;
	
	/** the beliefs which are used as basic */
	private Beliefs beliefs;
	
	/**
	 * Simple Ctor: Uses a copy of the actual beliefs of the agent.
	 * For planning in the future use the other ctor.
	 * @param agent		Reference to the agent who wants to check for violation.
	 * @param intent	The intention of the agent which needs a check. This might be
	 * 					a skill, an action or a complete plan.
	 */
	public ViolatesParameter(Agent agent, AngeronaAtom intent) {
		this(agent, agent.getBeliefs(), intent);
	}
	
	/**
	 * Ctor: Uses a copy of the given beliefs.
	 * @param agent
	 * @param beliefs
	 * @param intent
	 */
	public ViolatesParameter(Agent agent, Beliefs beliefs, AngeronaAtom intent) {
		super(agent);
		this.agent = agent;
		this.action = intent;
		this.beliefs = (Beliefs)beliefs.clone();
	}
	
	public Agent getAgent() {
		return agent;
	}
	
	/** @return the beliefs of the agent */
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return the action applied before proofing for violation */
	public AngeronaAtom getAtom() {
		return action;
	}
}
