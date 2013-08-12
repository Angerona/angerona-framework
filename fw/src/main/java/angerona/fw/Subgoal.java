package angerona.fw;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * A subgoal is a complex intention. It can realize sub plans by using stacks. 
 * It gives the user the possibility of using more than one sub-plan, this allows the implementation of
 * Hierarchical task network planners and similar stuff.
 * @author Tim Janus
 */
public class Subgoal extends Intention implements Cloneable {

	private String name = "SG";
	
	/** a collection of desires which will be fulfilled if this Intention was processed */
	private Desire	fulfillsDesire;
	
	/** a collection of stacks with sub-intentions defining the subgoals of this intention */
	protected List<Stack<PlanElement>> stacks = new LinkedList<>();
	
	public Subgoal(Agent agent) {
		this(agent, null);
	}
	
	public Subgoal(Agent agent, Desire desire) {
		super(agent);
		this.fulfillsDesire = desire;
	}
	
	protected Subgoal(Subgoal other) {
		super(other);
		this.fulfillsDesire = new Desire(other.fulfillsDesire);
		for(Stack<PlanElement> stack : other.stacks) {
			Stack<PlanElement> newOne = new Stack<PlanElement>();
			for(int i=0; i<stack.size(); ++i) {
				newOne.add(new PlanElement(stack.get(i)));
			}
			this.stacks.add(newOne);
		}
	}
	
	/** @return the number of used stacks */
	public int getNumberOfStacks() {
		return stacks.size();
	}
	
	/**
	 * create a new stack for the given complex intention. Is used for creating a stack with a complex intention.
	 * @param intention	will be put ontop of the newly create stack.
	 * @return true
	 */
	public boolean newStack(Intention intention) {
		return newStack(intention, null);
	}
	
	/**
	 * creates a new stack for the given intention. If the intention is an action the context for performing the action
	 * must be given as second parameter. If the intention is complex (a plan) the second parameter must be null.
	 * @param intention	reference to the intention which will be on top of the newly create stack
	 * @param context	the context used when running the given intention (for performing the action), this might
	 * 					be a class which is not of type context. The object has to give the intention a context, 
	 * 					like the query which the intention wants to answer and so on.
	 * @return			true
	 */
	public boolean newStack(Intention intention, Object context) {
		return newStack(new PlanElement(intention, context));
	}
	
	public boolean newStack(PlanElement pe) {
		if(pe == null)
			throw new IllegalArgumentException("PlanElement parameter must not be null.");
		
		Stack<PlanElement> newStack = new Stack<PlanElement>();
		newStack.add(pe);
		return stacks.add(newStack);
	}
	
	public boolean addToStack(Intention intention, int index) {
		return addToStack(intention, null, index);
	}
	
	public boolean addToStack(Intention intention, Object context, int index) {
		return addToStack(new PlanElement(intention, context), index);
	}
	
	public boolean addToStack(PlanElement pe, int index) {
		if(pe == null)
			throw new IllegalArgumentException("PlanElement parameter must not be null.");
		return stacks.get(index).add(pe);
	}
	
	
	/**
	 * Looks at the top-stack element of the stack with the given index.
	 * @param index	index of the used stack
	 * @return intention on-top of the stack with the given index.
	 */
	public PlanElement peekStack(int index) {
		PlanElement pe = stacks.get(index).peek();
		pe.getIntention().setParent(this);
		return pe;
	}

	@Override
	public boolean isAtomic() {
		return false;
	}

	@Override
	public boolean isSubPlan() {
		return !isPlan();
	}

	@Override
	public void onSubgoalFinished(Intention subgoal) {
		Stack<PlanElement> toDel = null;
		for(Stack<PlanElement> s : stacks) {
			if(s.peek().getIntention().equals( subgoal )) {
				s.pop();
				if(s.isEmpty()) {
					toDel = s;
				}
				break;
			}
		}
		
		if(toDel != null) {
			stacks.remove(toDel);
		}
		
		if(parent != null && stacks.isEmpty())
			parent.onSubgoalFinished(this);
		else
			getAgent().onSubgoalFinished(this);
	}
	
	/** @return collection of desires which will be fullfiled after the Intention was followed */
	public Desire getFulfillsDesire() {
		return fulfillsDesire;
	}
	
	@Override
	public Object clone() {
		return new Subgoal(this);
	}
	
	@Override
	public String toString() {
		String reval = this.fulfillsDesire + " <-- " + name + "(";
		
		for(Stack<PlanElement> stack : stacks) {
			reval += stack.toString();
			reval += ",";
		}
		if(!stacks.isEmpty())
			reval = reval.substring(0, reval.length()-1);
		reval +=  ")";
		
		return reval;
	}
	
	@Override
	public boolean equals(Object other) {
		if(! (other instanceof Subgoal)) {
			return false;
		}
		
		Subgoal sg = (Subgoal)other;
		return 	name.equals(sg.name) && 
				fulfillsDesire.equals(sg.fulfillsDesire) &&
				stacks.equals(sg.stacks);
	}
	
	@Override
	public int hashCode() {
		return (name.hashCode() + fulfillsDesire.hashCode() + stacks.hashCode()) * 3;
	}
}
