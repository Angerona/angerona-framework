package angerona.fw.operators.def;


//import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.PlanElement;
import angerona.fw.Subgoal;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;

/**
 * 	
 * 	@author Tim Janus
 */
public class IntentionUpdateOperator extends BaseIntentionUpdateOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(IntentionUpdateOperator.class);
	
	@Override
	protected PlanElement processInt(IntentionUpdateParameter param) {
		LOG.info("Run Default-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		for(Subgoal plan : param.getPlan().getPlans()) {
			for(int i=0; i<plan.getNumberOfStacks(); ++i) {
				PlanElement pe = plan.peekStack(i);
				if(pe.getIntention().isAtomic()) {
					Intention intention = pe.getIntention();
					
					if(intention.isAtomic()) {
						boolean alright = ag.performThought(ag.getBeliefs(), pe).isAlright();
					
						if(alright) {
							report("Mental action successfull, using '" + intention.toString() + "' as next atomic action.", ag);
							return pe;
						}
					}
				}
			}
		}
		report("No atomic step candidate found.", ag);
		return null;
	}

}
