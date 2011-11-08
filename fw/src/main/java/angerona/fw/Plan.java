package angerona.fw;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * A plan is a complex intention. It realized sub plans by using stacks. 
 * It gives the user the possibility of using more than one sub-plan, this allows the implementation of
 * Hierarchical task network planners and similar stuff.
 * @author Tim Janus
 */
public class Plan extends Intention {

	/**
	 * Represents a stack element. Skills need to save their context object to perform
	 * the correct actions
	 * @author Tim Janus
	 */
	private class StackElement {
		private Intention intention;
		
		private Object context;
		
		public StackElement(Intention intention, Object context) {
			this.intention = intention;
			this.context = context;
		}
	}
	
	/** a collection of stacks with sub-intentions defining the subgoals of this intention */
	private List<Stack<StackElement>> stacks = new LinkedList<Stack<StackElement>>();
	
	public Plan(Agent agent) {
		super(agent);
	}

	public Plan(Plan parent) {
		super(parent);
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
	 * @param context	the context used when running the given intention (for running the skill)
	 * @return
	 */
	public boolean newStack(Intention intention, Object context) {
		Stack<StackElement> newStack = new Stack<Plan.StackElement>();
		newStack.add(new StackElement(intention, context));
		return stacks.add(newStack);
	}
	
	public boolean addToStack(Intention intention, int index) {
		return addToStack(intention, null, index);
	}
	
	public boolean addToStack(Intention intention, Object context, int index) {
		return stacks.get(index).add(new StackElement(intention, context));
	}
	
	/**
	 * Looks at the top-stack element of the stack with the given index.
	 * @param index	index of the used stack
	 * @return intention on-top of the stack with the given index.
	 */
	public Intention peekStack(int index) {
		StackElement se = stacks.get(index).peek();
		se.intention.setObjectContainingContext(se.context);
		se.intention.setParent(this);
		return se.intention;
	}
	
	/**
	 * Returns and removes the top-stack element of the stack wit the given index.
	 * @param index	index of used stack.
	 * @return	intention on-top of the stack with the given index.
	 */
	/*
	public Intention popStack(int index) {
		StackElement se = stacks.get(index).pop();
		se.intention.setObjectContainingContext(se.context);
		se.intention.setParent(this);
		return se.intention;
	}
	*/

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
		Stack<StackElement> toDel = null;
		for(Stack<StackElement> s : stacks) {
			if(s.peek().intention.equals( subgoal )) {
				s.pop();
				if(s.isEmpty()) {
					toDel = s;
				}
				break;
			}
		}
		
		if(toDel != null)
			stacks.remove(toDel);
		
		if(parent != null && stacks.isEmpty())
			parent.onSubgoalFinished(this);
	}
}
