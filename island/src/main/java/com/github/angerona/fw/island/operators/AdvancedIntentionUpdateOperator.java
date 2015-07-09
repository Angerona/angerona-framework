package com.github.angerona.fw.island.operators;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.BaseIntentionUpdateOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.operators.parameter.AdvancedPlanParameter;
import com.github.angerona.fw.logic.Desires;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class AdvancedIntentionUpdateOperator extends BaseIntentionUpdateOperator {

	@Override
	protected PlanElement processImpl(PlanParameter param) {
		Desires desireComp = param.getAgent().getComponent(Desires.class);
		PlanComponent plans = param.getActualPlan();

		if (desireComp == null || plans == null)
			return null;

		// detect last pursued
		Desire pursued = null;
		if (!plans.getPlans().isEmpty()) {
			pursued = plans.getPlans().get(0).getFulfillsDesire();
		}

		Set<Desire> desires = desireComp.getDesires();

		// save existing plans
		Map<Desire, Subgoal> intentions = new LinkedHashMap<>();
		for (Subgoal sub : plans.getPlans()) {
			if (desires.contains(sub.getFulfillsDesire())) {
				intentions.put(sub.getFulfillsDesire(), sub);
			}
		}

		plans.clear();
		if (desires.isEmpty())
			return null;

		// choose intention from desires
		Desire chosen = chooseIntention(param, desires, pursued);

		if (chosen == null)
			return null;

		pick(plans, intentions, chosen, param);

		// recall existing plans
		for (Subgoal sub : intentions.values()) {
			plans.addPlan(sub);
		}

		return null;
	}

	protected void pick(PlanComponent plans, Map<Desire, Subgoal> intentions, Desire desire, PlanParameter param) {
		Subgoal tmp = intentions.remove(desire);
		plans.addPlan((tmp != null) ? tmp : new Subgoal(param.getAgent(), desire));
		param.report("agent has chosen " + desire);
	}

	protected abstract Desire chooseIntention(PlanParameter param, Set<Desire> desires, Desire pursued);

	@Override
	protected PlanParameter getEmptyParameter() {
		return new AdvancedPlanParameter();
	}

}
