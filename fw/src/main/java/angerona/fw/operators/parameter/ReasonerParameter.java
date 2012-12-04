package angerona.fw.operators.parameter;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;

public class ReasonerParameter extends BeliefbasePluginParameter {
	
	private FolFormula query;
	
	public ReasonerParameter() {}
	
	public ReasonerParameter(BaseBeliefbase beliefbase, FolFormula query) {
		super(beliefbase);
		this.query = query;
	}

	public FolFormula getQuery() {
		return query;
	}
	
	
}
