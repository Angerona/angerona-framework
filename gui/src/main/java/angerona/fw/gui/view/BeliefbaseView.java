package angerona.fw.gui.view;

import java.util.List;

import angerona.fw.Agent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.internal.Entity;
import angerona.fw.internal.IdGenerator;

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
	public void init() {
		super.init();
		
		Agent ag = (Agent)IdGenerator.getEntityWithId(this.ref.getParent());
		String postfix = "";
		if(ag.getBeliefs().getWorldKnowledge() == ref) {
			postfix = "World";
		} else {
			for(String key : ag.getBeliefs().getViewKnowledge().keySet()) {
				BaseBeliefbase bb = ag.getBeliefs().getViewKnowledge().get(key);
				if(bb == ref) {
					postfix = "View->"+key;
					break;
				}
			}
		}
		setTitle(ag.getName() + " - " + postfix);
	}
	
	@Override
	public Class<?> getObservationObjectType() {
		return BaseBeliefbase.class;
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
