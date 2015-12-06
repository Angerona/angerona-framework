package com.github.kreaturesfw.island.beliefbase;

import com.github.kreaturesfw.core.basic.Perception;
import com.github.kreaturesfw.core.bdi.components.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.operators.BaseUpdateBeliefsOperator;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;
import com.github.kreaturesfw.island.data.IslandPerception;

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
