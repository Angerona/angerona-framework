package com.github.angerona.fw.motivation.island;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.motivation.island.enums.ActionId;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandAction extends Action {

	private final ActionId id;

	public IslandAction(String senderId, ActionId id) {
		super(senderId);
		this.id = id;
	}

	public ActionId getId() {
		return id;
	}

}
