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

	private String name = "NO-NAME";
	
	/** a collection of desires which will be fulfilled if this Intention was processed */
	private Desire	fulfillsDesire;
	
	/** a collection of stacks with sub-intentions defining the subgoals of this intention */
	private List<Stack<PlanElement>> stacks = new LinkedList<Stack<PlanElement>>();
	
	public Subgoal(Agent agent) {
		this(agent, null);
	}
	
	public Subgoal(Agent agent, Desire desire) {
		super(agent);
		this.fulfillsDesire = desire;
	}
	
	protected Subgoal(Subgoal other) {
		super(other);
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
	 * @return
	 */
	public boolean newStack(Intention intention) {
		return newStack(intention, null);
	}
	
	/**
	 * creates a new stack for the given intention. If the intention is a Skill the context of running the skill
	 * must be given as second parameter. If the intention is complex (a plan) the second parameter must be null.
	 * @param intention	reference to the intention which will be on top of the newly create stack
	 * @param context	the context used when running the given intention (for running the skill), this might
	 * 					be a class which is not of type context. The object has to give the intention a context, 
	 * 					like the query which the intention wants to answer and so on.
	 * @return			true
	 */
	public boolean newStack(Intention intention, Object context) {
		Stack<PlanElement> newStack = new Stack<PlanElement>();
		newStack.add(new PlanElement(intention, context));
		return stacks.add(newStack);
	}
	
	public boolean addToStack(Intention intention, int index) {
		return addToStack(intention, null, index);
	}
	
	public boolean addToStack(Intention intention, Object context, int index) {
		return stacks.get(index).add(new PlanElement(intention, context));
	}
	
	/**
	 * Looks at the top-stack element of the stack with the given index.
	 * @param index	index of the used stack
	 * @return intention on-top of the stack with the given index.
	 */
	public PlanElement peekStack(int index) {
		PlanElement pe = stacks.get(index).peek();
		pe.getIntention().setParent(this);
		pe.prepare();
		return pe;
	}

	@Override
	public void run() {
		// TODO
	}

	@Override
	public boolean isAtomic() {
		return false;
	}

	@Override
	public boolean isPlan() {
		return parent == null;
	}

	@Override
	public boolean isSubPlan() {
		return parent != null;
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
		return name;
	}
}
