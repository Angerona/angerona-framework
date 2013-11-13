package com.github.angerona.fw.gui.view;

import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.logic.Desires;

/**
 * A List-View of the desires of an agent.
 * @author Tim Janus
 *
 */
public class DesiresView extends ListViewColored {

	/** kill warning */
	private static final long serialVersionUID = 1628457822916960917L;

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof Desires) {
			Desires des = (Desires)obj;
			List<String> reval = new LinkedList<String>();
			for(FolFormula f : des.getTweety()) {
				reval.add(f.toString());
			}
			
			return reval;
		}
		return null;
	}

	@Override
	public Class<? extends Desires> getObservedType() {
		return Desires.class;
	}

}
