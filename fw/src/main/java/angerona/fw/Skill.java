package angerona.fw;

import angerona.fw.logic.ViolatesResult;

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
	
	protected ViolatesResult violates = new ViolatesResult();
	
	public ViolatesResult violates() {
		return violates;
	}
	
	public Skill(Agent agent, String name) {
		super(agent);
		this.name = name;
	}
	
	/** @return the unique name of the Skill */
	public String getName() {
		return name;
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
}
