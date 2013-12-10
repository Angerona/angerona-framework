package com.github.angerona.fw.example.logic;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.operators.parameter.ChangeBeliefbaseParameter;

public class ExampleExpansion extends BaseChangeBeliefs {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ExampleBeliefbase.class;
	}

	@Override
	protected BaseBeliefbase processImpl(ChangeBeliefbaseParameter param) {
		ExampleBeliefbase bb = (ExampleBeliefbase) param.getSourceBeliefBase();
		ExampleBeliefbase nbb = (ExampleBeliefbase) param.getNewKnowledge();
		bb.fbs.addAll(nbb.fbs);
		
		return bb;
	}
}
