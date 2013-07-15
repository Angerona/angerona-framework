package angerona.fw.gui.view;

import java.util.LinkedList;
import java.util.List;

import angerona.fw.PlanComponent;
import angerona.fw.Subgoal;
import angerona.fw.internal.Entity;

public class PlanView extends ListViewColored {
	
	/** kick warning */
	private static final long serialVersionUID = 7901339015161976585L;

	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		List<String> reval = new LinkedList<>();
		if(obj instanceof PlanComponent) {
			PlanComponent p = (PlanComponent)obj;
			for(Subgoal sg : p.getPlans()) {
				reval.add(sg.toString());
			}
		}
		return reval;
	}

	@Override
	public Class<? extends PlanComponent> getObservedType() {
		return PlanComponent.class;
	}
}
