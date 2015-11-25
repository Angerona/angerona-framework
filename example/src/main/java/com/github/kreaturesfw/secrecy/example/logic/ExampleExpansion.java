package com.github.kreaturesfw.secrecy.example.logic;

import com.github.kreaturesfw.core.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.operators.parameter.ChangeBeliefbaseParameter;

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
