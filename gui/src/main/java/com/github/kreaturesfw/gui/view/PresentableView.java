package com.github.kreaturesfw.gui.view;

import java.util.List;

import com.github.kreaturesfw.core.comp.Presentable;
import com.github.kreaturesfw.core.internal.Entity;

/**
 * this view does not observe a certain type, but is able to display all
 * instances of Presentable
 * 
 * @author Manuel Barbi
 *
 */
public class PresentableView extends ListViewColored {

	private static final long serialVersionUID = -5110564232247267158L;

	@Override
	protected List<String> getStringRepresentation(Entity item) {
		if (item instanceof Presentable) {
			return ((Presentable) item).getRepresentation();
		}

		throw new IllegalArgumentException("entity does not have the type Presentable");
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		// this is on purpose
		return null;
	}

}
