package com.github.angerona.fw.simple.operators;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.BaseIntentionUpdateOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.simple.operators.parameter.SimplePlanParameter;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class FilterOperator extends BaseIntentionUpdateOperator {

	@Override
	protected PlanElement processImpl(PlanParameter param) {
		Desires desireComp = param.getAgent().getComponent(Desires.class);
		PlanComponent plans = param.getActualPlan();

		if (plans == null) {
			param.report("no plan component found");
			return null;
		}

		Desire pursued = null;
		if (!plans.getPlans().isEmpty()) {
			pursued = plans.getPlans().get(0).getFulfillsDesire();
		}

		if (desireComp == null) {
			param.report("no desire component found");
			return null;
		}

		Set<Desire> desires = desireComp.getDesires();

		Map<Desire, Subgoal> intentions = new LinkedHashMap<>();
		for (Subgoal sub : plans.getPlans()) {
			if (desires.contains(sub.getFulfillsDesire())) {
				intentions.put(sub.getFulfillsDesire(), sub);
			}
		}

		plans.clear();
		Desire goal = choose(desires, pursued, param);

		if (goal != null) {
			Subgoal tmp = intentions.remove(goal);
			plans.addPlan((tmp != null) ? tmp : new Subgoal(param.getAgent(), goal));
			param.report("agent has chosen " + goal);
		}

		for (Subgoal sub : intentions.values()) {
			plans.addPlan(sub);
		}

		return null;
	}

	/**
	 * @param options
	 *            a set of all desires, the agent currently has
	 * @param pursued
	 *            the currently pursued desire, if any
	 * @param param
	 *            the original plan-parameter
	 * @return the desire the agent will pursue
	 */
	protected abstract Desire choose(Set<Desire> options, Desire pursued, PlanParameter param);

	@Override
	protected PlanParameter getEmptyParameter() {
		return new SimplePlanParameter();
	}

}
