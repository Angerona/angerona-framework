package com.github.angerona.fw.motivation.plans.impl;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.island.WeatherChart;
import com.github.angerona.fw.island.enums.Location;
import com.github.angerona.fw.island.enums.Weather;
import com.github.angerona.fw.motivation.ActionSequence;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.operators.BasePlanOperator;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class TrailOperator extends BasePlanOperator {
	
	private BeliefState b;

	@Override
	protected Set<ActionSequence> getSequences(Desire d) {
		List<ActionSequence> list = new LinkedList<>();

	/*	for (StateNode node : param.getPlans()) {
			traverse(param, node, list, new ActionSequence(), new HashSet<TrailElem>(), true);
		} */

		Set seqs = new HashSet<ActionSequence>();
		seqs.addAll(list);
		return seqs;
	}

/*	private void traverse(PlanParam param, StateNode node, List<ActionSequence> list, ActionSequence seq,
			HashSet<TrailElem> used, boolean skip) {

		if (node != null) {

			if (!skip) {

				if (node.getEgdeCount() == 0) {
					list.add(seq);
					return;
				}

				for (TrailElem act : node) {
					if (reason(param, act.getCondition(), seq.getLength()) && !used.contains(act)) {
						traverse(param, act.getGoal(), list, branch(seq, act), branch(used, act), false);
					}
				}

			} else {
				if (param.getLocation().toString().equalsIgnoreCase(node.getName())) {
					traverse(param, node, list, seq, used, false);
				} else {
					for (TrailElem act : node) {
						if (!used.contains(act)) {
							traverse(param, act.getGoal(), list, seq, branch(used, act), true);
						}
					}
				}
			}
		} else if (!skip) {
			list.add(seq);
		}
	} */

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
				return param.getB().verify(f);
			}
		}

		return true;
	}

/*	private ActionSequence branch(ActionSequence seq, TrailElem act) {
		return seq.clone().add(ActionId.valueOf(act.getActionId().toUpperCase()));
	}

	private HashSet<TrailElem> branch(HashSet<TrailElem> used, TrailElem act) {
		@SuppressWarnings("unchecked")
		HashSet<TrailElem> cln = (HashSet<TrailElem>) used.clone();
		cln.add(act);
		return cln;
	} */
	
	private class PlanParam {

		protected BeliefState b;
		protected Location location;
		protected WeatherChart weather;

		public PlanParam(BeliefState b, Location location, WeatherChart weather) {

			this.b = b;
			this.location = location;
			this.weather = weather;
		}

		public Location getLocation() {
			return location;
		}

		public WeatherChart getWeather() {
			return weather;
		}

		public BeliefState getB() {
			return b;
		}
		
	}

}
