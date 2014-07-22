package com.github.angerona.fw.defendingagent.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.am.secrecy.operators.BaseViolatesOperator;
import com.github.angerona.fw.am.secrecy.operators.ViolatesResult;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;


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
