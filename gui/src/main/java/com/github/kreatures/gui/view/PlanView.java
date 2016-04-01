package com.github.kreatures.gui.view;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.PlanComponent;
import com.github.kreatures.core.Subgoal;
import com.github.kreatures.core.internal.Entity;

public class PlanView extends ListViewColored {
	
	/** kick warning */
	private static final long serialVersionUID = 7901339015161976585L;

	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(PlanView.class);
	
	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		List<String> reval = new LinkedList<>();
		if(obj instanceof PlanComponent) {
			PlanComponent p = (PlanComponent)obj;
			for(Subgoal sg : p.getPlans()) {
				reval.add(sg.toString());
			}
		}
		LOG.info(reval.toString());
		return reval;
	}

	@Override
	public Class<? extends PlanComponent> getObservedType() {
		return PlanComponent.class;
	}
}
