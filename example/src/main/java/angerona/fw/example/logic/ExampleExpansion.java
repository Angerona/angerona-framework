package angerona.fw.example.logic;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.ChangeBeliefbaseParameter;

public class ExampleExpansion extends BaseChangeBeliefs {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ExampleBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processInternal(ChangeBeliefbaseParameter param) {
		ExampleBeliefbase bb = (ExampleBeliefbase) param.getSourceBeliefBase();
		ExampleBeliefbase nbb = (ExampleBeliefbase) param.getNewKnowledge();
		bb.fbs.addAll(nbb.fbs);
		
		return bb;
	}
}
