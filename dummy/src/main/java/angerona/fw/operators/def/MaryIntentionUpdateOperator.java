package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.logic.SecrecyStrengthPair;
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
		
		if(intention.getHonestyStatus())
		{
			return false;
		}
		return true;
		
		//return false;
	}
	private double lyingCost(Intention intention)
	{
		return 0.5;
	}
	//Which is best for this function? Maximum weakening or sum of weakenings?
	private double secrecyWeakeningCost(List<SecrecyStrengthPair> weakenings)
	{
		double total = 0.0;
		for (SecrecyStrengthPair pair : weakenings)
		{
			total += pair.getDegreeOfWeakening();
		}
		return total;
	}
	
	//A more elegant solution for this function would be to use Collections.max
	//Also note that this function might have to be changed if my definition of INFINITY changes
	//(Have an interface for constants like INFINITY? Or else some other module for final values?)
	private Intention minimalCosting(List<Intention> intentions)
	{
		if (intentions == null || intentions.size() == 0)
		{
			return null;
		}
		Intention minIntent = intentions.get(0);
		double minCost = 20000.0;
		System.out.println("(Delete) number of intentions:"+intentions.size());
		for (Intention intent : intentions)
		{
			double curCost = intent.getCost();
			System.out.println("(Delete) curCost:"+curCost);
			if(curCost < minCost)
			{
				minCost = curCost;
				minIntent = intent;
			}
		}
		System.out.println("(Delete) minCost:"+minCost);
		return minIntent;
	}
	@Override
	protected Intention processInt(IntentionUpdateParameter param) {
		LOG.info("Run Mary-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		List <Intention> atomicIntentions = new LinkedList<Intention>();
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
						double cost = lyingCost(intention);
						intention.setCost(cost);
						System.out.println("(Delete) atomic intention added 1");
						atomicIntentions.add(intention);
					}
					else
					{
						intention.run();
						Skill sk = (Skill)intention;
						//Perhaps the output of sk.weakenings() should be saved rather than accessed twice, but there are dependency issues
						List<SecrecyStrengthPair> weakenings = sk.weakenings();
						if(weakenings != null) {
							report("Mental action successfull. Now weighing cost of '" + sk.getName() + "' as next atomic action.", ag);
							//System.out.println("(Delete) number of weakenings: "+sk.weakenings().size());
							double cost = secrecyWeakeningCost(weakenings);
							intention.setCost(cost);
							System.out.println("(Delete) atomic intention added 2");
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
			return minimalCosting(atomicIntentions);
		}
	}
}
