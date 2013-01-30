package angerona.fw.gui.view;

import java.util.List;

import angerona.fw.PlanComponent;
import angerona.fw.internal.Entity;

public class PlanView extends ListViewColored<PlanComponent> {
	
	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		if(obj instanceof PlanComponent) {
			//MasterPlan p = (MasterPlan)obj;
			// TODO, as tree view
			return null;
		}
		return null;
	}

	@Override
	public Class<? extends PlanComponent> getObservedType() {
		return PlanComponent.class;
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
}
