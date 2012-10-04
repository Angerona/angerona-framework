package angerona.fw.logic.dummy;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

public class DummyExpansion extends BaseChangeBeliefs {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		DummyBeliefbase bb = (DummyBeliefbase) param.getBeliefBase();
		DummyBeliefbase nbb = (DummyBeliefbase) param.getNewKnowledge();
		bb.fbs.addAll(nbb.fbs);
		
		return bb;
	}

}
