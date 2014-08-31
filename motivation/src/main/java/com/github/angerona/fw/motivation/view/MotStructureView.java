package com.github.angerona.fw.motivation.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.model.MotStrcEntry;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotStructureView extends ListViewColored {

	private static final long serialVersionUID = -8636032796162678071L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof MotStructure) {
			List<String> reval = new LinkedList<String>();

			for (MotStrcEntry mse : (MotStructure) entity) {
				reval.add(mse.toString());
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return MotStructure.class;
	}

}
