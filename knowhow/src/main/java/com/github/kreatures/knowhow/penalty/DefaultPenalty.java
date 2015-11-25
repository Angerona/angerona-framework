package com.github.kreatures.knowhow.penalty;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.logic.Beliefs;

public class DefaultPenalty implements PenaltyFunction {

	private int iterations = 0;
	
	private Beliefs beliefs;
	
	private Set<FolFormula> inferedKnowledge;
	
	public DefaultPenalty() {}
	
	public DefaultPenalty(DefaultPenalty dp) {
		iterations = dp.iterations;
		beliefs = new Beliefs(dp.beliefs);
		inferedKnowledge = new HashSet<>();
		for(FolFormula formula : inferedKnowledge) {
			dp.inferedKnowledge.add(formula.clone());
		}
	}
	
	@Override
	public void init(Agent agent) {
		iterations = 0;
		beliefs = new Beliefs(agent.getBeliefs());
		inferedKnowledge = beliefs.getWorldKnowledge().infere();
	}

	@Override
	public int iterations() {
		return iterations;
	}

	@Override
	public double penalty(Action action) throws IllegalStateException {
		++iterations;
		return 0;
	}

	@Override
	public DefaultPenalty clone() {
		return new DefaultPenalty(this);
	}

	@Override
	public boolean validCondition(FolFormula formula) {
		return inferedKnowledge.contains(formula);
	}
}
