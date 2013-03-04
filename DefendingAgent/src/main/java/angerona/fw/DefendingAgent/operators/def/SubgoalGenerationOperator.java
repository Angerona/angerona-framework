package angerona.fw.DefendingAgent.operators.def;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.Subgoal;
import angerona.fw.DefendingAgent.ViewComponent;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.Desires;
import angerona.fw.logic.SecrecyKnowledge;
import angerona.fw.logic.Secret;

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
	/**
	 * 
	 * @param desire
	 * @param pp
	 * @param ag
	 * @see [1] Biskup, Joachim and Tadros, Cornelia. Revising Belief without Revealing Secrets
	 */
	public void processQuery(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();
		Query query = (Query) desire.getPerception();
		SecrecyKnowledge conf = ag.getComponent(SecrecyKnowledge.class);
		
		for(Secret a : conf.getTargets()){
			//ans := true
			if(cexec.poss(ag.getComponent(ViewComponent.class).getView(ag.toString())
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_TRUE)) && 
					cexec.skepticalInference(ag.getComponent(ViewComponent.class).getView(ag.toString())
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_TRUE), a.getInformation())){
				Answer answer = new Answer(ag,query.getSenderId(), query.getQuestion(), AnswerValue.AV_REJECT);
				Subgoal answerGoal = new Subgoal(ag, desire);
				answerGoal.newStack(answer);
				ag.getPlanComponent().addPlan(answerGoal);
				pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
						"' to the plan", ag.getPlanComponent());
				return;
			}
			//ans := false
			if(cexec.poss(ag.getComponent(ViewComponent.class).getView(ag.toString())
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_FALSE)) && 
					cexec.skepticalInference(ag.getComponent(ViewComponent.class).getView(ag.toString())
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_FALSE), a.getInformation())){
				Answer answer = new Answer(ag,query.getSenderId(), query.getQuestion(), AnswerValue.AV_REJECT);
				Subgoal answerGoal = new Subgoal(ag, desire);
				answerGoal.newStack(answer);
				ag.getPlanComponent().addPlan(answerGoal);
				pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
						"' to the plan", ag.getPlanComponent());
				return;
			}
			//ans := undef
			if(cexec.poss(ag.getComponent(ViewComponent.class).getView(ag.toString())
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_UNKNOWN)) && 
					cexec.skepticalInference(ag.getComponent(ViewComponent.class).getView(ag.toString())
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_UNKNOWN), a.getInformation())){
				Answer answer = new Answer(ag,query.getSenderId(), query.getQuestion(), AnswerValue.AV_REJECT);
				Subgoal answerGoal = new Subgoal(ag, desire);
				answerGoal.newStack(answer);
				ag.getPlanComponent().addPlan(answerGoal);
				pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
						"' to the plan", ag.getPlanComponent());
				return;
			}
		}
		
		AnswerValue answerValue;
		if(cexec.skepticalInference(ag.getComponent(ViewComponent.class).getView(ag.toString()), query.getQuestion())){
			answerValue = AnswerValue.AV_TRUE;
		}else{
			answerValue = AnswerValue.AV_FALSE;
		}
		
		ag.getComponent(ViewComponent.class).setView(ag.toString(), ag.getComponent(ViewComponent.class).getView(ag.toString()).RefineViewByQuery(query.getQuestion(), answerValue));
		
		Answer answer = new Answer(ag,query.getSenderId(), query.getQuestion(), answerValue);
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
	}
	
	// TODO: javadoc
	/**
	 * 
	 * @param desire
	 * @param pp
	 * @param ag
	 * @see [1] Biskup, Joachim and Tadros, Cornelia. Revising Belief without Revealing Secrets
	 */
	public void processRevision(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();
		Revision revision = (Revision) desire.getPerception();
		SecrecyKnowledge conf = ag.getComponent(SecrecyKnowledge.class);
		
		for(Secret a : conf.getTargets()){
		if(cexec.skepticalInference(ag.getComponent(ViewComponent.class).getView(ag.toString())
				.RefineViewByRevision(revision.getProposition(), AnswerValue.AV_TRUE), a.getInformation())){
					Answer answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), AnswerValue.AV_REJECT);
					Subgoal answerGoal = new Subgoal(ag, desire);
					answerGoal.newStack(answer);
					ag.getPlanComponent().addPlan(answerGoal);
					pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
							"' to the plan", ag.getPlanComponent());
					return;
				}
		}
		if(cexec.poss(ag.getComponent(ViewComponent.class).getView(ag.toString())
				.RefineViewByRevision(revision.getProposition(), AnswerValue.AV_FALSE))){
			if(!cexec.poss(ag.getComponent(ViewComponent.class).getView(ag.toString())
					.RefineViewByRevision(revision.getProposition(), AnswerValue.AV_TRUE))){
					Answer answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), AnswerValue.AV_FALSE);
					Subgoal answerGoal = new Subgoal(ag, desire);
					answerGoal.newStack(answer);
					ag.getPlanComponent().addPlan(answerGoal);
					pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
							"' to the plan", ag.getPlanComponent());
					return;
			}else{
				for(Secret a : conf.getTargets()){
					if(cexec.skepticalInference(ag.getComponent(ViewComponent.class).getView(ag.toString())
							.RefineViewByRevision(revision.getProposition(), AnswerValue.AV_FALSE), a.getInformation())){
								Answer answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), AnswerValue.AV_REJECT);
								Subgoal answerGoal = new Subgoal(ag, desire);
								answerGoal.newStack(answer);
								ag.getPlanComponent().addPlan(answerGoal);
								pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
										"' to the plan", ag.getPlanComponent());
								return;
							}
					}
			}
		}
		AnswerValue answerValue;
		if(cexec.skepticalInference(ag.getComponent(ViewComponent.class).getView(ag.toString()), revision.getProposition())){
			answerValue = AnswerValue.AV_TRUE;
			ag.getBeliefs().getWorldKnowledge().addKnowledge(revision.getProposition());
		}else{
			answerValue = AnswerValue.AV_FALSE;
		}
		
		ag.getComponent(ViewComponent.class).setView(ag.toString(), ag.getComponent(ViewComponent.class).getView(ag.toString()).RefineViewByRevision(revision.getProposition(), answerValue));
		
		Answer answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), answerValue);
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
	}

}
