package com.github.angerona.fw.motivation.reliable.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.island.comp.Battery;
import com.github.angerona.fw.motivation.reliable.impl.TimeSlots;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TimeSlotsView extends ListViewColored {

	private static final long serialVersionUID = -3837032144260387351L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof Battery) {
			List<String> reval = new LinkedList<String>();

			for (Integer slot : (TimeSlots) entity) {
				reval.add(String.valueOf(slot));
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return TimeSlots.class;
	}

}
