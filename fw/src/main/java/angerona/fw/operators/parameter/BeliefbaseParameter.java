package angerona.fw.operators.parameter;

import angerona.fw.BaseBeliefbase;
import angerona.fw.operators.OperatorVisitor;

public class BeliefbaseParameter extends GenericOperatorParameter {
	private BaseBeliefbase beliefbase;
	
	public BeliefbaseParameter(BaseBeliefbase bb, OperatorVisitor ov) {
		super(ov);
		this.beliefbase = bb;
	}
	
	public BaseBeliefbase getBeliefbase() {
		return beliefbase;
	}
}
