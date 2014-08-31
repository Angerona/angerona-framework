package com.github.angerona.fw.motivation.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.GenMotiveCouplings;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class GenCouplingsView extends ListViewColored {

	private static final long serialVersionUID = 2622431251856572958L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof GenMotiveCouplings<?, ?>) {
			List<String> reval = new LinkedList<String>();

			for (MotiveCoupling<?, ?> mc : (GenMotiveCouplings<?, ?>) entity) {
				reval.add(mc.toString());
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return GenMotiveCouplings.class;
	}

}
