package com.github.angerona.fw.logic.asp;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.DLPAtom;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.UpdateBeliefsOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

/**
 * This class implements the AUX program described by Krümpelmann in MATES13 by successive adding the rules for 
 * atoms to the different belief bases, therefore it uses a map that maps belief bases to their set of atoms that 
 * are handled by the AUX sub-program.
 * 
 * This operator handles the 'real' information of the speech-acts as {@link UpdateBeliefsOperator}.
 * 
 * @author Tim Janus
 */
public class MatesUpdateBeliefs extends UpdateBeliefsOperator {

	/** todo move scope to allow multi-threading more easily */
	private static Map<AspBeliefbase, Set<DLPAtom>> findGoodName = new HashMap<>();
	
	@Override
	protected Beliefs processInternal(EvaluateParameter param) {		
		Beliefs reval = super.processInternal(param);
		
		if(reval.getWorldKnowledge() instanceof AspBeliefbase) {
			synchonizeAux((AspBeliefbase)reval.getWorldKnowledge());
		}
		
		for(BaseBeliefbase view : reval.getViewKnowledge().values()) {
			if(view instanceof AspBeliefbase) {
				synchonizeAux((AspBeliefbase)view);
			}
		}
		
		return reval;
	}
	
	private void synchonizeAux(AspBeliefbase beliefBase) {
		// todo implement
	}
}
