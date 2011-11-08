package angerona.fw.logic.dummy;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseConsolidation;

public class DummyConsolidation extends BaseConsolidation {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	public BaseBeliefbase process(BaseBeliefbase param) {
		return param;
	}

}
