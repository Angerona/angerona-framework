package angerona.fw.defendingagent.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.defendingagent.View;
import angerona.fw.defendingagent.ViewComponent;
import angerona.fw.defendingagent.comm.RevisionAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.Beliefs;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.parameter.EvaluateParameter;

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
	protected Beliefs processInternal(EvaluateParameter param) {
		LOG.info("Run Defending-Update-Beliefs-Operator");
		Beliefs beliefs = param.getBeliefs();
		Beliefs oldBeliefs = (Beliefs)param.getBeliefs().clone();
		String id = param.getAgent().getName();
		Action act = (Action)param.getAtom();
		if(act == null) {
			return beliefs;
		}
		ViewComponent views = param.getAgent().getComponent(ViewComponent.class);
		String out = "Update-Beliefs: ";
		
		// ignore incoming actions
		if(!id.equals(act.getSenderId())) {
			out += "ignoring incoming perception for now: " + act.toString();
			param.report(out);
			return beliefs;
		}
		if(act instanceof RevisionAnswer) {
			// answer is a response to a revision request
			out += "Sending RevisionAnswer to revision request: ";
			RevisionAnswer ans = (RevisionAnswer) act;
			AnswerValue value = ans.getAnswer().getAnswerValue();
			
			if(value == AnswerValue.AV_REJECT) {
				// do nothing
				out += "reject";
				param.report(out);
			} else {
				// refine view
				View view = views.getView(act.getReceiverId());
				view = view.RefineViewByRevision(ans.getRegarding(), value);
				views.setView(act.getReceiverId(), view);
				
				out += value.toString();
				BaseBeliefbase bb = null;
				if(value == AnswerValue.AV_TRUE) {
					// revision successful, add knowledge to beliefbase
					bb = beliefs.getWorldKnowledge();
					bb.addKnowledge(ans.getRegarding());
				}
				param.report(out, bb);
			}
		} else if(act instanceof Answer) {
			// answer must be a response to a query execution request
			out += "Sending Answer to query: ";
			Answer ans = (Answer) act;
			AnswerValue value = ans.getAnswer().getAnswerValue();
			// refine view
			if(value != AnswerValue.AV_REJECT) {
				View view = views.getView(act.getReceiverId());
				view = view.RefineViewByQuery(ans.getRegarding(), value);
				views.setView(act.getReceiverId(), view);
			}
			out += value.toString();
			param.report(out);
		}
		
		
		// Inform agent listeners about update invocation
		if(beliefs.getCopyDepth() == 0) {
			param.getAgent().onUpdateBeliefs((Perception)param.getAtom(), oldBeliefs);
		}
		return beliefs;
	}
}
