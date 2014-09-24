package com.github.angerona.fw.motivation.island.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.island.comp.IslandActions;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionMapView extends ListViewColored {

	private static final long serialVersionUID = 3262083863956889360L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof IslandActions) {
			IslandActions actions = (IslandActions) entity;
			List<String> reval = new LinkedList<String>();
			for (String key : actions) {
				reval.add(actions.get(key).toString());
			}
			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return IslandActions.class;
	}

}
