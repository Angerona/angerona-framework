package com.github.angerona.fw.motivation.plan;

import net.sf.tweety.Formula;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <F>
 */
public class ActionEdge<F extends Formula> {

	protected String actionId;
	protected F condition;
	protected StateNode<F> goal;

	public ActionEdge(String actionId, F condition, StateNode<F> goal) {
		if (actionId == null) {
			throw new NullPointerException("action  must not be null");
		}

		if (goal == null) {
			throw new NullPointerException("goal must not be null");
		}

		this.actionId = actionId;
		this.condition = condition;
		this.goal = goal;
	}

	public String getActionId() {
		return actionId;
	}

	public F getCondition() {
		return condition;
	}

	public StateNode<F> getGoal() {
		return goal;
	}

}
