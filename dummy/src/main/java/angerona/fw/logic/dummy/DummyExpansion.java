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
		if(! (param.getBeliefBase() instanceof DummyBeliefbase))
			throw new ClassCastException("");
		
		DummyBeliefbase bb = (DummyBeliefbase) param.getBeliefBase();
		throw new RuntimeException("DummyExpansion does not support Translate yet");
		/*
		for(Formula f : param.getNewKnowledge()) {
			if(! (f instanceof FolFormula))
				throw new ClassCastException("Formulas of dummy belief base must be Fol_Formulas");
			bb.fbs.add((FolFormula) f);
		}
		
		
		return bb;*/
	}

}
