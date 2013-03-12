package angerona.fw.DefendingAgent.operators.def;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.parser.FolParserB;
import net.sf.tweety.logics.firstorderlogic.parser.ParseException;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Desire;
import angerona.fw.Subgoal;
import angerona.fw.DefendingAgent.View;
import angerona.fw.DefendingAgent.ViewComponent;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Query;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.logic.Desires;
import angerona.fw.logic.SecrecyKnowledge;
import angerona.fw.logic.Secret;
import angerona.fw.logic.conditional.ConditionalBeliefbase;
import angerona.fw.logic.conditional.ConditionalRevision;

/**
 * Censor agents subgoal generation generates the atomic actions need to react on the
 * different speech acts using a censor component to protect its secrets.
 * Also implements specialized methods to handle initial artifical desires.
 * @author Tim Janus, Sebastian Homann, Pia Wierzoch
 */
public class SubgoalGenerationOperator extends BaseSubgoalGenerationOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(SubgoalGenerationOperator.class);
	
	/**
	 * Processes current desires and breaks them down into subgoals using the censor component.
	 */
	@Override
	protected Boolean processInternal(PlanParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		Desires des = ag.getDesires();
		
		boolean reval = processPersuadeOtherAgentsDesires(pp, ag);
		
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
		return reval;
	}
	
	/**
	 * Process an incoming query request from an attacking agent. Using the censor component,
	 * this method first checks if a secret might be revealed by the actual query handling.
	 * Based on this preprocessing, it either generates a Refusal-SpeechAct or starts the
	 * query handling and generates an appropriate answer.
	 * @param desire
	 * @param pp
	 * @param ag
	 */
	public void processQuery(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();

		Query query = (Query) desire.getPerception();

		View view = ag.getComponent(ViewComponent.class).getView(query.getSenderId());
		SecrecyKnowledge conf = ag.getComponent(SecrecyKnowledge.class);
		
		// check for all possible answers to the query, whether this answer would
		// potentially reveal a secret and in that case, refuse to answer.
		for(Secret a : conf.getTargets()){
			//ans := true
			if(cexec.poss(view
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_TRUE)) && 
					cexec.skepticalInference(view
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
			if(cexec.poss(view
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_FALSE)) && 
					cexec.skepticalInference(view
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
			if(cexec.poss(view
					.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_UNKNOWN)) && 
					cexec.skepticalInference(view
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
		// no secret will be revealed by any possible answer to the query.
		// handle the query and create an appropriate answer action.
		AnswerValue answerValue;
		if(cexec.skepticalInference(view, query.getQuestion())){
			answerValue = AnswerValue.AV_TRUE;
		}else{
			answerValue = AnswerValue.AV_FALSE;
		}
		Answer answer = new Answer(ag,query.getSenderId(), query.getQuestion(), answerValue);
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
	}

	/**
	 * Process an incoming revision request from an attacking agent. Using the censor component,
	 * this method first checks if a secret might be revealed by the actual revision handling.
	 * Based on this preprocessing, it either generates a Refusal-SpeechAct or runs the
	 * actual revision on the belief base and generates a notification action.
	 * @param desire
	 * @param pp
	 * @param ag
	 */
	public void processRevision(Desire desire, PlanParameter pp, Agent ag) {
		Censor cexec = new Censor();
		
		Revision revision = (Revision) desire.getPerception();
		View view = ag.getComponent(ViewComponent.class).getView(revision.getSenderId());
		SecrecyKnowledge conf = ag.getComponent(SecrecyKnowledge.class);
		
		// check for each secret whether it might be revealed by this revision operation
		for(Secret a : conf.getTargets()){
			if(cexec.skepticalInference(view
					.RefineViewByQuery(revision.getProposition(), AnswerValue.AV_TRUE), a.getInformation())){
						Answer answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), AnswerValue.AV_REJECT);
						Subgoal answerGoal = new Subgoal(ag, desire);
						answerGoal.newStack(answer);
						ag.getPlanComponent().addPlan(answerGoal);
						pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
								"' to the plan", ag.getPlanComponent());
						return;
			}
		}
		if(cexec.poss(view
				.RefineViewByQuery(revision.getProposition(), AnswerValue.AV_FALSE))){
			if(!cexec.poss(view
					.RefineViewByQuery(revision.getProposition(), AnswerValue.AV_TRUE))){
					Answer answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), AnswerValue.AV_FALSE);
					Subgoal answerGoal = new Subgoal(ag, desire);
					answerGoal.newStack(answer);
					ag.getPlanComponent().addPlan(answerGoal);
					pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
							"' to the plan", ag.getPlanComponent());
					return;
			}else{
				for(Secret a : conf.getTargets()){
					if(cexec.skepticalInference(view
							.RefineViewByQuery(revision.getProposition(), AnswerValue.AV_FALSE), a.getInformation())){
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
		
		// revision would not breach confidentiality, continue revising beliefbase
		AnswerValue answerValue = AnswerValue.AV_FALSE;
		BaseBeliefbase bbase = ag.getBeliefs().getWorldKnowledge();
		
		// slightly hacked: we need to get the revision result from the revision operator
		BaseChangeBeliefs changeOp = bbase.getChangeOperator();
		boolean success = false;
		if(changeOp instanceof ConditionalRevision) {
			ConditionalRevision revisionOp = (ConditionalRevision) changeOp;
			BaseTranslator translator = bbase.getTranslator();
			Set<FolFormula> revisionFormulas = new HashSet<FolFormula>();
			revisionFormulas.add(revision.getProposition());
			ConditionalBeliefbase newK = (ConditionalBeliefbase) translator.translateFOL(new ConditionalBeliefbase(), revisionFormulas);
			success = revisionOp.simulateRevision((ConditionalBeliefbase)bbase, newK);
		}
		
		if(success) {

			bbase.addKnowledge(revision.getProposition());
			answerValue = AnswerValue.AV_TRUE;
		}
		
		// refine view on the attacking agent
		ag.getComponent(ViewComponent.class).setView(revision.getSenderId(), view.RefineViewByRevision(revision.getProposition(),answerValue));
		
		// send answer
		Answer answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), answerValue);
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
	}
	
	
	/**
	 * This is a helper method: Which searches for desires starting with the prefix 'v_'.
	 * It creates RevisionRequests for such desires.
	 * @param pp		The data-structure containing parameters for the operator.
	 * @param ag		The agent.
	 * @return			true if a new subgoal was created and added to the master-plan, false otherwise.
	 */
	protected boolean processPersuadeOtherAgentsDesires(
			PlanParameter pp, Agent ag) {
		boolean reval = false;
		if(ag.getDesires() == null)
			return false;
		
		for(Desire desire : ag.getDesires().getDesires()) {
			// only add a plan if no plan for the desire exists.
			if(ag.getPlanComponent().countPlansFor(desire) > 0)
				continue;
			
			Atom atom = desire.getAtom();
			String atomStr = atom.toString().trim();
			boolean informDesire = atomStr.startsWith("v_");
			boolean queryDesire = atomStr.startsWith("q_");
			
			if(informDesire || queryDesire) {
				int si = atom.toString().indexOf("_")+1;
				int li = atom.toString().indexOf("(", si);
				if(si == -1 || li == -1)
					continue;
				String recvName = atom.toString().substring(si, li);
				
				si = atom.toString().indexOf("(")+1;
				li = atom.toString().lastIndexOf(")");
				if(si == -1 || li == -1)
					continue;
				String content = atom.toString().substring(si,li);
				
				LOG.info("'{}' wants '"+recvName+"' to believe: '{}'",  ag.getName(), content);
		
				Subgoal sg = new Subgoal(ag, desire);
				FolParserB parser = new FolParserB(new StringReader(content));
				Atom a = null;
				try {
					a = parser.atom(new FolSignature());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(informDesire) {
					sg.newStack( new Inform(ag, recvName, a));
				} else {
					sg.newStack( new Query(ag, recvName, a));
				}
				ag.getPlanComponent().addPlan(sg);
				pp.report("Add the new atomic action '" + Inform.class.getSimpleName() + 
						"' to the plan, choosed by desire: " + desire.toString(), 
						ag.getPlanComponent());
				reval = true;
			}
		}
		return reval;
	}
}
