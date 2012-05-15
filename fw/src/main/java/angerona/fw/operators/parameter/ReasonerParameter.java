package angerona.fw.operators.parameter;

import net.sf.tweety.Formula;
import angerona.fw.logic.BaseBeliefbase;

public class ReasonerParameter  {
	private BaseBeliefbase beliefbase;
	
	private Formula query;
	
	public ReasonerParameter(BaseBeliefbase beliefbase, Formula query) {
		this.beliefbase = beliefbase;
		this.query = query;
	}

	public BaseBeliefbase getBeliefbase() {
		return beliefbase;
	}

	public Formula getQuery() {
		return query;
	}
	
	
}
