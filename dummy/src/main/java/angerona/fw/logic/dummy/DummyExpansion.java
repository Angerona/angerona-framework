package angerona.fw.logic.dummy;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.ChangeBeliefbaseParameter;

public class DummyExpansion extends BaseChangeBeliefs {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInternal(ChangeBeliefbaseParameter param) {
		DummyBeliefbase bb = (DummyBeliefbase) param.getSourceBeliefBase();
		DummyBeliefbase nbb = (DummyBeliefbase) param.getNewKnowledge();
		bb.fbs.addAll(nbb.fbs);
		
		return bb;
	}
}
