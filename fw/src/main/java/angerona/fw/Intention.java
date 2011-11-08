package angerona.fw;

import java.util.Collection;
import java.util.LinkedList;

import net.sf.tweety.Formula;

/**
 * An intention is either atomic, then it is called Skill or it is complex
 * then it is called Plan. 
 * @see Skill
 * @see Plan
 * @author Tim Janus
 */
public abstract class Intention implements Runnable {
	
	/** the name for the top-level plan of an agent. */
	public static final String ID_AGENT_PLAN = "_AGENT_PLAN_";
	
	/** reference to the agent who is owner of this Intention */
	private Agent agent;
	
	/** a collection of desires which will be fulfilled if this Intention was processed */
	private Collection<Formula>	fulfillsDesires = new LinkedList<Formula>();
	
	/** the parent intention of this instance */
	protected Intention parent;
	
	/** flag indicating if the actions in this Intention should be send to the environment */
	protected boolean realRun = false;
	
	/** the context used for dynamic code evaluation */
	protected Object objectContainingContext;
		
	/**
	 * Ctor: Creates a new instance of an intention for the given agent.
	 * @param agent	reference to the agent owning the intention
	 */
	public Intention(Agent agent) {
		this.agent = agent;
	}
	
	/**
	 * Ctor: Creates a child intention for the given parent intention of the agent
	 * @param parent
	 */
	public Intention(Intention parent) {
		this(parent.getAgent());
		this.parent = parent;
	}
	
	/** @return reference to the agent owning this Intention */
	public Agent getAgent() {
		return agent;
	}
	
	/** @return collection of desires which will be fullfiled after the Intention was followed */
	public Collection<Formula> getFulfillsDesires() {
		return fulfillsDesires;
	}
	
	/**
	 * Sets the flag which indicates if the Intention run should communicate with the environment (true behavior) or
	 * if the changes should only occur locally (false behavior).
	 * The false behavior is used for Violates reasons and so so on.
	 * @param realRun	boolean flag.
	 */
	public void setRealRun(boolean realRun) {
		this.realRun = realRun;
	}
	
	/**
	 * Gets the flag which indicates if the Intention run should communicate with the environment (true behavior) or
	 * if the changes should only occurs locally (false behavior).
	 * The false behavior is used for Violates reasons and so so on.
	 * @return boolean flag.
	 */
	public boolean isRealRun() {
		return realRun;
	}
	
	/** setting the object which contains the input context for this intention */
	public void setObjectContainingContext(Object obj) {
		this.objectContainingContext = obj;
	}
	
	public void setParent(Intention parent) {
		this.parent = parent;
	}
	
	/**
	 * Is called when an sub goal is finished by an agent. For example a Skill was performed or 
	 * a complex plan was finished. The given subgoal must be removed from the subgoals by the
	 * implementation
	 * @param subgoal reference to the finished subgoal
	 */
	public abstract void onSubgoalFinished(Intention subgoal);
	
	/** returns true if this is an atomic intention, an intention which can pe performed in one step */
	public abstract boolean isAtomic();
	
	/** returns true if this is the high level plan of an agent. */
	public abstract boolean isPlan();
	
	/** @return	true if this instance is a sub-intention but no atomic intention in a plan of the agent */
	public abstract boolean isSubPlan();	
}
