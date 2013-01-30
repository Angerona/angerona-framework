package angerona.fw.gui.view;

import java.util.LinkedList;
import java.util.List;

import angerona.fw.internal.Entity;
import angerona.fw.logic.SecrecyKnowledge;
import angerona.fw.logic.Secret;

/**
 * View for the confidential knowledge of the agents. It shows the confidential targets
 * in a list.
 * 
 * @author Tim Janus
 */
public class SecrecyView extends ListViewColored<SecrecyKnowledge> {
	
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
