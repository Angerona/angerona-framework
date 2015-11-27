package com.github.kreaturesfw.knowhow.penalty;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreaturesfw.core.basic.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.operators.OperatorCallWrapper;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;
import com.github.kreaturesfw.secrecy.example.operators.ViolatesOperator;
import com.github.kreaturesfw.secrecy.operators.ViolatesResult;

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
	
	private Set<FolFormula> inferedKnowledge;
	
	public RestrictiveSecretsPenalty() {}
	
	public RestrictiveSecretsPenalty(RestrictiveSecretsPenalty rsp) {
		this.agent = rsp.agent;
		this.beliefs = new Beliefs(rsp.beliefs);
		this.inferedKnowledge = new HashSet<>();
		for(FolFormula formula : rsp.inferedKnowledge) {
			inferedKnowledge.add(formula.clone());
		}
	}
	
	@Override
	public void init(Agent agent) {
		this.agent = agent;
		this.beliefs = agent.getBeliefs().clone();
		inferedKnowledge = this.beliefs.getWorldKnowledge().infere();
	}
	
	@Override
	public double penalty(Action action) {
		OperatorCallWrapper violates = agent.getOperators().getOperationSetByType(ViolatesOperator.OPERATION_NAME).getPreferred();
		EvaluateParameter param = new EvaluateParameter(agent, beliefs, action);
		ViolatesResult res = (ViolatesResult)violates.process(param);
		this.beliefs = res.getBeliefs();
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

	@Override
	public boolean validCondition(FolFormula formula) {
		return inferedKnowledge.contains(formula);
	}
}
