package com.github.angerona.fw.island.operators;

import static com.github.angerona.fw.island.data.IslandDesires.FILL_BATTERY;
import static com.github.angerona.fw.island.data.IslandDesires.FIND_SHELTER;
import static com.github.angerona.fw.island.data.IslandDesires.FINISH_WORK;
import static com.github.angerona.fw.island.data.IslandDesires.SECURE_SITE;
import static com.github.angerona.fw.island.data.IslandDesires.isEqual;
import static com.github.angerona.fw.island.enums.ActionId.ASSEMBLE_PARTS;
import static com.github.angerona.fw.island.enums.ActionId.CHARGE_BATTERY;
import static com.github.angerona.fw.island.enums.ActionId.COVER_SITE;
import static com.github.angerona.fw.island.enums.ActionId.ENTER_CAVE;
import static com.github.angerona.fw.island.enums.ActionId.LEAVE_CAVE;
import static com.github.angerona.fw.island.enums.ActionId.MOVE_TO_HQ;
import static com.github.angerona.fw.island.enums.ActionId.MOVE_TO_SITE;
import static com.github.angerona.fw.island.enums.ActionId.UNCOVER_SITE;
import static com.github.angerona.fw.island.enums.Location.AT_HQ;
import static com.github.angerona.fw.island.enums.Location.AT_SITE;
import static com.github.angerona.fw.island.enums.Location.ON_THE_WAY_1;
import static com.github.angerona.fw.island.enums.Location.ON_THE_WAY_2;
import static com.github.angerona.fw.island.enums.Location.ON_THE_WAY_3;
import static com.github.angerona.fw.island.enums.Weather.STORM_OR_RAIN;
import static com.github.angerona.fw.island.enums.Weather.THUNDERSTORM;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.data.IslandAction;
import com.github.angerona.fw.island.enums.Location;
import com.github.angerona.fw.island.enums.Weather;
import com.github.angerona.fw.island.operators.parameter.AdvancedPlanParameter;

/**
 * A simple SubgoalGenerationOperator for the island-scenario. It's basically a
 * hard coded implementation of the trail-based planning-component
 * 
 * @author Manuel Barbi
 *
 */
public class IslandSubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	@Override
	protected Boolean processImpl(PlanParameter param) {
		Area area = param.getAgent().getComponent(Area.class);
		PlanComponent plans = param.getActualPlan();

		if (area != null && !plans.getPlans().isEmpty()) {
			Desire desire = plans.getPlans().get(0).getFulfillsDesire();
			plans.clear();

			List<Action> sequence = calcMinSequence(desire, param.getAgent(), area);
			param.report(sequence.toString());

			if (sequence != null) {
				Subgoal plan = new Subgoal(param.getAgent(), desire);
				Stack<PlanElement> temp = new Stack<>();

				for (Action a : sequence) {
					temp.push(new PlanElement(a));
				}

				plan.newStack();
				plan.setStack(0, temp);
				plans.addPlan(plan);
			}

			return true;
		}

		return false;
	}

	public static List<Action> calcMinSequence(Desire desire, Agent agent, Area area) {
		if (isEqual(desire, FINISH_WORK)) {
			return min(finishWorkFromHQ(new LinkedList<Action>(), area.getLocation(), agent, area, 0, area.isSecured()),
					finishWorkFromCave(new LinkedList<Action>(), area.getLocation(), agent, area, 0, area.isSecured()));
		} else if (isEqual(desire, FILL_BATTERY)) {
			return fillBattery(new LinkedList<Action>(), area.getLocation(), agent, area, 0);
		} else if (isEqual(desire, FIND_SHELTER)) {
			return min(findShelterFromHQ(new LinkedList<Action>(), area.getLocation(), agent, area, 0),
					findShelterFromCave(new LinkedList<Action>(), area.getLocation(), agent, area, 0));
		} else if (isEqual(desire, SECURE_SITE)) {
			return min(secureSiteFromHQ(new LinkedList<Action>(), area.getLocation(), agent, area, 0, area.isSecured()),
					secureSiteFromCave(new LinkedList<Action>(), area.getLocation(), agent, area, 0, area.isSecured()));
		}

		return null;
	}

	protected static List<Action> finishWorkFromHQ(List<Action> seq, Location current, Agent agent, Area area, int tick, boolean secured) {
		switch (current) {
		case AT_HQ:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return finishWorkFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_1 : ON_THE_WAY_2, agent, area, tick + 1, secured);
		case ON_THE_WAY_1:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return finishWorkFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_2 : ON_THE_WAY_3, agent, area, tick + 1, secured);
		case ON_THE_WAY_2:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return finishWorkFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_3 : AT_SITE, agent, area, tick + 1, secured);
		case ON_THE_WAY_3:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return finishWorkFromHQ(seq, AT_SITE, agent, area, tick + 1, secured);
		case AT_SITE:
			if (secured) {
				seq.add(new IslandAction(agent, UNCOVER_SITE));
				return finishWorkFromHQ(seq, current, agent, area, tick + 1, false);
			}

			seq.add(new IslandAction(agent, ASSEMBLE_PARTS));
			return seq;
		default:
			return null;
		}
	}

	protected static List<Action> finishWorkFromCave(List<Action> seq, Location current, Agent agent, Area area, int tick, boolean secured) {
		switch (current) {
		case IN_CAVE:
			seq.add(new IslandAction(agent, LEAVE_CAVE));
			return finishWorkFromCave(seq, AT_SITE, agent, area, tick + 1, secured);
		case AT_SITE:
			if (secured) {
				seq.add(new IslandAction(agent, UNCOVER_SITE));
				return finishWorkFromCave(seq, current, agent, area, tick + 1, false);
			}

			seq.add(new IslandAction(agent, ASSEMBLE_PARTS));
			return seq;
		default:
			return null;
		}
	}

	protected static List<Action> fillBattery(List<Action> seq, Location current, Agent agent, Area area, int tick) {
		switch (current) {
		case IN_CAVE:
			seq.add(new IslandAction(agent, LEAVE_CAVE));
			return fillBattery(seq, AT_SITE, agent, area, tick + 1);
		case AT_SITE:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
			return fillBattery(seq, isSlow(area, tick) ? ON_THE_WAY_3 : ON_THE_WAY_2, agent, area, tick + 1);
		case ON_THE_WAY_3:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
			return fillBattery(seq, isSlow(area, tick) ? ON_THE_WAY_2 : ON_THE_WAY_1, agent, area, tick + 1);
		case ON_THE_WAY_2:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
			return fillBattery(seq, isSlow(area, tick) ? ON_THE_WAY_1 : AT_HQ, agent, area, tick + 1);
		case ON_THE_WAY_1:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
			return fillBattery(seq, AT_HQ, agent, area, tick + 1);
		case AT_HQ:
			seq.add(new IslandAction(agent, CHARGE_BATTERY));
			return seq;
		default:
			return null;
		}
	}

	protected static List<Action> findShelterFromHQ(List<Action> seq, Location current, Agent agent, Area area, int tick) {
		switch (current) {
		case AT_HQ:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return findShelterFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_1 : ON_THE_WAY_2, agent, area, tick + 1);
		case ON_THE_WAY_1:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return findShelterFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_2 : ON_THE_WAY_3, agent, area, tick + 1);
		case ON_THE_WAY_2:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return findShelterFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_3 : AT_SITE, agent, area, tick + 1);
		case ON_THE_WAY_3:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return findShelterFromHQ(seq, AT_SITE, agent, area, tick + 1);
		case AT_SITE:
			seq.add(new IslandAction(agent, ENTER_CAVE));
		case IN_CAVE:
			return seq;
		default:
			return null;
		}
	}

	protected static List<Action> findShelterFromCave(List<Action> seq, Location current, Agent agent, Area area, int tick) {
		switch (current) {
		case IN_CAVE:
			seq.add(new IslandAction(agent, LEAVE_CAVE));
			return findShelterFromCave(seq, AT_SITE, agent, area, tick + 1);
		case AT_SITE:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
			return findShelterFromCave(seq, isSlow(area, tick) ? ON_THE_WAY_3 : ON_THE_WAY_2, agent, area, tick + 1);
		case ON_THE_WAY_3:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
			return findShelterFromCave(seq, isSlow(area, tick) ? ON_THE_WAY_2 : ON_THE_WAY_1, agent, area, tick + 1);
		case ON_THE_WAY_2:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
			return findShelterFromCave(seq, isSlow(area, tick) ? ON_THE_WAY_1 : AT_HQ, agent, area, tick + 1);
		case ON_THE_WAY_1:
			seq.add(new IslandAction(agent, MOVE_TO_HQ));
		case AT_HQ:
			return seq;
		default:
			return null;
		}
	}

	protected static List<Action> secureSiteFromHQ(List<Action> seq, Location current, Agent agent, Area area, int tick, boolean secured) {
		if (secured) {
			return seq;
		}

		switch (current) {
		case AT_HQ:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return secureSiteFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_1 : ON_THE_WAY_2, agent, area, tick + 1, secured);
		case ON_THE_WAY_1:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return secureSiteFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_2 : ON_THE_WAY_3, agent, area, tick + 1, secured);
		case ON_THE_WAY_2:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return secureSiteFromHQ(seq, isSlow(area, tick) ? ON_THE_WAY_3 : AT_SITE, agent, area, tick + 1, secured);
		case ON_THE_WAY_3:
			seq.add(new IslandAction(agent, MOVE_TO_SITE));
			return secureSiteFromHQ(seq, AT_SITE, agent, area, tick + 1, secured);
		case AT_SITE:
			seq.add(new IslandAction(agent, COVER_SITE));
			return seq;
		default:
			return null;
		}
	}

	protected static List<Action> secureSiteFromCave(List<Action> seq, Location current, Agent agent, Area area, int tick, boolean secured) {
		if (secured) {
			return seq;
		}

		switch (current) {
		case IN_CAVE:
			seq.add(new IslandAction(agent, LEAVE_CAVE));
			return secureSiteFromCave(seq, AT_SITE, agent, area, tick + 1, secured);
		case AT_SITE:
			seq.add(new IslandAction(agent, COVER_SITE));
			return seq;
		default:
			return null;
		}
	}

	protected static boolean isSlow(Area area, int tick) {
		Weather w = area.getWeather().getWeather(tick);
		return w == STORM_OR_RAIN || w == THUNDERSTORM;
	}

	protected static List<Action> min(List<Action> fst, List<Action> snd) {
		return (fst != null && (snd == null || fst.size() <= snd.size())) ? fst : snd;
	}

	@Override
	protected PlanParameter getEmptyParameter() {
		return new AdvancedPlanParameter();
	}

}
