package com.github.angerona.fw.motivation.island.view;

import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.island.comp.IslandCouplings;
import com.github.angerona.fw.motivation.view.GenCouplingsView;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandCouplingsView extends GenCouplingsView {

	private static final long serialVersionUID = -4989947656364595844L;

	@Override
	public Class<? extends Entity> getObservedType() {
		return IslandCouplings.class;
	}

}
