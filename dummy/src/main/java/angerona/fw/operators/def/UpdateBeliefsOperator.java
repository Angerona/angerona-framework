package angerona.fw.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Justification;
import angerona.fw.comm.Query;
import angerona.fw.logic.Beliefs;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.parameter.UpdateBeliefsParameter;

/**
 * Default Update Beliefs reacts on Answer and Query speech acts.
 * Sub-classes can use the default behavior and add their custom knowledge
 * updates on custom perceptions/actions
 * @author Tim Janus
 */
public class UpdateBeliefsOperator extends BaseUpdateBeliefsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(UpdateBeliefsOperator.class);
	
	@Override
	protected Beliefs processInt(UpdateBeliefsParameter param) {
		LOG.info("Run Default-Update-Beliefs-Operator");
		Beliefs beliefs = param.getBeliefs();
		String id = param.getAgent().getAgentProcess().getName();
		String out = "Update-Beliefs: ";
		
		boolean receiver = false;
		if(param.getPerception() instanceof Action) {
			Action act = (Action)param.getPerception();
			receiver = !id.equals(act.getSenderId());
		} else {
			receiver = true;
		}
		
		
		if(param.getPerception() instanceof Answer) {
			Answer naa = (Answer)param.getPerception();
			out += "Answer ";
			out += receiver ? "as receiver (world)" : "as sender (view)";
			
			BaseBeliefbase bb = null;
			if(receiver) {
				bb = beliefs.getWorldKnowledge();
			} else {
				bb = beliefs.getViewKnowledge().get(naa.getReceiverId());
			}
			
			bb.addKnowledge(naa);
			report(out, bb);
		} else if(param.getPerception() instanceof Query) {
			//Query naq = (Query)param.getPerception();
			out += "Query ";
			out += (!receiver) ? "as sender (normally no changes)" : "as receiver (no change yet)";
			
			report(out, param.getAgent());
		} else if(param.getPerception() instanceof Inform) {
			// When we get informed about something we believe the sender of the Inform
			// himself believes it... but we do not update the own belief base yet.
			Inform i = (Inform) param.getPerception();
			BaseBeliefbase bb = null;
			if(receiver) {
				bb = beliefs.getViewKnowledge().get(i.getSenderId());
				bb.addKnowledge(i);
			}
			
			out = "Inform ";
			out += receiver ? ("as receiver (view->" + i.getSenderId() + ")") : " as sender (no changes)";
			report(out, bb);
		} else if (param.getPerception() instanceof Justification) {
			Justification j = (Justification) param.getPerception();
			BaseBeliefbase bb = null;
			if(receiver) {
				bb = beliefs.getViewKnowledge().get(j.getSenderId());
				bb.addKnowledge(j.getJustifications());
				report("Justification as receiver (1. update view->" + j.getSenderId() + ")", bb);
				bb = beliefs.getWorldKnowledge();
				bb.addKnowledge(j.getJustifications());
				report("Justification as receiver (2. update world-knowledge.)", bb);
			} else {
				bb = beliefs.getViewKnowledge().get(j.getReceiverId());
				bb.addKnowledge(j.getJustifications());
				report("Justification as sender (update view->" + j.getReceiverId() + ")", bb);
			}
		} else {
			report("Update-Operator: Cant handle perception of type: " + param.getPerception().getClass().getName());
		}
		
		return beliefs;
	}
}
