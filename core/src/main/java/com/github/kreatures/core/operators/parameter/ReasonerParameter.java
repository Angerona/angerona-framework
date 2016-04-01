package com.github.kreatures.core.operators.parameter;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreatures.core.BaseBeliefbase;

public class ReasonerParameter extends BeliefbasePluginParameter {
	
	private FolFormula query;
	
	public ReasonerParameter() {}
	
	public ReasonerParameter(BaseBeliefbase beliefbase) {
		this(beliefbase, null);
	}
	
	public ReasonerParameter(BaseBeliefbase beliefbase, FolFormula query) {
		super(beliefbase);
		this.query = query;
	}

	public FolFormula getQuery() {
		return query;
	}
	
	
}
