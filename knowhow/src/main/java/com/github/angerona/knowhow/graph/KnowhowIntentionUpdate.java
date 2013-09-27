package com.github.angerona.knowhow.graph;


import com.github.angerona.fw.Action;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.example.operators.IntentionUpdateOperator;

public class KnowhowIntentionUpdate extends IntentionUpdateOperator {
	@Override
	protected PlanElement processInternal(PlanParameter param) {
		PlanElement reval = super.processInternal(param);
		
		// evaluate action parameters:
		if(reval != null) {
			if(reval.getIntention() instanceof ActionAdapter) {
				ActionAdapter aa = (ActionAdapter)reval.getIntention();
				Action realAction = aa.evaluateAction();
				if(realAction != null) {
					reval.setIntention(realAction);
				} else {
					return null;
				}
			}
		}
		
		return reval;
	}
}
