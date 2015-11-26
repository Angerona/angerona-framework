package com.github.kreaturesfw.island.data;

import com.github.kreaturesfw.core.legacy.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.island.enums.ActionId;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandAction extends Action {

	private final ActionId id;

	public IslandAction(Agent agent, ActionId id) {
		super(agent.getName());
		this.setAgent(agent);
		this.id = id;
	}

	public ActionId getId() {
		return id;
	}

	@Override
	public String toString() {
		return "[" + id + "]";
	}

}
