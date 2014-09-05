package com.github.angerona.fw.motivation.island.operators;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.motivation.island.IslandPerception;
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
		} else {
			param.report("Update-Operator: Can't handle perception of type: " + param.getAtom().getClass().getName());
		}

		param.getAgent().onUpdateBeliefs((Perception) param.getAtom(), oldBeliefs);
		return beliefs;
	}

}
