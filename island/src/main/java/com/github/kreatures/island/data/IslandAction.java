package com.github.kreatures.island.data;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.island.enums.ActionId;

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
