package com.github.kreaturesfw.defendingagent.operators.def;

import com.github.kreaturesfw.core.legacy.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.Perception;
import com.github.kreaturesfw.core.legacy.PlanElement;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;
import com.github.kreaturesfw.secrecy.operators.BaseViolatesOperator;
import com.github.kreaturesfw.secrecy.operators.ViolatesResult;


public class ViolatesOperator extends BaseViolatesOperator {
	
	@Override
	protected ViolatesResult onAction(Action action, EvaluateParameter param) {
		return new ViolatesResult();
	}
	
	@Override
	protected ViolatesResult onPerception(Perception percept, EvaluateParameter param) {
		return new ViolatesResult();
	}

	public void addMetaInformation(Beliefs newBeliefs, EvaluateParameter param) {
	}
	
	
	@Override
	protected ViolatesResult onPlan(PlanElement pe, EvaluateParameter param) {
		return new ViolatesResult();
	}
	
	@Override
	public void performAction(Action action, Agent agent, Beliefs beliefs) {
	}

	@Override
	protected ViolatesResult onCheck(Agent agent, Beliefs beliefs) {
		return new ViolatesResult();
	}
}
