package angerona.fw;

import java.util.List;

import angerona.fw.logic.SecrecyStrengthPair;

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
	
	protected boolean violates = false;
	
	/* These changes need to be isolate or otherwise changed -- Daniel */
	protected List<SecrecyStrengthPair> weakenings = null;
	
	public Skill(Agent agent, String name) {
		super(agent);
		this.name = name;
	}
	
	public Skill(Skill s)
	{
		super(s.agent);
		this.parent = s.parent;
		this.realRun = s.realRun;
		this.objectContainingContext = s.objectContainingContext;
		this.cost = s.cost;
		this.honesty = s.honesty;
		this.name = s.name;
		this.violates = s.violates;
		this.weakenings = s.weakenings;
	}
	

	public List<SecrecyStrengthPair> getWeakenings()
	{
		return this.weakenings;
	}
	public void setWeakenings(List<SecrecyStrengthPair> weaks)
	{
		this.weakenings = weaks;
	}
	
	
	/* End of Daniel's changes */
	public boolean violates() {
		return violates;
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

	@Override
	public Object clone() {
		return this;
	}
	
	/**
	 * In the future, Skill probably shouldn't have a deepCopy, as it shouldn't need it. 
	 * Thus Skill doesn't need to implement cloneable
	 */
	public abstract Skill deepCopy();
}
