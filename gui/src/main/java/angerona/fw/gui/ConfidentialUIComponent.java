package angerona.fw.gui;

import java.util.LinkedList;
import java.util.List;

import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.ConfidentialTarget;
import angerona.fw.report.Entity;

public class ConfidentialUIComponent extends ListViewColored<ConfidentialKnowledge> {

	/** kill warning */
	private static final long serialVersionUID = 5545434636562463488L;

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		ConfidentialKnowledge ck = (ConfidentialKnowledge)obj;
		
		List<String> reval = new LinkedList<String>();
		for(ConfidentialTarget ct : ck.getTargets()) {
			reval.add(ct.toString());
		}
		
		return reval;
	}

	@Override
	public void setObservationObject(Object obj) {
		if(! (obj instanceof ConfidentialKnowledge)) {
			throw new IllegalArgumentException("Observation Object must be of type '" +  ConfidentialKnowledge.class.getSimpleName() + "'");
		}
		this.ref = (ConfidentialKnowledge)obj;
		this.actual = this.ref;
	}

	@Override
	public String getComponentTypeName() {
		return "Confidential Knowledge";
	}

	@Override
	public Class<?> getObservationObjectType() {
		return ConfidentialKnowledge.class;
	}
	
}
