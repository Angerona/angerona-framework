package angerona.fw;

import angerona.fw.listener.ActionProcessor;
import angerona.fw.logic.Beliefs;


/** 
 * 	A skill represents an atomic intention an agent can perform.
 * 	This is the abstract base class for a Skill. Subclasses can implement
 *  the run method to give the skill its own behavior.
 *  
 * 	@see angerona.fw.internal.XMLSkill
 * 	@author Tim Janus
 */
public abstract class Skill extends Intention implements Runnable {

	/** the unique name of the Skill */
	private String name;
	
	/**
	 * flag indicating if the actions in this Intention should be send to the
	 * environment
	 */
	protected ActionProcessor actionProcessor;

	/**
	 * the context used for dynamic code evaluation, allows the access on data
	 * saved to for evaluating the intention
	 */
	protected Object dataObject;

	protected Beliefs beliefs;

	
	public Skill(Agent agent, String name) {
		super(agent);
		this.name = name;
	}
	
	/** @return the unique name of the Skill */
	public String getName() {
		return name;
	}
	
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	public void prepare(ActionProcessor actionProcessor, Beliefs beliefs, Object data) {
		this.actionProcessor = actionProcessor;
		this.beliefs = beliefs;
		this.dataObject = data;
	}

	/**
	 * Gets the flag which indicates if the Intention run should communicate
	 * with the environment (true behavior) or if the changes should only occurs
	 * locally (false behavior). The false behavior is used for Violates reasons
	 * and so so on.
	 * 
	 * @return boolean flag.
	 */
	public boolean isRealRun() {
		return actionProcessor instanceof Agent;
	}
	
	@Override
	public boolean isAtomic() {
		return true;
	}
	
	@Override
	public boolean isPlan() {
		return false;
	}

	@Override
	public boolean isSubPlan() {
		return false;
	}

	@Override
	public void onSubgoalFinished(Intention subgoal) {
		// does nothing here has no subgoals.
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override 
	public boolean equals(Object other) {
		if(! (other instanceof Skill)) return false;
		Skill s = (Skill)other;
		return name.equals(s.name);
	}
}
