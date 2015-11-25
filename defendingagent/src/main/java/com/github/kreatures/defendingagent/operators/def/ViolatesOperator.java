package com.github.kreatures.defendingagent.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.PlanElement;
import com.github.kreatures.secrecy.operators.BaseViolatesOperator;
import com.github.kreatures.secrecy.operators.ViolatesResult;
import com.github.kreatures.core.logic.Beliefs;
import com.github.kreatures.core.operators.parameter.EvaluateParameter;


public class ViolatesOperator extends BaseViolatesOperator {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViolatesOperator.class);
	
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
