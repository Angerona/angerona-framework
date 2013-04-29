package angerona.fw.defendingagent.operators.def;


//import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.PlanElement;
import angerona.fw.Subgoal;
import angerona.fw.am.secrecy.operators.BaseIntentionUpdateOperator;
import angerona.fw.am.secrecy.operators.BaseViolatesOperator;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.operators.parameter.EvaluateParameter;

/**
 * 	The default implementation of the IntentionUpdate operation searches the 
 * 	PlanComponent for the first action. The first stack in the collection
 * 	containing an action on top provides the action which is performed.
 * 	Therefore this implementation is order dependent.
 * 
 * 	Simplified version not using the violates operator
 * 
 * 	@author Tim Janus, Sebastian Homann
 */
public class IntentionUpdateOperator extends BaseIntentionUpdateOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(IntentionUpdateOperator.class);
	
	@Override
	protected PlanElement processInternal(PlanParameter param) {
		LOG.info("Run Default-Intention-Update");
		Agent ag = param.getActualPlan().getAgent();
		for(Subgoal plan : param.getActualPlan().getPlans()) {
			for(int i=0; i<plan.getNumberOfStacks(); ++i) {
				PlanElement pe = plan.peekStack(i);
				if(pe.getIntention().isAtomic()) {
					Intention intention = pe.getIntention();
					
					if(intention.isAtomic()) {
						return pe;
					}
				}
			}
		}
		param.report("No atomic step candidate found.");
		return null;
	}
}
