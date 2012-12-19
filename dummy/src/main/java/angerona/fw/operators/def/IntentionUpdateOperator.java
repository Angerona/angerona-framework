package angerona.fw.operators.def;


//import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.PlanElement;
import angerona.fw.Subgoal;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.PlanParameter;

/**
 * 	
 * 	@author Tim Janus
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
						boolean select = Boolean.parseBoolean(
								getParameter("allowUnsafe", String.valueOf(false)));
						
						if(!select) {
							select = ag.performThought(ag.getBeliefs(), pe).isAlright();
							if(select) {
								param.report("Mental action successfull, using '" + intention.toString() + "' as next atomic action.");
							}
						}
						
						if(select) {
							return pe;
						}
					}
				}
			}
		}
		param.report("No atomic step candidate found.");
		return null;
	}
}
