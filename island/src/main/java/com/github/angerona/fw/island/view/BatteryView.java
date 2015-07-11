package com.github.angerona.fw.island.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.island.components.Battery;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class BatteryView extends ListViewColored {

	private static final long serialVersionUID = -6063338442612426053L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		List<String> reval = new LinkedList<>();

		if (entity instanceof Battery) {
			Battery akku = (Battery) entity;
			reval.add("Charge: " + akku.getCharge());
			reval.add("Damaged: " + akku.isDamaged());
			return reval;
		} else {
			reval.add("not able to display " + entity.getClass().getSimpleName());
		}

		return reval;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return Battery.class;
	}

}
