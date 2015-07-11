package com.github.angerona.fw.motivation.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;
import com.github.angerona.fw.motivation.data.Maslow;
import com.github.angerona.fw.motivation.data.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class RangesView extends ListViewColored {

	private static final long serialVersionUID = -8530060509397800148L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof WeightRanges) {
			List<String> reval = new LinkedList<>();

			for (Entry<Maslow, WeightRange> e : (WeightRanges) entity) {
				reval.add("(" + e.getKey() + ", " + e.getValue() + ")");
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return WeightRanges.class;
	}

}
