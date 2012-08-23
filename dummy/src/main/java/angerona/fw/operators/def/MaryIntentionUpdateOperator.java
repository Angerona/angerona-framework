package angerona.fw.operators.def;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
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

	private boolean isLie(Intention intention) {

		if (intention.getHonestyStatus()) {
			return false;
		}
		return true;
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
	private Intention minimalCosting(List<Intention> intentions) {
		if (intentions == null || intentions.size() == 0) {
			return null;
		}
		Intention minIntent = intentions.get(0);
		double minCost = minIntent.getCost();
		for (Intention intent : intentions) {
			double curCost = intent.getCost();
			if (curCost < minCost) {
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
		List<Intention> atomicIntentions = new LinkedList<Intention>();
		// Loop needs to be changed so that only options from the same plan are
		// considered
		for (Subgoal plan : param.getPlan().getPlans()) {
			for (int i = 0; i < plan.getNumberOfStacks(); ++i) {
				if (plan.peekStack(i).isAtomic()) {
					Intention intention = plan.peekStack(i);
					intention.setRealRun(false);

					if (isLie(intention)) {
						// add return value of lyingCost(intention) to intention
						double cost = lyingCost(intention);
						intention.setCost(cost);
						atomicIntentions.add(intention);
					} else {
						List<Pair<Secret, Double>> weakenings = ag.performThought(ag.getBeliefs(), intention).getPairs();
						
						if (weakenings != null) {
							double cost = secrecyWeakeningCost(weakenings);
							intention.setCost(cost);
							atomicIntentions.add(intention);

						}
					}

				}
			}
		}
		if (atomicIntentions.size() == 0) {
			report("No atomic step candidate found.");
			return null;
		} else {
			Intention min = minimalCosting(atomicIntentions);
			return min;
		}
	}
}
