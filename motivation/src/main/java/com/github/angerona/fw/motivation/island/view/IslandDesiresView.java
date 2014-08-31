package com.github.angerona.fw.motivation.island.view;

import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.island.comp.IslandDesires;
import com.github.angerona.fw.motivation.view.GenCouplingsView;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandDesiresView extends GenCouplingsView {

	private static final long serialVersionUID = -8533320359231996016L;

	@Override
	public Class<? extends Entity> getObservedType() {
		return IslandDesires.class;
	}

}
