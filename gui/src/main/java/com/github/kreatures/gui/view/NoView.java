package com.github.kreatures.gui.view;

import java.util.ArrayList;
import java.util.List;

import com.github.kreatures.core.internal.Entity;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class NoView extends ListViewColored {

	private static final long serialVersionUID = -5022038599558351426L;

	@Override
	protected List<String> getStringRepresentation(Entity item) {
		List<String> rep = new ArrayList<>();
		rep.add("<not able to display " + item.getClass().getSimpleName() + ">");
		return rep;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		// this is on purpose
		return null;
	}

}
