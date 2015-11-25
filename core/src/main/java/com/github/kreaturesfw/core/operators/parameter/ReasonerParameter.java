package com.github.kreaturesfw.core.operators.parameter;

import com.github.kreaturesfw.core.BaseBeliefbase;

import net.sf.tweety.logics.fol.syntax.FolFormula;

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
