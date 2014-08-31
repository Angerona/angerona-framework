package com.github.angerona.fw.motivation.view;

import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class CouplingsView extends GenCouplingsView {

	private static final long serialVersionUID = -3941634394465867313L;

	@Override
	public Class<? extends Entity> getObservedType() {
		return MotiveCouplings.class;
	}

}
