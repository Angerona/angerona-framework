package com.github.angerona.fw.motivation.plans.impl;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TrailPlanView extends ListViewColored {

	private static final long serialVersionUID = 96293578702840657L;

	@Override
	protected List<String> getStringRepresentation(Entity entity) {
		if (entity instanceof TrailBasedPlans) {
			TrailBasedPlans plans = (TrailBasedPlans) entity;
			List<String> reval = new LinkedList<String>();

			for (String key : plans) {
				reval.add(key);

			/*	for (StateNode node : plans.getTrails(key)) {
					printPlan(reval, new HashSet<String>(), node);
				} */
			}

			return reval;
		}
		return null;
	}

/*	private void printPlan(List<String> reval, Set<String> visited, Trail trail) {
		reval.add("|_____" + trail);
		visited.add(node.toString());

		for (TrailElem a : node) {
			reval.add("|__________" + a);
		}

		for (TrailElem a : node) {
			if (a.getGoal() != null && !visited.contains(a.getGoal().toString())) {
				printPlan(reval, visited, a.getGoal());
			}
		}
	} */

	@Override
	public Class<? extends Entity> getObservedType() {
		return TrailBasedPlans.class;
	}

}
