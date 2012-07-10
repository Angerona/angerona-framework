package angerona.fw.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
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
		Beliefs reval = param.getAgent().getBeliefs();
		String id = param.getAgent().getAgentProcess().getName();
		String out = "Update-Beliefs: ";
		
		if(param.getPerception() instanceof Answer) {
			Answer naa = (Answer)param.getPerception();
			out += "Answer ";
			out += (naa.getSenderId().compareTo(id) == 0 ? "as sender (view)" : "as receiver (world)");
			
			BaseBeliefbase bb = null;
			if(id.compareTo(naa.getReceiverId()) == 0) {
				bb = reval.getWorldKnowledge();
			} else if (id.compareTo(naa.getSenderId()) == 0){
				bb = param.getAgent().getBeliefs().getViewKnowledge().get(naa.getReceiverId());
			}
			
			bb.addKnowledge(naa);
			report(out, bb);
		} else if(param.getPerception() instanceof Query) {
			Query naq = (Query)param.getPerception();
			out += "Query ";
			out += (naq.getSenderId().compareTo(id) == 0 ? "as sender (might be an error)" : "as receiver (view)");
			
			report(out, param.getAgent());
		} else {
			LOG.warn("Update-Operator: Cant handle perception of type: " + param.getPerception().getClass().getName());
		}
		
		return reval;
	}
}
