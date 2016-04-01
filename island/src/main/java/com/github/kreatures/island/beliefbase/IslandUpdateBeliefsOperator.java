package com.github.kreatures.island.beliefbase;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.Perception;
import com.github.kreatures.island.data.IslandPerception;
import com.github.kreatures.core.logic.Beliefs;
import com.github.kreatures.core.operators.BaseUpdateBeliefsOperator;
import com.github.kreatures.core.operators.parameter.EvaluateParameter;

/**
 * 
 * @author Stefan Roetner
 * 
 */
public class IslandUpdateBeliefsOperator extends BaseUpdateBeliefsOperator {

	@Override
	protected Beliefs processImpl(EvaluateParameter param) {
		Beliefs beliefs = param.getBeliefs();
		Beliefs oldBeliefs = (Beliefs) param.getBeliefs().clone();
		BaseBeliefbase bb = beliefs.getWorldKnowledge();
		if (param.getAtom() instanceof IslandPerception) {
			IslandPerception p = (IslandPerception) param.getAtom();
			bb.addKnowledge(p);
			param.report("Reveived Perception: " + p.toString(), bb);
			param.getAgent().onUpdateBeliefs((Perception) param.getAtom(), oldBeliefs);
		}

		return beliefs;
	}

}
