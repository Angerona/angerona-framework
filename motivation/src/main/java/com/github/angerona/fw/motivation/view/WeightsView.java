package com.github.angerona.fw.motivation.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dao.impl.LevelWeights;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightsView extends ListViewColored {

	private static final long serialVersionUID = 3288429012967236129L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof LevelWeights) {
			List<String> reval = new LinkedList<String>();

			for (Entry<Maslow, Double> e : (LevelWeights) entity) {
				reval.add("(" + e.getKey() + ", " + e.getValue() + ")");
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return LevelWeights.class;
	}

}
