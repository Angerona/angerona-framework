package com.github.angerona.knowhow.graph;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.am.secrecy.operators.ViolatesResult;
import com.github.angerona.fw.example.operators.ViolatesOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

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
