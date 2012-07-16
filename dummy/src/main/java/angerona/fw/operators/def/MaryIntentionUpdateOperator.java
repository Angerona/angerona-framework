package angerona.fw.operators.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;

public class MaryIntentionUpdateOperator extends BaseIntentionUpdateOperator{
	private static double maxWeakening = 0.5; //Probably should be removed later

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(IntentionUpdateOperator.class);
	
	private boolean intentionOutdated(Intention intention)
	{
		return false;
	}
	
	@Override
	protected Intention processInt(IntentionUpdateParameter param) {
		LOG.info("Run Mary-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		for(Subgoal plan : param.getPlan().getPlans()) {
			for(int i=0; i<plan.getNumberOfStacks(); ++i) {
				if(plan.peekStack(i).isAtomic()) {
					Intention intention = plan.peekStack(i);
					intention.setRealRun(false);
					report("Performing mental-action applying: '"+intention+"'", ag);
					intention.run();
					Skill sk = (Skill)intention;
					if(intentionOutdated(intention))
					{
						continue;
					}
					if(sk.weakenings() != null) {
						report("Mental action successfull, using '" + sk.getName() + "' as next atomic action.", ag);
						System.out.println("(Delete) number of weakenings: "+sk.weakenings().size());
						return intention;
					}
					else
					{
						
					}
					
				}
			}
		}
		report("No atomic step candidate found.", ag);
		return null;
	}
}
