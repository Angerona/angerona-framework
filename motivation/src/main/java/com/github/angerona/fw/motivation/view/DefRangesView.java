package com.github.angerona.fw.motivation.view;

import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.DefaultRanges;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DefRangesView extends GenRangesView {

	private static final long serialVersionUID = 8176143163772982787L;

	@Override
	public Class<? extends Entity> getObservedType() {
		return DefaultRanges.class;
	}

}
