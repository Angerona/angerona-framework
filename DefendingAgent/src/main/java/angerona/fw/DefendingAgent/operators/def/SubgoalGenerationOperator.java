package angerona.fw.DefendingAgent.operators.def;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.Subgoal;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.Desires;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
 * Default subgoal generation generates the atomic actions need to react on the
 * different speech acts. Subclasses can use the default behavior to react to speech
 * acts.
 * Also implements specialized methods for the simple version of the strike-committee-example.
 * @author Tim Janus
 */
public class SubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	@Override
	protected Boolean processInternal(PlanParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		Desires des = ag.getDesires();
		
		if(des != null) {
			Set<Desire> currentDesires;
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareQueryProcessing);
			for(Desire d : currentDesires) {
				processQuery(d, pp, ag);
			}
			
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareRevisionProcessing);
			for(Desire d : currentDesires) {
				processRevision(d, pp, ag);
			}
			
		}
		return true;
	}
	
	// TODO: javadoc
	public void processQuery(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();
		Query query = (Query) desire.getPerception();
		boolean result = cexec.processQuery(ag, query);
		
		AnswerValue answerValue = AnswerValue.AV_REJECT;
		
		if(result) {
			// green light from the censor, evaluate query
			AngeronaAnswer answer = ag.getBeliefs().getWorldKnowledge().reason(query.getQuestion());
			answerValue = answer.getAnswerValue();
		}
		
		Answer answer = new Answer(ag,query.getSenderId(), query.getQuestion(), answerValue);
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
	}
	
	public void processRevision(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();
		Revision revision = (Revision) desire.getPerception();
		boolean result = cexec.processRevision(ag, revision);
		
		// TODO: finish - use censor methods
		
//		Action action = cexec.processRevision(ag, (Revision) desire.getPerception());
		 Subgoal answer = new Subgoal(ag, desire);
			
//		answer.newStack(action);
			
		ag.getPlanComponent().addPlan(answer);
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
					"' to the plan", ag.getPlanComponent());
	}

}
