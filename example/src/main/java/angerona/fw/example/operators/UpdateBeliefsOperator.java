package angerona.fw.example.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
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
 * updates on custom perceptions/actions.
 * 
 * @author Tim Janus
 */
public class UpdateBeliefsOperator extends BaseUpdateBeliefsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(UpdateBeliefsOperator.class);
	
	@Override
	protected Beliefs processInternal(EvaluateParameter param) {
		LOG.info("Run Default-Update-Beliefs-Operator");
		
		Beliefs beliefs = param.getBeliefs();
		Beliefs oldBeliefs = (Beliefs)param.getBeliefs().clone();
		String id = param.getAgent().getName();
		String out = "Update-Beliefs: ";
		
		boolean update = true;
		
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
			Inform i = (Inform) param.getAtom();
			BaseBeliefbase bb = null;
			
			out = "Inform as ";
			if(receiver) {
				bb = beliefs.getWorldKnowledge();
				bb.addKnowledge(i);
				param.report(out + "receiver adapt world knowledge", bb);
				
				bb = beliefs.getViewKnowledge().get(i.getSenderId());
				bb.addKnowledge(i);
				param.report(out + "receiver adapt view on '" + i.getSenderId() + "'", bb);
			} else {
				bb = beliefs.getViewKnowledge().get(i.getReceiverId());
				bb.addKnowledge(i);
				param.report(out + "sender adapt view on '" + i.getReceiverId() + "'", bb);
			}
			
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
		} else {
			param.report("No Speech-Act for updating the Beliefs");
			update = false;
		}
			
		if(update && beliefs.getCopyDepth() == 0) {
			param.getAgent().onUpdateBeliefs((Perception)param.getAtom(), oldBeliefs);
		}
		return beliefs;
	}
}
