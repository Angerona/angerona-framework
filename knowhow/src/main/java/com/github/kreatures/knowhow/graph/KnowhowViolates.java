package com.github.kreatures.knowhow.graph;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Perception;
import com.github.kreatures.secrecy.operators.ViolatesResult;
import com.github.kreatures.example.operators.ViolatesOperator;
import com.github.kreatures.core.operators.parameter.EvaluateParameter;

/**
 *  An Violates operator implementation that does the same as
 * {@link ViolatesOperator} but handles the {@link ActionAdapter}
 * perceptions correctly.
 * 
 * @author Tim Janus
 */
public class KnowhowViolates extends ViolatesOperator {
	@Override
	protected ViolatesResult onPerception(Perception percept, EvaluateParameter param) {
		if(percept instanceof ActionAdapter) {
			ActionAdapter aa = (ActionAdapter)percept;
			Action a = aa.evaluateAction(param.getBeliefs());
			if(a instanceof Perception) {
				param = new EvaluateParameter(param.getAgent(), param.getBeliefs(), a);
				return super.onPerception((Perception)a, param);
			}
			return new ViolatesResult();
		}
		return super.onPerception(percept, param);
	}
}
