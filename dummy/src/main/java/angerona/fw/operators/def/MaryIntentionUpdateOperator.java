package angerona.fw.operators.def;

import java.util.LinkedList;

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
	private boolean isLie(Intention intention)
	{
		return false;
	}
	private double lyingCost(Intention intention)
	{
		return 0.0;
	}
	private double secrecyWeakeningCost(LinkedList<SecrecyStrengthPair>)
	{
		return 0.0;
	}
	@Override
	protected Intention processInt(IntentionUpdateParameter param) {
		LOG.info("Run Mary-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		LinkedList <Intention> atomicIntentions = new LinkedList<Intention>();
		for(Subgoal plan : param.getPlan().getPlans()) {
			for(int i=0; i<plan.getNumberOfStacks(); ++i) {
				if(plan.peekStack(i).isAtomic()) {
					Intention intention = plan.peekStack(i);
					intention.setRealRun(false);
					if(intentionOutdated(intention)) //Shouldn't this come before a report of performing that mental action?
					{
						continue;
					}
					report("Performing mental-action applying: '"+intention+"'", ag);
					if(isLie(intention))
					{
						//add return value of lyingCost(intention) to intention
						atomicIntentions.add(intention);
					}
					else
					{
						intention.run();
						Skill sk = (Skill)intention;
						//Perhaps the output of sk.weakenings() should be saved rather than accessed twice, but there are dependency issues
						LinkedList<SecrecyStrengthPair> weakenings = sk.weakenings();
						if(weakenings != null) {
							report("Mental action successfull. Now weighing cost of '" + sk.getName() + "' as next atomic action.", ag);
							//System.out.println("(Delete) number of weakenings: "+sk.weakenings().size());
							double cost = secrecyWeakeningCost(weakenings);
							//add cost to intention
							atomicIntentions.add(intention);
							
						}
					}
					
				}
			}
		}
		if(atomicIntentions.size() == 0)
		{
			report("No atomic step candidate found.", ag);
			return null;
		}
		else
		{
			//Choose atomic intention with minimal cost
			return null;
		}
	}
}
