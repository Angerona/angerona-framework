package angerona.fw.operators.parameter;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;

public class ReasonerParameter  {
	private BaseBeliefbase beliefbase;
	
	private FolFormula query;
	
	public ReasonerParameter(BaseBeliefbase beliefbase, FolFormula query) {
		this.beliefbase = beliefbase;
		this.query = query;
	}

	public BaseBeliefbase getBeliefbase() {
		return beliefbase;
	}

	public FolFormula getQuery() {
		return query;
	}
	
	
}
