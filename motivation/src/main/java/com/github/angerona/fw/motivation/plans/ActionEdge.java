package com.github.angerona.fw.motivation.plans;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actionId == null) ? 0 : actionId.hashCode());
		result = prime * result + ((condition == null) ? 0 : condition.hashCode());
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionEdge other = (ActionEdge) obj;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		if (condition == null) {
			if (other.condition != null)
				return false;
		} else if (!condition.equals(other.condition))
			return false;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		return true;
	}

}
