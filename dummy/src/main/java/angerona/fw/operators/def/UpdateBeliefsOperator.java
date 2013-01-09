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
import angerona.fw.operators.parameter.EvaluateParameter;

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
	protected Beliefs processInternal(EvaluateParameter param) {
		LOG.info("Run Default-Update-Beliefs-Operator");
		Beliefs beliefs = param.getBeliefs();
		String id = param.getAgent().getAgentProcess().getName();
		String out = "Update-Beliefs: ";
		
		boolean receiver = false;
		if(param.getAtom() instanceof Action) {
			Action act = (Action)param.getAtom();
			receiver = !id.equals(act.getSenderId());
		} else {
			receiver = true;
		}
		
		
		if(param.getAtom() instanceof Answer) {
			Answer naa = (Answer)param.getAtom();
			out += "Answer ";
			out += receiver ? "as receiver (world)" : "as sender (view)";
			
			BaseBeliefbase bb = null;
			if(receiver) {
				bb = beliefs.getWorldKnowledge();
			} else {
				bb = beliefs.getViewKnowledge().get(naa.getReceiverId());
			}
			
			bb.addKnowledge(naa);
			param.report(out, bb);
		} else if(param.getAtom() instanceof Query) {
			out += "Query ";
			out += (!receiver) ? "as sender (no changes)" : "as receiver (no changes)";
			
			param.report(out);
		} else if(param.getAtom() instanceof Inform) {
			// When we get informed about something we believe the sender of the Inform
			// himself believes it... but we do not update the own belief base yet.
			Inform i = (Inform) param.getAtom();
			BaseBeliefbase bb = null;
			
			out = "Inform ";
			// only allow literals with prefix "ask_":
			if(	i.getSentences().size() == 1 && 
				i.getSentences().iterator().next().toString().startsWith("ask_")) {
				if(receiver)
					bb = beliefs.getWorldKnowledge();
				else
					bb = beliefs.getViewKnowledge().get(i.getReceiverId());
				bb.addKnowledge(i);
				out += (!receiver) ? "as sender (add '" + i.getSentences().iterator().next().toString() + "' literal to view)" : 
					"as receiver (add '" + i.getSentences().iterator().next().toString() + "' literal to world)";
			} else if (receiver) {
				bb = beliefs.getViewKnowledge().get(i.getSenderId());
				bb.addKnowledge(i);
				out += receiver ? ("as receiver (view->" + i.getSenderId() + ")") : " as sender (no changes)";
			}
			
			param.report(out, bb == null ? null : bb);
		} else if (param.getAtom() instanceof Justification) {
			Justification j = (Justification) param.getAtom();
			BaseBeliefbase bb = null;
			if(receiver) {
				bb = beliefs.getViewKnowledge().get(j.getSenderId());
				bb.addKnowledge(j.getJustifications());
				param.report("Justification as receiver (1. update view->" + j.getSenderId() + ")", bb);
				bb = beliefs.getWorldKnowledge();
				bb.addKnowledge(j.getJustifications());
				param.report("Justification as receiver (2. update world-knowledge.)", bb);
			} else {
				bb = beliefs.getViewKnowledge().get(j.getReceiverId());
				bb.addKnowledge(j.getJustifications());
				param.report("Justification as sender (update view->" + j.getReceiverId() + ")", bb);
			}
		} else if(param.getAtom() != null){
			param.report("Update-Operator: Cant handle perception of type: " + param.getAtom().getClass().getName());
		}
		
		return beliefs;
	}
}
