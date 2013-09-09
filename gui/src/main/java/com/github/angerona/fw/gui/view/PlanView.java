package com.github.angerona.fw.gui.view;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.internal.Entity;

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
