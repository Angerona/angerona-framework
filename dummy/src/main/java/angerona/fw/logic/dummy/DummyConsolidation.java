package angerona.fw.logic.dummy;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseConsolidation;
import angerona.fw.operators.parameter.BeliefbaseParameter;

public class DummyConsolidation extends BaseConsolidation {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInt(BeliefbaseParameter param) {
		return param.getBeliefbase();
	}

}
