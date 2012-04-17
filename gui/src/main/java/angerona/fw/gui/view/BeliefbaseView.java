package angerona.fw.gui.view;

import java.util.List;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.report.Entity;

/**
 * Generic ui view to show a Beliefbase. It shows its content in a list
 * and uses the generic base class ListViewColored
 * 
 * @author Tim Janus
 */
public class BeliefbaseView extends ListViewColored<BaseBeliefbase> {
	
	/** kill warning */
	private static final long serialVersionUID = -3706152280500718930L;
	
	
	@Override
	public Class<?> getObservationObjectType() {
		return BaseBeliefbase.class;
	}

	@Override
	public String getComponentTypeName() {
		return "Default Beliefbase-Component";
	}

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof BaseBeliefbase) {
			BaseBeliefbase bb = (BaseBeliefbase)obj;
			return bb.getAtoms();
		}
		
		return null;
	}

	@Override
	public void setObservationObject(Object obj) {
		if(! (obj instanceof BaseBeliefbase)) {
			throw new IllegalArgumentException("Observation Object must be of type '" +  BaseBeliefbase.class.getSimpleName() + "'");
		}
		this.ref = (BaseBeliefbase)obj;
		this.actual = this.ref;
	}
	
}
