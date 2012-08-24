package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.PlanElement;
import angerona.fw.Subgoal;
import angerona.fw.logic.Secret;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;
import angerona.fw.util.Pair;

/**
 * The intention update operator suitable for the "Mary Courtroom Scenario" The
 * previous intention update operator chose the first intention not to violate
 * secrecy This operator considers all options at once and then chooses an
 * optimal one It picks the optimal one by assigning a cost to weakening secrets
 * and to telling lies For further use of the operator, the operator should be
 * made to distinguish between all options available in particular plans. The
 * operator currently chooses from all options from all plans.
 * 
 * @author Daniel Dilger
 */
public class MaryIntentionUpdateOperator extends BaseIntentionUpdateOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(IntentionUpdateOperator.class);

	private boolean isLie(PlanElement pe) {
		if(pe.getUserData() instanceof Boolean) {
			return (Boolean)pe.getUserData();
		}
		return false;
	}

	private double lyingCost(Intention intention) {
		double estimate = 0.5;
		report(intention
				+ " <b> 'dontKnow' is a lie </b>. Estimated cost equal to weakening secret by "
				+ estimate);
		return estimate;
	}

	private double secrecyWeakeningCost(List<Pair<Secret, Double>> weakenings) {
		double total = 0.0;
		for (Pair<Secret, Double> pair : weakenings) {
			total = total + pair.second;
		}
		return total;
	}

	/**
	 * A more elegant solution would be to use Collections.max
	 */
	private PlanElement minimalCosting(List<PlanElement> intentions) {
		if (intentions == null || intentions.size() == 0) {
			return null;
		}
		PlanElement minIntent = null;
		double minCost = Double.MAX_VALUE;
		
		for (PlanElement intent : intentions) {
			double curCost = intent.getCosts();
			if (curCost < minCost) {
				minCost = curCost;
				minIntent = intent;
			}
		}
		return minIntent;
	}

	@Override
	protected PlanElement processInt(IntentionUpdateParameter param) {
		LOG.info("Run Mary-Intention-Update");
		Agent ag = param.getPlan().getAgent();
		List<PlanElement> atomicIntentions = new LinkedList<PlanElement>();
		// Loop needs to be changed so that only options from the same plan are
		// considered
		for (Subgoal plan : param.getPlan().getPlans()) {
			for (int i = 0; i < plan.getNumberOfStacks(); ++i) {
				PlanElement pe = plan.peekStack(i);
				if (pe.isAtomic()) {
					
					if (isLie(pe)) {
						// add return value of lyingCost(intention) to intention
						double cost = lyingCost(pe.getIntention());
						pe.setCosts(cost + pe.getCosts());
						atomicIntentions.add(pe);
					} else {
						List<Pair<Secret, Double>> weakenings = 
								ag.performThought(ag.getBeliefs(), pe).getPairs();
						
						if (weakenings != null) {
							double cost = secrecyWeakeningCost(weakenings);
							pe.setCosts(cost);
							atomicIntentions.add(pe);
						}
					}

				}
			}
		}
		if (atomicIntentions.size() == 0) {
			report("No atomic step candidate found.");
			return null;
		} else {
			PlanElement min = minimalCosting(atomicIntentions);
			return min;
		}
	}
}
