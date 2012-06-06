package angerona.fw.operators.def;

import javax.swing.JOptionPane;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Intention;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.parameter.IntentionUpdateParameter;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;

/**
 * 
 * @author Tim Janus
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
					intention.setRealRun(false);
					report("Performing mental-action applying: '"+intention+"'", ag);
					intention.run();
					Skill sk = (Skill)intention;
					if(!sk.violates()) {
						report("Mental action successfull, using '" + sk.getName() + "' as next atomic action.", ag);
						return intention;
					}
					/*else
					{
						JOptionPane.showMessageDialog(null, "Made it here");
						//NOTE: this is a poor solution since there could actions other than answering queries
						 //which violate confidentiality
						//Lying happens here
						//Adds a new intention for a false answer
						//Should it go in its own module, or within the reasoner?
						//But if it goes in the reasoner then there isn't a catch for when confidentiality is violated
						//Could lying even be its own skill?
						Query query = (Query) (ag.getActualPerception());
						AngeronaAnswer ans = ag.getBeliefs().getWorldKnowledge().reason((FolFormula)query.getQuestion());
						AnswerValue truth = ans.getAnswerExtended();
						AnswerValue lie;
						if(truth==AnswerValue.AV_TRUE)
						{
							lie = AnswerValue.AV_FALSE;
						}
						else if(truth==AnswerValue.AV_FALSE)
						{
							lie = AnswerValue.AV_TRUE;
						}
						else
						{
							continue;//Don't bother lying
						}
						if (lie!=null)
						{
							//To lie, add a new intention to the plan
							Context context = ContextFactory.createContext(
									ag.getActualPerception());
							context.set("answer", lie);
					
							Subgoal sg = new Subgoal(ag, plan.getFulfillsDesire());
							sg.newStack(sk, context);
							ag.getPlanComponent().addPlan(sg);
							report("Add the new atomic action '"+sk.getName()+"' to the plan", ag.getPlanComponent());
						}
						
					} */
				}
			}
		}
		report("No atomic step candidate found.", ag);
		return null;
	}

}
