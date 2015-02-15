package com.github.angerona.fw.island.comp;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class AreaView extends ListViewColored {

	private static final long serialVersionUID = -5614395245664119810L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof Area) {
			Area area = (Area) entity;
			List<String> reval = new LinkedList<String>();
			reval.add("Location: " + area.getLocation());
			reval.add("Expandsion: " + area.getExpansion());
			reval.add("Secured: " + area.isSecured());
			reval.add("Weather: " + area.getWeather());
			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return Area.class;
	}

}
