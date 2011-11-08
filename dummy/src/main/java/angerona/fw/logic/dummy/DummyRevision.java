package angerona.fw.logic.dummy;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseRevision;
import angerona.fw.logic.base.BeliefUpdateParameter;

public class DummyRevision extends BaseRevision {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	public BaseBeliefbase process(BeliefUpdateParameter param) {
		return param.getBeliefBase();
	}

}
