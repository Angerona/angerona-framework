package com.github.kreaturesfw.secrecy.gui;

import java.util.LinkedList;
import java.util.List;

import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.gui.view.ListViewColored;
import com.github.kreaturesfw.secrecy.Secret;
import com.github.kreaturesfw.secrecy.components.SecrecyKnowledge;

/**
 * View for the confidential knowledge of the agents. It shows the confidential targets
 * in a list.
 * 
 * @author Tim Janus
 */
public class SecrecyView extends ListViewColored {
	
	/** kill warning*/
	private static final long serialVersionUID = 5599204624867315364L;

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof SecrecyKnowledge) {
			SecrecyKnowledge ck = (SecrecyKnowledge)obj;
			
			List<String> reval = new LinkedList<String>();
			for(Secret ct : ck.getSecrets()) {
				reval.add(ct.toString());
			}
			
			return reval;
		}
		return new LinkedList<>();
	}

	@Override
	public Class<? extends SecrecyKnowledge> getObservedType() {
		return SecrecyKnowledge.class;
	}
	
}
