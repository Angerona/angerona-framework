package com.github.angerona.fw.motivation.operators.temp.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.operators.temp.Intentions;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IntentionsView extends ListViewColored {

	private static final long serialVersionUID = 3725118072165693132L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof Intentions) {
			List<String> reval = new LinkedList<String>();

			Desire selected = ((Intentions) entity).getSelected();
			if (selected != null) {
				reval.add(selected.toString());
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return Intentions.class;
	}

}
