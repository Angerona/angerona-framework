package com.github.angerona.fw.island.operators;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * I decided to add an ExecuteOperator, that realizes the
 * ActionSelectionFunction from the BDI-Architecture, because apparently it has
 * not been existed yet.
 * 
 * @author Manuel Barbi
 *
 */
public class ExecuteOperator extends Operator<Agent, PlanParameter, Void> {

	public static final String OPERATION_TYPE = "Execute";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, ExecuteOperator.class);
	}

	@Override
	protected PlanParameter getEmptyParameter() {
		return new PlanParameter();
	}

	@Override
	protected Void processImpl(PlanParameter param) {
		for (Subgoal sub : param.getActualPlan().getPlans()) {
			if (sweep(sub, param)) {
				break;
			}
		}

		return null;
	}

	protected boolean sweep(Intention intention, PlanParameter param) {
		if (intention instanceof Action) {
			param.getAgent().performAction((Action) intention);
			return true;
		} else if (intention instanceof Subgoal) {
			Subgoal sub = (Subgoal) intention;

			for (int i = 0; i < sub.getNumberOfStacks(); i++) {
				for (PlanElement elem : sub.getStack(i)) {
					if (sweep(elem.getIntention(), param)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
