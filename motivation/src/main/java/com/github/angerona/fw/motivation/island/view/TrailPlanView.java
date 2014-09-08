package com.github.angerona.fw.motivation.island.view;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.motivation.island.comp.TrailBasedPlans;
import com.github.angerona.fw.motivation.plan.ActionEdge;
import com.github.angerona.fw.motivation.plan.StateNode;

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

				for (StateNode node : plans.getPlan(key)) {
					printPlan(reval, new HashSet<String>(), node);
				}
			}

			return reval;
		}
		return null;
	}

	private void printPlan(List<String> reval, Set<String> visited, StateNode node) {
		reval.add("|_____" + node);
		visited.add(node.toString());

		for (ActionEdge a : node) {
			reval.add("|__________" + a);
		}

		for (ActionEdge a : node) {
			if (a.getGoal() != null && !visited.contains(a.getGoal().toString())) {
				printPlan(reval, visited, a.getGoal());
			}
		}
	}

	@Override
	public Class<? extends Entity> getObservedType() {
		return TrailBasedPlans.class;
	}

}
