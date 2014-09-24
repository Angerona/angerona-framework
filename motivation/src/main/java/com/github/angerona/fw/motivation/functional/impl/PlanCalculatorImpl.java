package com.github.angerona.fw.motivation.functional.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.dao.PlanParam;
import com.github.angerona.fw.motivation.functional.PlanCalculator;
import com.github.angerona.fw.motivation.island.enums.ActionId;
import com.github.angerona.fw.motivation.island.enums.Weather;
import com.github.angerona.fw.motivation.plan.ActionEdge;
import com.github.angerona.fw.motivation.plan.ActionSequence;
import com.github.angerona.fw.motivation.plan.StateNode;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class PlanCalculatorImpl implements PlanCalculator<ActionId> {

	@Override
	public ActionSequence<ActionId> calc(PlanParam param) {
		List<ActionSequence<ActionId>> list = new LinkedList<>();

		for (StateNode node : param.getPlans()) {
			traverse(param, node, list, new ActionSequence<ActionId>(), new HashSet<ActionEdge>(), true);
		}

		if (!list.isEmpty()) {
			return Collections.min(list).setSafeWindow(param.getWeather().getSafeWindow());
		}

		return null;
	}

	private void traverse(PlanParam param, StateNode node, List<ActionSequence<ActionId>> list, ActionSequence<ActionId> seq,
			HashSet<ActionEdge> used, boolean skip) {

		if (node != null) {

			if (!skip) {

				if (node.getEgdeCount() == 0) {
					list.add(seq);
					return;
				}

				for (ActionEdge act : node) {
					if (reason(param, act.getCondition(), seq.getDuration()) && !used.contains(act)) {
						traverse(param, act.getGoal(), list, branch(seq, act), branch(used, act), false);
					}
				}

			} else {
				if (param.getLocation().toString().equalsIgnoreCase(node.getName())) {
					traverse(param, node, list, seq, used, false);
				} else {
					for (ActionEdge act : node) {
						if (!used.contains(act)) {
							traverse(param, act.getGoal(), list, seq, branch(used, act), true);
						}
					}
				}
			}
		} else if (!skip) {
			list.add(seq);
		}
	}

	/**
	 * more or less dirty work around
	 * 
	 * @param param
	 * @param f
	 * @param t
	 * @return
	 */
	private boolean reason(PlanParam param, FolFormula f, int t) {

		if (f != null) {
			String arg = f.toString();
			Weather w = param.getWeather().getWeather(t);

			if (arg.equals("slow")) {
				return (w != Weather.SUN && w != Weather.CLOUDS);
			} else if (arg.equals("-slow")) {
				return (w == Weather.SUN || w == Weather.CLOUDS);
			} else {
				return param.reason(f);
			}
		}

		return true;
	}

	private ActionSequence<ActionId> branch(ActionSequence<ActionId> seq, ActionEdge act) {
		return seq.clone().add(ActionId.valueOf(act.getActionId().toUpperCase()));
	}

	private HashSet<ActionEdge> branch(HashSet<ActionEdge> used, ActionEdge act) {
		@SuppressWarnings("unchecked")
		HashSet<ActionEdge> cln = (HashSet<ActionEdge>) used.clone();
		cln.add(act);
		return cln;
	}

}
