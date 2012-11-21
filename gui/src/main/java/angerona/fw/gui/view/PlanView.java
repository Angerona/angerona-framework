package angerona.fw.gui.view;

import java.util.List;

import angerona.fw.PlanComponent;
import angerona.fw.internal.Entity;

public class PlanView extends ListViewColored<PlanComponent> {

	/** kill warning */
	private static final long serialVersionUID = -8417236877682507065L;

	@Override
	public void init() {
		super.init();
		setTitle("Plan");
	}
	
	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof PlanComponent) {
			//MasterPlan p = (MasterPlan)obj;
			// TODO, as tree view
			return null;
		}
		return null;
	}
	
	/*
	private List<String> rek(Intention intent, int depth) {
		List<String> reval = new LinkedList<String>();
		String prefix = "-";
		for(int i=0; i<depth; ++i) {
			prefix+="-";
		}
		if(intent instanceof Subgoal) {
			Subgoal sg = (Subgoal) intent;
			reval.add(prefix + intent.toString());
			for(int i=0; i<sg.getNumberOfStacks(); ++i) {
				List<Intention> childs = sg.getStackAsIntentionList(i);
				for(Intention child : childs) {
					reval.addAll(0, rek(child, depth+1));
				}
			}
		} else if(intent instanceof Skill) {
			reval.add(0, prefix + intent.toString());
		}
		return reval;
	}
	*/

	@Override
	public void setObservationObject(Object obj) {
		if( !(obj instanceof PlanComponent))
			throw new IllegalArgumentException();
		
		ref = (PlanComponent)obj;
		actual = ref;
	}

	@Override
	public Class<?> getObservationObjectType() {
		return PlanComponent.class;
	}
}
