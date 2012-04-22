package angerona.fw.operators.parameter;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.logic.BaseBeliefbase;

public class BeliefbaseParameter extends GenericOperatorParameter {
	private BaseBeliefbase beliefbase;
	
	public BeliefbaseParameter(BaseBeliefbase bb, AngeronaEnvironment ev) {
		super(ev);
		this.beliefbase = bb;
	}
	
	public BaseBeliefbase getBeliefbase() {
		return beliefbase;
	}
}
