package com.github.kreaturesfw.secrecy.example.operators;

import com.github.kreaturesfw.core.basic.Perception;
import com.github.kreaturesfw.core.comm.Inform;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.operators.UpdateBeliefsOperator;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;
import com.github.kreaturesfw.core.util.Utility;

/**
 * This Update Beliefs operator acts more cautious by only updating the view
 * on other agent when receiving an inform speech-act in every other aspect
 * it does the same asthe {@link UpdateBeliefsOperator}.
 * 
 * @author Tim Janus
 */
public class CautiousUpdateBeliefs extends UpdateBeliefsOperator {

	@Override
	protected Beliefs processImpl(EvaluateParameter param) {
		if(param.getAtom() instanceof Inform) {
			Beliefs beliefs = param.getBeliefs();
			Beliefs oldBeliefs = beliefs.clone();
			Inform i = (Inform) param.getAtom();
			BaseBeliefbase bb = null;
			boolean receiver = Utility.equals(i.getReceiverId(), 
					param.getAgent().getName());
			
			// inform speech act only updates views in those scenarios
			String out = "Inform as ";
			if(receiver) {
				bb = beliefs.getViewKnowledge().get(i.getSenderId());
				bb.addKnowledge(i);
				param.report(out + "receiver adapt view on '" + i.getSenderId() + "'", bb);
			}
			
			// inform other components about the update
			if(beliefs.getCopyDepth() == 0) {
				param.getAgent().onUpdateBeliefs((Perception)param.getAtom(), oldBeliefs);
			}
			
			return beliefs;
		} else {
			return super.processImpl(param);
		}
	}
	
}
