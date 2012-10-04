package angerona.fw;

import angerona.fw.listener.SubgoalListener;

/**
 * An intention is either atomic, then it is called Skill or it is complex then
 * it is called Plan.
 * 
 * To support the Angerona violates structure an intention can save if it is a
 * real-run or a mental-run of the intention (the plan).
 * 
 * @see Skill
 * @see Subgoal
 * @author Tim Janus
 */
public abstract class Intention implements SubgoalListener {

	/** reference to the agent who is owner of this Intention */
	protected Agent agent;

	/** the parent intention of this instance */
	protected Intention parent;

	/**
	 * Ctor: Creates a new instance of an intention for the given agent.
	 * @param agent	reference to the agent owning the intention
	 */
	public Intention(Agent agent) {
		this.agent = agent;
	}

	protected Intention(Intention other) {
		this.parent = other.parent;
		this.agent = other.agent;
	}

	/** @return reference to the agent owning this Intention */
	public Agent getAgent() {
		return agent;
	}

	protected void setAgent(Agent agent) {
		this.agent = agent;
	}
	
	public void setParent(Intention parent) {
		this.parent = parent;
	}

	public Intention getParent() {
		return parent;
	}

	/**
	 * Is called when an sub goal is finished by an agent. For example a Skill
	 * was performed or a complex plan was finished. The given subgoal must be
	 * removed from the subgoals by the implementation and is a child of the
	 * called object.
	 * 
	 * @param subgoal
	 *            reference to the finished subgoal
	 */
	public abstract void onSubgoalFinished(Intention subgoal);

	/**
	 * returns true if this is an atomic intention, an intention which can pe
	 * performed in one step
	 */
	public abstract boolean isAtomic();

	/**
	 * @return true if this instance is a sub-intention but no atomic intention
	 *         in a plan of the agent
	 */
	public abstract boolean isSubPlan();
	
	/** @return true if this is the high level plan of an agent. */
	public boolean isPlan() {
		return parent == null;
	}
}
