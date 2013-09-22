package com.github.angerona.knowhow.penalty;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.am.secrecy.operators.ViolatesResult;
import com.github.angerona.fw.example.operators.ViolatesOperator;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.OperatorCallWrapper;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

/**
 * Implements a penalty function that gives those actions a penalty of infinity that,
 * reveal a secret. After creation of the penalty function the function assumes that
 * the agent's beliefs does not change because it clones the agent's beliefs at creation
 * time to perform it own operations. That means if penalty is called with several actions
 * each step of thinking (performing an action mentally) is applied on top of the previous
 * changes.
 * 
 * @author Tim Janus
 */
public class RestrictiveSecretsPenalty implements PenaltyFunction {

	private Agent agent;
	
	private Beliefs beliefs;
	
	private int iterations = 0;
	
	public RestrictiveSecretsPenalty() {}
	
	public RestrictiveSecretsPenalty(RestrictiveSecretsPenalty rsp) {
		this.agent = rsp.agent;
		this.beliefs = new Beliefs(rsp.beliefs);
	}
	
	@Override
	public void init(Agent agent) {
		this.agent = agent;
		this.beliefs = agent.getBeliefs().clone();
	}
	
	@Override
	public double penalty(Action action) {
		OperatorCallWrapper violates = agent.getOperators().getOperationSetByType(ViolatesOperator.OPERATION_NAME).getPreferred();
		EvaluateParameter param = new EvaluateParameter(agent, beliefs, action);
		ViolatesResult res = (ViolatesResult)violates.process(param);
		++iterations;
		return res.isAlright() ? 0 : Double.MAX_VALUE;
	}

	@Override
	public int iterations() {
		return iterations;
	}

	@Override
	public RestrictiveSecretsPenalty clone() {
		return new RestrictiveSecretsPenalty(this);
	}
}
