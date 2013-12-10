package com.github.angerona.fw.defendingagent.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.comm.SpeechAct;
import com.github.angerona.fw.defendingagent.View;
import com.github.angerona.fw.defendingagent.ViewDataComponent;
import com.github.angerona.fw.defendingagent.comm.RevisionAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.BaseUpdateBeliefsOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;

/**
 * Defensive Update Belief operator only handles proactive Answer and RevisionAnswer speech acts
 * send by the defending agent. Whenever the answer is not a rejection, this operator
 * updates the view and belief base according to the revision/request result calculated by the censor
 * component.
 * 
 * @author Sebastian Homann
 */
public class UpdateBeliefsOperator extends BaseUpdateBeliefsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(UpdateBeliefsOperator.class);
	
	@Override
	protected Beliefs processImpl(EvaluateParameter param) {
		LOG.info("Run Defending-Update-Beliefs-Operator");
		Beliefs beliefs = param.getBeliefs();
		Beliefs oldBeliefs = (Beliefs)param.getBeliefs().clone();
		String id = param.getAgent().getName();
		SpeechAct act = (SpeechAct)param.getAtom();
		if(act == null) {
			return beliefs;
		}
		String out = "Received perception: " + act.toString();
		ViewDataComponent views = param.getAgent().getComponent(ViewDataComponent.class);
				
		// ignore incoming actions
		if(!id.equals(act.getSenderId())) {
			param.report(out);
			return beliefs;
		}
		if(act instanceof RevisionAnswer) {
			// answer is a response to a revision request
			RevisionAnswer ans = (RevisionAnswer) act;
			AnswerValue value = ans.getAnswer().getAnswerValue();
			
			if(value == AnswerValue.AV_REJECT) {
				// do nothing
				param.report(out);
			} else {
				// refine view
				View view = views.getView(act.getReceiverId());
				view = view.RefineViewByRevision(ans.getRegarding(), value);
				views.setView(act.getReceiverId(), view);
//				param.report("Refined view on agent '" + act.getReceiverId() + "': " + view.toString());
				
				BaseBeliefbase bb = null;
				if(value == AnswerValue.AV_TRUE) {
					// revision successful, add knowledge to beliefbase
					bb = beliefs.getWorldKnowledge();
					bb.addKnowledge(ans.getRegarding());
					param.report("Add new information '" + ans.getRegarding() + "' to belief base");
					
				}
				param.report("Send RevisionAnswer to revision request: " + act.toString(), bb);
			}
		} else if(act instanceof Answer) {
			// answer must be a response to a query execution request
			Answer ans = (Answer) act;
			AnswerValue value = ans.getAnswer().getAnswerValue();
			// refine view
			if(value != AnswerValue.AV_REJECT) {
				View view = views.getView(act.getReceiverId());
				view = view.RefineViewByQuery(ans.getRegarding(), value);
				views.setView(act.getReceiverId(), view);
//				param.report("Refined view on agent '" + act.getReceiverId() + "': " + view.toString());
			}
			param.report("Send Answer to query: " + act.toString());
		}
		
		
		// Inform agent listeners about update invocation
		if(beliefs.getCopyDepth() == 0) {
			param.getAgent().onUpdateBeliefs((Perception)param.getAtom(), oldBeliefs);
		}
		return beliefs;
	}
}
