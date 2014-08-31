package com.github.angerona.fw.motivation.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.GenWeightRanges;
import com.github.angerona.fw.motivation.model.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class GenRangesView extends ListViewColored {

	private static final long serialVersionUID = 7853556590479371628L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof GenWeightRanges<?>) {
			List<String> reval = new LinkedList<String>();

			for (Entry<?, WeightRange> e : (GenWeightRanges<?>) entity) {
				reval.add("(" + e.getKey() + ", " + e.getValue() + ")");
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return GenWeightRanges.class;
	}

}
