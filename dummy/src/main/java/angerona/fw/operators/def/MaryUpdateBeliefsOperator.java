package angerona.fw.operators.def;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.logic.Secret;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.parameter.UpdateBeliefsParameter;
/**
 * Update Beliefs Operator which enables the weakening of secrets.
 * @author dilger
 *
 */
//Not sure about all the details of how the update beliefs operator works...
public class MaryUpdateBeliefsOperator extends BaseUpdateBeliefsOperator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(UpdateBeliefsOperator.class);
	@Override
	protected Beliefs processInt(UpdateBeliefsParameter param) {
		LOG.info("Run Mary-Update-Beliefs-Operator");
		ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
		
		Beliefs reval = param.getAgent().getBeliefs();
		String id = param.getAgent().getAgentProcess().getName();
		String out = "Update-Beliefs: ";
		if(param.getPerception() instanceof Answer) {
			Answer naa = (Answer)param.getPerception();
			out += "Answer ";
			out += (naa.getSenderId().compareTo(id) == 0 ? "as sender (view)" : "as receiver (world)");
			
			
			List<SecrecyStrengthPair> weakenings = param.getAgent().getWeakenings();
			
			
			BaseBeliefbase bb = null;
			if(id.compareTo(naa.getReceiverId()) == 0) {
				bb = reval.getWorldKnowledge();
			} else if (id.compareTo(naa.getSenderId()) == 0){
				bb = param.getAgent().getBeliefs().getViewKnowledge().get(naa.getReceiverId());
			}
			
			bb.addKnowledge(naa);

			for(Secret secret : conf.getTargets()) 
			{

				if(weakenings == null)
				{
					continue;
				}
					
				for (SecrecyStrengthPair sPair : weakenings)
				{
					
					if(secret.getInformation().toString().equals(sPair.getSecret().getInformation().toString()))
					{
						Map <String, String> map =  secret.getReasonerParameters();
						double oldD = Double.parseDouble(map.get("d"));
						double newD = oldD - sPair.getDegreeOfWeakening();
						map.put("d", new Double(newD).toString());
						secret.setReasonerParameters(map); 
					}
				}
			}
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
