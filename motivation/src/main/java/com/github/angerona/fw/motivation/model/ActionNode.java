package com.github.angerona.fw.motivation.model;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <T>
 */
public class ActionNode<T> {

	protected T actionId;
	protected int duration;

	public ActionNode(T actionId, int duration) {
		this.actionId = actionId;
		this.duration = duration;
	}

	public T getActionId() {
		return actionId;
	}

	public int getDuration() {
		return duration;
	}

}
