package com.github.angerona.fw.motivation.reliable.view;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.ActionSequence;
import com.github.angerona.fw.motivation.reliable.impl.ActionSequences;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class ActionSequencesView extends ListViewColored {

	private static final long serialVersionUID = 3632732387891845534L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof ActionSequences) {
			List<String> reval = new LinkedList<String>();

			for (Entry<String, ActionSequence> e : (ActionSequences) entity) {
				reval.add("(" + e.getKey() + ", " + e.getValue() + ")");
			}

			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return ActionSequences.class;
	}

}
