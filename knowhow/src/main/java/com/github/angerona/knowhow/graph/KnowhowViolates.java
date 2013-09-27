package com.github.angerona.knowhow.graph;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.am.secrecy.operators.ViolatesResult;
import com.github.angerona.fw.example.operators.ViolatesOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

public class KnowhowViolates extends ViolatesOperator {
	@Override
	protected ViolatesResult onPerception(Perception percept, EvaluateParameter param) {
		if(percept instanceof ActionAdapter) {
			ActionAdapter aa = (ActionAdapter)percept;
			Action a = ((ActionAdapter) percept).evaluateAction(param.getBeliefs());
			param = new EvaluateParameter(param.getAgent(), param.getBeliefs(), a);
			return super.onPerception(a, param);
		}
		return super.onPerception(percept, param);
	}
}
