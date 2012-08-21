package angerona.fw.operators.def;


//import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
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
	protected Intention processInt(IntentionUpdateParameter param) {
		LOG.info("Run Default-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		for(Subgoal plan : param.getPlan().getPlans()) {
			for(int i=0; i<plan.getNumberOfStacks(); ++i) {
				if(plan.peekStack(i).isAtomic()) {
					Intention intention = plan.peekStack(i);
					
					/*
					intention.setRealRun(false);
					report("Performing mental-action applying: '"+intention+"'", ag);
					intention.run();
					Skill sk = (Skill)intention;
					*/
					
					if(intention.isAtomic()) {
						boolean alright = ag.performThought(ag.getBeliefs(), intention).isAlright();
					
						if(alright) {
							report("Mental action successfull, using '" + intention.toString() + "' as next atomic action.", ag);
							return intention;
						}
					}
				}
			}
		}
		report("No atomic step candidate found.", ag);
		return null;
	}

}
