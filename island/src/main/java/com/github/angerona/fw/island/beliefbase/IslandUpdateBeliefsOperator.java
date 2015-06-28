package com.github.angerona.fw.island.beliefbase;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.island.data.IslandPerception;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.BaseUpdateBeliefsOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

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
