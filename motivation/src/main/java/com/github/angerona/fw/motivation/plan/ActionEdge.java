package com.github.angerona.fw.motivation.plan;

import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class ActionEdge {

	protected String actionId;
	protected FolFormula condition;
	protected StateNode goal;

	ActionEdge(String actionId) {
		setActionId(actionId);
	}

	ActionEdge setActionId(String actionId) {
		if (actionId == null) {
			throw new NullPointerException("action-id must not be null");
		}

		this.actionId = actionId;
		return this;
	}

	ActionEdge setCondition(FolFormula condition) {
		this.condition = condition;
		return this;
	}

	ActionEdge setGoal(StateNode goal) {
		this.goal = goal;
		return this;
	}

	public String getActionId() {
		return actionId;
	}

	public FolFormula getCondition() {
		return condition;
	}

	public StateNode getGoal() {
		return goal;
	}

	@Override
	public String toString() {
		return "[" + actionId + ", " + condition + ", " + goal + "]";
	}

}
