package com.github.angerona.fw.motivation.plan;

import net.sf.tweety.Formula;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <ID>
 * @param <F>
 * @param <S>
 */
public class ActionEdge<ID extends Comparable<ID>, F extends Formula> {

	protected ID actionId;
	protected F condition;
	protected StateNode<ID, F> goal;

	public ActionEdge(ID actionId, F condition, StateNode<ID, F> goal) {
		if (actionId == null) {
			throw new NullPointerException("action ID must not be null");
		}

		if (goal == null) {
			throw new NullPointerException("goal must not be null");
		}

		this.actionId = actionId;
		this.condition = condition;
		this.goal = goal;
	}

	public ID getActionId() {
		return actionId;
	}

	public F getCondition() {
		return condition;
	}

	public StateNode<ID, F> getGoal() {
		return goal;
	}

}
