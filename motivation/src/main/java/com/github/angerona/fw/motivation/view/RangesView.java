package com.github.angerona.fw.motivation.view;

import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class RangesView extends GenRangesView {

	private static final long serialVersionUID = 665194948430419058L;

	@Override
	public Class<? extends Entity> getObservedType() {
		return WeightRanges.class;
	}

}
