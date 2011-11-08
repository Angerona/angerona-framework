package angerona.fw.logic.dummy;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseExpansion;
import angerona.fw.logic.base.BeliefUpdateParameter;

public class DummyExpansion extends BaseExpansion {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	public BaseBeliefbase process(BeliefUpdateParameter param) {
		if(! (param.getBeliefBase() instanceof DummyBeliefbase))
			throw new ClassCastException("");
		
		DummyBeliefbase bb = (DummyBeliefbase) param.getBeliefBase();
		for(Formula f : param.getNewKnowledge()) {
			if(! (f instanceof FolFormula))
				throw new ClassCastException("Formulas of dummy belief base must be Fol_Formulas");
			bb.fbs.add((FolFormula) f);
		}
		
		return bb;
	}

}
