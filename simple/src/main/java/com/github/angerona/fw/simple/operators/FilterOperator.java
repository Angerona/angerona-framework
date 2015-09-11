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

		Desire pursued = null;
		if (!plans.getPlans().isEmpty()) {
			pursued = plans.getPlans().get(0).getFulfillsDesire();
		}

		if (desireComp != null) {
			Set<Desire> desires = desireComp.getDesires();

			Map<Desire, Subgoal> intentions = new LinkedHashMap<>();
			for (Subgoal sub : plans.getPlans()) {
				if (desires.contains(sub.getFulfillsDesire())) {
					intentions.put(sub.getFulfillsDesire(), sub);
				}
			}

			plans.clear();
			process(new ParamBundle(plans, desires, pursued, intentions, param));

			for (Subgoal sub : intentions.values()) {
				plans.addPlan(sub);
			}
		}

		return null;
	}

	protected abstract void process(ParamBundle param);

	protected void choose(Desire desire, ParamBundle param) {
		Subgoal tmp = param.getIntentions().remove(desire);
		param.getPlans().addPlan((tmp != null) ? tmp : new Subgoal(param.getParam().getAgent(), desire));
		param.getParam().report("agent has chosen " + desire);
	}

	@Override
	protected PlanParameter getEmptyParameter() {
		return new SimplePlanParameter();
	}

	protected class ParamBundle {

		private final PlanComponent plans;
		private final Set<Desire> desires;
		private final Desire pursued;
		private final Map<Desire, Subgoal> intentions;
		private final PlanParameter param;

		public ParamBundle(PlanComponent plans, Set<Desire> desires, Desire pursued, Map<Desire, Subgoal> intentions, PlanParameter param) {
			this.plans = plans;
			this.desires = desires;
			this.pursued = pursued;
			this.intentions = intentions;
			this.param = param;
		}

		/**
		 * @return the agent'S plan-component
		 */
		public PlanComponent getPlans() {
			return plans;
		}

		/**
		 * @return a set of all desires, the agent currently has
		 */
		public Set<Desire> getDesires() {
			return desires;
		}

		/**
		 * @return the currently pursued desire, if any
		 */
		public Desire getPursued() {
			return pursued;
		}

		/**
		 * @return a mapping between an intention and the desire it fulfills
		 */
		public Map<Desire, Subgoal> getIntentions() {
			return intentions;
		}

		/**
		 * @return the original plan-parameter
		 */
		public PlanParameter getParam() {
			return param;
		}

	}

}
