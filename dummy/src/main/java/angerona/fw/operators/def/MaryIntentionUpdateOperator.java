package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.logic.SecrecyStrengthPair;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;

public class MaryIntentionUpdateOperator extends BaseIntentionUpdateOperator{
	

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(IntentionUpdateOperator.class);
	
	private boolean intentionOutdated(Intention intention)
	{
		return false;
	}
	private boolean isLie(Intention intention)
	{
		
		if(intention.getHonestyStatus())
		{
			return false;
		}
		return true;
	}
	private double lyingCost(Intention intention)
	{
		double estimate = 0.5;
		report(intention+" <b> 'dontKnow' is a lie </b>. Estimated cost equal to weakening secret by "+estimate);
		return estimate;
	}
	private double secrecyWeakeningCost(List<SecrecyStrengthPair> weakenings)
	{
		double total = 0.0;
		for (SecrecyStrengthPair pair : weakenings)
		{
			total = total + pair.getDegreeOfWeakening();
		}
		return total;
	}
	
	/**
	 * A more elegant solution would be to use Collections.max
	 */
	private Intention minimalCosting(List<Intention> intentions)
	{
		if (intentions == null || intentions.size() == 0)
		{
			return null;
		}
		Intention minIntent = intentions.get(0);
		double minCost = minIntent.getCost();
		for (Intention intent : intentions)
		{
			double curCost = intent.getCost();
			if(curCost < minCost)
			{
				minCost = curCost;
				minIntent = intent;
			}
		}
		return minIntent;
	}
	@Override
	protected Intention processInt(IntentionUpdateParameter param) {
		LOG.info("Run Mary-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		List <Intention> atomicIntentions = new LinkedList<Intention>();
		//Loop needs to be changed so that only options from the same plan are considered
		for(Subgoal plan : param.getPlan().getPlans()) {
			for(int i=0; i<plan.getNumberOfStacks(); ++i) {
				if(plan.peekStack(i).isAtomic()) {
					Intention intention = plan.peekStack(i);
					intention.setRealRun(false);
					if(intentionOutdated(intention)) 
					{
						continue;
					}
					report("Performing mental-action applying: '"+intention+"'", ag);
					if(isLie(intention))
					{
						//add return value of lyingCost(intention) to intention
						double cost = lyingCost(intention);
						intention.setCost(cost);
						atomicIntentions.add(intention);
					}
					else
					{
						intention.run();
						Skill sk = (Skill)intention;
						
						List<SecrecyStrengthPair> weakenings = sk.getWeakenings();
						if(weakenings != null) {
							double cost = secrecyWeakeningCost(weakenings);
							intention.setCost(cost);
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
			Intention min = minimalCosting(atomicIntentions);
			ag.setWeakenings(((Skill) min).getWeakenings());
			return min;
		}
	}
}
