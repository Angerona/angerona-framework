package com.github.angerona.fw.island.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.island.components.Area;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class AreaView extends ListViewColored {

	private static final long serialVersionUID = -7235237466491984406L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		List<String> reval = new LinkedList<String>();

		if (entity instanceof Area) {
			Area area = (Area) entity;
			reval.add("Location: " + area.getLocation());
			reval.add("Expandsion: " + area.getExpansion());
			reval.add("Secured: " + area.isSecured());
			reval.add("Weather: " + area.getWeather());
		} else {
			reval.add("not able to display " + entity.getClass().getSimpleName());
		}

		return reval;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return Area.class;
	}

}
