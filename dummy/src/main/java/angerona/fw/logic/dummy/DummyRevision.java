package angerona.fw.logic.dummy;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

public class DummyRevision extends BaseChangeBeliefs {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		return param.getBeliefBase();
	}

}
