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

	/** kill warning */
	private static final long serialVersionUID = 5545434636562463488L;

	@Override
	public void init() {
		super.init();
		setTitle("Confidential");
	}
	
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
	public void setObservationObject(Object obj) {
		if(! (obj instanceof SecrecyKnowledge)) {
			throw new IllegalArgumentException("Observation Object must be of type '" +  SecrecyKnowledge.class.getSimpleName() + "'");
		}
		this.ref = (SecrecyKnowledge)obj;
		this.actual = this.ref;
	}

	@Override
	public Class<?> getObservationObjectType() {
		return SecrecyKnowledge.class;
	}
	
}
