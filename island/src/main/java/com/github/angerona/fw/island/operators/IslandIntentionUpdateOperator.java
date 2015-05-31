package com.github.angerona.fw.island.operators;

import static com.github.angerona.fw.island.data.IslandDesires.FILL_BATTERY;
import static com.github.angerona.fw.island.data.IslandDesires.FIND_SHELTER;
import static com.github.angerona.fw.island.data.IslandDesires.FINISH_WORK;
import static com.github.angerona.fw.island.data.IslandDesires.SECURE_SITE;
import static com.github.angerona.fw.island.enums.Weather.THUNDERSTORM;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.BaseIntentionUpdateOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;
import com.github.angerona.fw.logic.Desires;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandIntentionUpdateOperator extends BaseIntentionUpdateOperator {

	@Override
	protected PlanElement processImpl(PlanParameter param) {
		Desires desireComp = param.getAgent().getComponent(Desires.class);
		Area area = param.getAgent().getComponent(Area.class);
		Battery battery = param.getAgent().getComponent(Battery.class);
		PlanComponent plans = param.getActualPlan();

		Desire pursued = null;
		if (!plans.getPlans().isEmpty()) {
			pursued = plans.getPlans().get(0).getFulfillsDesire();
		}

		if (desireComp != null && area != null) {
			Set<Desire> desires = desireComp.getDesires();

			Map<Desire, Subgoal> intentions = new LinkedHashMap<>();
			for (Subgoal sub : plans.getPlans()) {
				if (desires.contains(sub.getFulfillsDesire())) {
					intentions.put(sub.getFulfillsDesire(), sub);
				}
			}

			plans.clear();

			if ((battery != null && battery.getCharge() < 4 || pursued == FILL_BATTERY) && desires.contains(FILL_BATTERY)) {
				// if battery low or last pursued intention was 'fill battery'
				// then fill battery
				if (desires.contains(SECURE_SITE)) {
					// secure site before leaving
					plans.addPlan(get(intentions, SECURE_SITE, param.getAgent()));
				} else {
					plans.addPlan(get(intentions, FILL_BATTERY, param.getAgent()));
				}
			} else if (area.getWeather().getWeather(0) == THUNDERSTORM && desires.contains(FIND_SHELTER)) {
				// if weather is dangerous then find shelter
				if (desires.contains(SECURE_SITE)) {
					// secure site before leaving
					plans.addPlan(get(intentions, SECURE_SITE, param.getAgent()));
				} else {
					plans.addPlan(get(intentions, FIND_SHELTER, param.getAgent()));
				}
			} else if (desires.contains(FINISH_WORK)) {
				// other just work
				plans.addPlan(intentions.remove(FINISH_WORK));
			}
		}

		return null;
	}

	protected Subgoal get(Map<Desire, Subgoal> intentions, Desire desire, Agent agent) {
		Subgoal tmp = intentions.get(desire);
		return (tmp != null) ? tmp : new Subgoal(agent, desire);
	}

}
