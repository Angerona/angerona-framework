package com.github.kreatures.swarm.beliefbase;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.logic.Beliefs;
import com.github.kreatures.core.operators.BaseUpdateBeliefsOperator;
import com.github.kreatures.core.operators.parameter.EvaluateParameter;
import com.github.kreatures.swarm.basic.SwarmPerception;
/**
 * 
 * @author donfack
 *
 */
public class SwarmUpdateBeliefsOperator extends BaseUpdateBeliefsOperator {
	/*
	 * 
	 * @author Stefan Roetner
	 * 
	 */
	@Override
	protected Beliefs processImpl(EvaluateParameter preprocessedParameters) {
		Beliefs beliefs = preprocessedParameters.getBeliefs();
		Beliefs oldBeliefs = (Beliefs) preprocessedParameters.getBeliefs().clone();
		BaseBeliefbase bb = beliefs.getWorldKnowledge();
		if (preprocessedParameters.getAtom() instanceof SwarmPerception) {
			SwarmPerception p = (SwarmPerception) preprocessedParameters.getAtom();
			bb.addKnowledge(p);
			preprocessedParameters.report("Reveived Perception: " + p.toString(), bb);
			preprocessedParameters.getAgent().onUpdateBeliefs((Perception) preprocessedParameters.getAtom(), oldBeliefs);
		}

		return beliefs;
	}

}
