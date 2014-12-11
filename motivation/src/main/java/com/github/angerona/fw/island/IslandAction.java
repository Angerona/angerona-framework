package com.github.angerona.fw.island;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.island.enums.ActionId;

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
