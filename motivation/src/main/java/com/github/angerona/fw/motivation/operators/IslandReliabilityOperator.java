package com.github.angerona.fw.motivation.operators;

import static com.github.angerona.fw.island.operators.IslandSubgoalGenerationOperator.calcMinSequence;

import java.util.List;
import java.util.Stack;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;
import com.github.angerona.fw.motivation.dao.CouplingDao;
import com.github.angerona.fw.motivation.dao.TimeSlotDao;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.TimeSlots;
import com.github.angerona.fw.motivation.data.Maslow;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandReliabilityOperator extends BaseReliabilityOperator {

	@Override
	protected Void processImpl(PlanParameter param) {
		PlanComponent plans = param.getActualPlan();

		if (plans == null)
			return null;

		plans.clear();

		Area area = param.getAgent().getComponent(Area.class);
		CouplingDao<Maslow> couplings = param.getAgent().getComponent(MotiveCouplings.class);

		if (area == null || couplings == null)
			return null;

		Subgoal plan;
		Stack<PlanElement> temp;

		// try to calculate a plan for every desire

		for (Desire d : couplings.getDesires()) {

			List<Action> sequence = calcMinSequence(d, param.getAgent(), area);
			param.report(sequence.toString());

			if (sequence != null) {
				plan = new Subgoal(param.getAgent(), d);
				temp = new Stack<>();

				for (Action a : sequence) {
					temp.push(new PlanElement(a));
				}

				plan.newStack();
				plan.setStack(0, temp);
				plans.addPlan(plan);
			}
		}

		TimeSlotDao timeSlots = param.getAgent().getComponent(TimeSlots.class);

		if (timeSlots == null)
			return null;

		timeSlots.clear();

		// collect time slots

		if (area.getWeather() != null) {
			timeSlots.addSlot(area.getWeather().getSafeWindow());
		}

		Battery battery = param.getAgent().getComponent(Battery.class);

		if (battery != null) {
			timeSlots.addSlot(battery.getCharge());
		}

		return null;
	}

	@Override
	protected BaseReliabilityOperator clone() {
		return new IslandReliabilityOperator();
	}

}
