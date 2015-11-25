package com.github.kreaturesfw.knowhow.graph;

import com.github.kreaturesfw.core.Action;
import com.github.kreaturesfw.core.Perception;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;
import com.github.kreaturesfw.secrecy.example.operators.ViolatesOperator;
import com.github.kreaturesfw.secrecy.operators.ViolatesResult;

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
