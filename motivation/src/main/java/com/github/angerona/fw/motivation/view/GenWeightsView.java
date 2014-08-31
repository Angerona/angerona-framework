package com.github.angerona.fw.motivation.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.GenLevelWeights;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class GenWeightsView extends ListViewColored {

	private static final long serialVersionUID = -844060141057554692L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof GenLevelWeights<?>) {
			List<String> reval = new LinkedList<String>();

			for (Entry<?, Double> e : (GenLevelWeights<?>) entity) {
				reval.add("(" + e.getKey() + ", " + e.getValue() + ")");
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return GenLevelWeights.class;
	}

}
