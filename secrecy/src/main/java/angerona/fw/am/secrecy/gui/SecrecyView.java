package angerona.fw.am.secrecy.gui;

import java.util.LinkedList;
import java.util.List;

import angerona.fw.am.secrecy.components.SecrecyKnowledge;
import angerona.fw.gui.view.ListViewColored;
import angerona.fw.internal.Entity;
import angerona.fw.logic.Secret;

/**
 * View for the confidential knowledge of the agents. It shows the confidential targets
 * in a list.
 * 
 * @author Tim Janus
 */
public class SecrecyView extends ListViewColored<SecrecyKnowledge> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5599204624867315364L;

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		SecrecyKnowledge ck = (SecrecyKnowledge)obj;
		
		List<String> reval = new LinkedList<String>();
		for(Secret ct : ck.getTargets()) {
			reval.add(ct.toString());
		}
		
		return reval;
	}

	@Override
	public Class<? extends SecrecyKnowledge> getObservedType() {
		return SecrecyKnowledge.class;
	}
	
}
