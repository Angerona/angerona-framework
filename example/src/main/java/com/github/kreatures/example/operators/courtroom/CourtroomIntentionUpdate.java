package com.github.kreatures.example.operators.courtroom;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.ActionHistory;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.Intention;
import com.github.kreatures.core.PlanElement;
import com.github.kreatures.core.Subgoal;
import com.github.kreatures.secrecy.SecrecyChangeProposal;
import com.github.kreatures.secrecy.components.SecrecyKnowledge;
import com.github.kreatures.secrecy.operators.BaseIntentionUpdateOperator;
import com.github.kreatures.secrecy.operators.BaseViolatesOperator;
import com.github.kreatures.secrecy.operators.ViolatesResult;
import com.github.kreatures.secrecy.operators.parameter.PlanParameter;
import com.github.kreatures.core.comm.Answer;
import com.github.kreatures.core.logic.Beliefs;
import com.github.kreatures.core.operators.OperatorCallWrapper;
import com.github.kreatures.core.operators.parameter.EvaluateParameter;

/**
 * The intention update operator suitable for the "Mary Courtroom Scenario" The
 * previous intention update operator chose the first intention not to violate
 * secrecy This operator considers all options at once and then chooses an
 * optimal one It picks the optimal one by assigning a cost to weakening secrets
 * and to telling lies For further use of the operator, the operator should be
 * made to distinguish between all options available in particular plans. The
 * operator currently chooses from all options from all plans.
 * 
 * @author Daniel Dilger, Tim Janus
 */
public class CourtroomIntentionUpdate extends BaseIntentionUpdateOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(CourtroomIntentionUpdate.class);

	/** 
	 * Checks if the given plan element represents a lie. It assumes that the 
	 * user data is given as boolean which indicates if it is a lie (true) or
	 * if it the truth (false).
	 * @param pe	The plan element
	 * @return		true if the plan element represents a lie, false otherwise.
	 */
	private boolean isLie(PlanElement pe) {
		if(pe.getUserData() instanceof Boolean) {
			return (Boolean)pe.getUserData();
		}
		return false;
	}

	
	private double lyingCost(Intention intention, PlanParameter pp) {
		double estimate = 0.5;
		pp.report(intention
				+ " <b> 'dontKnow' is a lie </b>. Estimated cost equal to weakening secret by "
				+ estimate);
		return estimate;
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
	protected PlanElement processImpl(PlanParameter param) {
		LOG.info("Run Mary-Intention-Update");
		Agent ag = param.getActualPlan().getAgent();
		List<PlanElement> atomicIntentions = new LinkedList<PlanElement>();
		// Loop needs to be changed so that only options from the same plan are
		// considered
		OperatorCallWrapper vop = ag.getOperators().getPreferedByType(BaseViolatesOperator.OPERATION_NAME);
		for (Subgoal plan : param.getActualPlan().getPlans()) {
			for (int i = 0; i < plan.getNumberOfStacks(); ++i) {
				
				PlanElement pe = plan.peekStack(i);
				if (pe.isAtomic()) {
					
					// do not self repeat:
					ActionHistory history = param.getAgent().getBeliefs().getComponentOrSub(ActionHistory.class);
					if(history != null && pe.getIntention() instanceof Answer) {
						if(history.didAction((Answer)pe.getIntention())) {
							continue;
						}
					}
					
					if (isLie(pe)) {
						// add return value of lyingCost(intention) to intention
						double cost = lyingCost(pe.getIntention(), param);
						pe.setCosts(cost + pe.getCosts());
						atomicIntentions.add(pe);
					} else {
						EvaluateParameter eparam = new EvaluateParameter(ag, ag.getBeliefs(), pe);
						ViolatesResult vRes = ((ViolatesResult)vop.process(eparam));
						Beliefs newBeliefs = vRes.getBeliefs();
						
						if(newBeliefs != null) {
							SecrecyKnowledge sk = ag.getComponent(SecrecyKnowledge.class);
							SecrecyChangeProposal scp = sk.processNeededChanges(newBeliefs, ag.getBeliefs());
							
							// todo distribute belief operator families to secrets....
							double costs = scp.distance(ag.getBeliefs().getWorldKnowledge().getBeliefOperatorFamily());
							
							param.report(String.format("Cost for Action: '%s' is '%f'", pe.toString(), costs));
							pe.setCosts(costs);
							atomicIntentions.add(pe);
						}
					}
				}
			}
		}
		if (atomicIntentions.size() == 0) {
			param.report("No atomic step candidate found.");
			return null;
		} else {
			PlanElement min = minimalCosting(atomicIntentions);
			return min;
		}
	}
}
