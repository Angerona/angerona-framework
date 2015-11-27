package com.github.kreaturesfw.defendingagent.operators.def;

import java.io.StringReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.bdi.Desire;
import com.github.kreaturesfw.core.bdi.Intention;
import com.github.kreaturesfw.core.bdi.Subgoal;
import com.github.kreaturesfw.core.bdi.components.Desires;
import com.github.kreaturesfw.core.bdi.operators.BaseSubgoalGenerationOperator;
import com.github.kreaturesfw.core.bdi.operators.parameter.PlanParameter;
import com.github.kreaturesfw.core.comm.Answer;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.comm.Revision;
import com.github.kreaturesfw.core.comm.SpeechAct;
import com.github.kreaturesfw.core.comm.Update;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.logic.ScriptingComponent;
import com.github.kreaturesfw.core.operators.OperatorCallWrapper;
import com.github.kreaturesfw.core.operators.parameter.TranslatorParameter;
import com.github.kreaturesfw.defendingagent.BetterView;
import com.github.kreaturesfw.defendingagent.CensorComponent;
import com.github.kreaturesfw.defendingagent.CompressedHistory;
import com.github.kreaturesfw.defendingagent.GeneralHistory;
import com.github.kreaturesfw.defendingagent.GeneralView;
import com.github.kreaturesfw.defendingagent.History;
import com.github.kreaturesfw.defendingagent.HistoryComponent;
import com.github.kreaturesfw.defendingagent.View;
import com.github.kreaturesfw.defendingagent.ViewDataComponent;
import com.github.kreaturesfw.defendingagent.ViewWithCompressedHistory;
import com.github.kreaturesfw.defendingagent.ViewWithHistory;
import com.github.kreaturesfw.defendingagent.comm.RevisionAnswer;
import com.github.kreaturesfw.defendingagent.comm.UpdateAnswer;
import com.github.kreaturesfw.ocf.logic.ConditionalBeliefbase;
import com.github.kreaturesfw.ocf.logic.ConditionalRevision;
import com.github.kreaturesfw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.kreaturesfw.plwithknowledge.logic.PLWithKnowledgeUpdate;
import com.github.kreaturesfw.secrecy.Secret;
import com.github.kreaturesfw.secrecy.components.SecrecyKnowledge;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.parser.ParseException;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;

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
	protected Boolean processImpl(PlanParameter pp) {
		LOG.info("Run Default-Subgoal-Generation");
		Agent ag = pp.getActualPlan().getAgent();
		Desires des = ag.getComponent(Desires.class);
		
		boolean reval = processPersuadeOtherAgentsDesires(pp, ag);
		
		if(des != null) {
			Set<Desire> currentDesires;
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareQueryProcessing);
			for(Desire d : currentDesires) {
				reval = reval || processQuery(d, pp, ag);
			}
			
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareRevisionProcessing);
			for(Desire d : currentDesires) {
				reval = reval || processRevision(d, pp, ag);
			}
			
			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareUpdateProcessing);
			for(Desire d : currentDesires) {
				reval = reval || processUpdate(d, pp, ag);
			}

			currentDesires = des.getDesiresByPredicate(GenerateOptionsOperator.prepareScriptingProcessing);
			for(Desire d : currentDesires){
				reval = reval || processScripting(d, pp, ag);
				
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
	public boolean processQuery(Desire desire, PlanParameter pp, Agent ag) {
		pp.report("Generate new subgoal to process query request");
		
		CensorComponent cexec = ag.getComponent(CensorComponent.class);
		
		Query query = (Query) desire.getPerception();		
		
		GeneralView v = ag.getComponent(ViewDataComponent.class).getView(query.getSenderId());
		SecrecyKnowledge conf = ag.getComponent(SecrecyKnowledge.class);
		GeneralHistory history = null;
		
		pp.report("Invoke censor to check all possible answers for meta inferences");
		if(v instanceof View){
			View view = (View) v;
			// check for all possible answers to the query, whether this answer would
			// potentially reveal a secret and in that case, refuse to answer.
			for(Secret a : conf.getSecrets()){
				//ans := true
				if(cexec.poss(view
						.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_TRUE)) && 
						cexec.scepticalInference(view
						.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_TRUE), a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "true");
					return true;
				}
				//ans := false
				if(cexec.poss(view
						.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_FALSE)) && 
						cexec.scepticalInference(view
						.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_FALSE), a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "false");
					return true;
				}
				//ans := undef
				if(cexec.poss(view
						.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_UNKNOWN)) && 
						cexec.scepticalInference(view
						.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_UNKNOWN), a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "unknown");
					return true;
				}
			}
		}else if(v instanceof ViewWithCompressedHistory){
			ViewWithCompressedHistory view = (ViewWithCompressedHistory) v;
			history = ag.getComponent(HistoryComponent.class).getHistories().get(query.getSenderId());
			if(history == null){
				history = new CompressedHistory();
				ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
			}
			for(Secret a : conf.getSecrets()){
				//ans := true
				ViewWithCompressedHistory copyView = new ViewWithCompressedHistory(view);
				CompressedHistory refinedHistory = new CompressedHistory((CompressedHistory)history);
				refinedHistory.putAction(query, AnswerValue.AV_TRUE);
				if(cexec.scepticalInference(copyView, a.getInformation(), refinedHistory)){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "true");
					history.putAction(query, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
					return true;
				}

				//ans := false
				copyView = new ViewWithCompressedHistory(view);
				refinedHistory = new CompressedHistory((CompressedHistory)history);
				refinedHistory.putAction(query, AnswerValue.AV_FALSE);
				if(cexec.scepticalInference(copyView, a.getInformation(), refinedHistory)){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "false");
					history.putAction(query, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
					return true;
				}
				
				//ans := undef
				copyView = new ViewWithCompressedHistory(view);
				refinedHistory = new CompressedHistory((CompressedHistory)history);
				refinedHistory.putAction(query, AnswerValue.AV_UNKNOWN);
				if(cexec.scepticalInference(copyView, a.getInformation(), refinedHistory)){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "unknown");
					history.putAction(query, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
					return true;					
				}
			}
		}else if(v instanceof ViewWithHistory){
			ViewWithHistory view = (ViewWithHistory) v;
			history =  ag.getComponent(HistoryComponent.class).getHistories().get(query.getSenderId());
			if(history == null){
				history = new History();
				ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
			}
			// check for all possible answers to the query, whether this answer would
			// potentially reveal a secret and in that case, refuse to answer.
			for(Secret a : conf.getSecrets()){
				//ans := true
				if(cexec.scepticalInference(view.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_TRUE),
						a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "true");
					history.putAction(query, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
					return true;
				}
				//ans := false
				if(cexec.scepticalInference(view.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_FALSE),
						a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "false");
					history.putAction(query, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
					return true;
				}
				//ans := undef
				if(cexec.scepticalInference(view.RefineViewByQuery(query.getQuestion(), AnswerValue.AV_UNKNOWN), 
						a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "unknown");
					history.putAction(query, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
					return true;
				}
			}
		}else if(v instanceof BetterView){
			BetterView view = (BetterView) v;
			
			// check for all possible answers to the query, whether this answer would
			// potentially reveal a secret and in that case, refuse to answer.
			for(Secret a : conf.getSecrets()){
				//ans := true
				if(cexec.scepticalInference(view.MentalRefineViewByQuery(query.getQuestion(), AnswerValue.AV_TRUE),
						a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "true");
					return true;
				}
				//ans := false
				if(cexec.scepticalInference(view.MentalRefineViewByQuery(query.getQuestion(), AnswerValue.AV_FALSE),
						a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "false");
					return true;
				}
				//ans := undef
				if(cexec.scepticalInference(view.MentalRefineViewByQuery(query.getQuestion(), AnswerValue.AV_UNKNOWN), 
						a.getInformation())){
					this.sendRejectAnswer(ag, desire, cexec, query, a, "unknown");
					return true;
				}
			}
		}
		cexec.report("No answer to query '"+ query.getQuestion() + " would reveal a secret. Start actual handling.");
		
		// no secret will be revealed by any possible answer to the query.
		// handle the query and create an appropriate answer action.
		AngeronaAnswer answer = ag.getBeliefs().getWorldKnowledge().reason(query.getQuestion());
		if(history != null){
			history.putAction(query,answer.getAnswerValue());
			ag.getComponent(HistoryComponent.class).getHistories().put(query.getSenderId(), history);
		}
		ag.getBeliefs().getWorldKnowledge().report("Actual answer to query: " + answer.getAnswerValue());
		Answer answerSpeechAct = new Answer(ag,query.getSenderId(), query.getQuestion(), answer.getAnswerValue());
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answerSpeechAct);
		ag.getPlanComponent().addPlan(answerGoal);
				
		pp.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
		return true;
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
	public boolean processRevision(Desire desire, PlanParameter pp, Agent ag) {
		pp.report("Generate new subgoal to process revision request");
		CensorComponent cexec = ag.getComponent(CensorComponent.class);
		
		Revision revision = (Revision) desire.getPerception();
		View view = (View) ag.getComponent(ViewDataComponent.class).getView(revision.getSenderId());
		SecrecyKnowledge conf = ag.getComponent(SecrecyKnowledge.class);
		
		pp.report("Invoke censor to check all possible answers for meta inferences");
		
		// check if revision would reveal a secret
		View refinedView = view.RefineViewByRevision(revision.getProposition(), AnswerValue.AV_TRUE);
		if(cexec.poss(refinedView)) {
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation())) {
					this.sendRejectAnswer(ag, desire, cexec, revision, s, "success");
					return true;
				}
			}
		}
		
		// check if the failure of the revision would reveal a secret
		refinedView = view.RefineViewByRevision(revision.getProposition(), AnswerValue.AV_FALSE);
		if(cexec.poss(refinedView)) {
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation())) {
					this.sendRejectAnswer(ag, desire, cexec, revision, s, "failure");
					return true;
				}
			}
		}
		
		// revision would not breach confidentiality, continue revising beliefbase
		BaseBeliefbase bbase = ag.getBeliefs().getWorldKnowledge();
		
		// slightly hacked: we need to get the revision result from the revision operator
		OperatorCallWrapper changeOp = bbase.getChangeOperator();
		boolean success = false;
		if(changeOp.getImplementation() instanceof ConditionalRevision) {
			ConditionalRevision revisionOp = (ConditionalRevision) changeOp.getImplementation();
			OperatorCallWrapper translator = bbase.getTranslator();
			Set<FolFormula> revisionFormulas = new HashSet<FolFormula>();
			revisionFormulas.add(revision.getProposition());
			ConditionalBeliefbase newK = (ConditionalBeliefbase) translator.process(new TranslatorParameter(bbase, revisionFormulas));
			success = revisionOp.simulateRevision((ConditionalBeliefbase)bbase, newK);
		}
		
		AnswerValue answerValue = AnswerValue.AV_FALSE;
		if(success) {
			pp.report("Revision will be successful. Send answer 'true' and add '" + revision.getProposition().toString() + "' to belief base");
			answerValue = AnswerValue.AV_TRUE;
		} else {
			pp.report("Revision will be not be successful. Send answer 'false'.");
			answerValue = AnswerValue.AV_FALSE;
		}
		
		// send answer
		RevisionAnswer answer = new RevisionAnswer(ag,revision.getSenderId(), revision.getProposition(), answerValue);
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		pp.report("Add the new action '"+ RevisionAnswer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
		return true;
	}
	
	/**
	 * Process an incoming update request from an attacking agent. Using the censor component,
	 * this method first checks if a secret might be revealed by the actual update handling.
	 * Based on this preprocessing, it either generates a Refusal-SpeechAct or runs the
	 * actual update on the belief base and generates a notification action.
	 * @param desire
	 * @param pp
	 * @param ag
	 */
	public boolean processUpdate(Desire desire, PlanParameter pp, Agent ag) {	
		pp.report("Generate new subgoal to process update request");
		
		CensorComponent cexec = ag.getComponent(CensorComponent.class);
		
		Update update = (Update) desire.getPerception();
				
		GeneralView v = ag.getComponent(ViewDataComponent.class).getView(update.getSenderId());
		SecrecyKnowledge conf = ag.getComponent(SecrecyKnowledge.class);
		GeneralHistory history = null;
		
		pp.report("Invoke censor to check all possible answers for meta inferences");
		if(v instanceof ViewWithCompressedHistory){
			ViewWithCompressedHistory view = new ViewWithCompressedHistory((ViewWithCompressedHistory) v);
			history = ag.getComponent(HistoryComponent.class).getHistories().get(update.getSenderId());
			if(history == null){
				history = new CompressedHistory();
				ag.getComponent(HistoryComponent.class).getHistories().put(update.getSenderId(), history);
			}
			// check if update would reveal a secret
			ViewWithCompressedHistory refinedView = view.RefineViewByUpdate(update.getProposition(), AnswerValue.AV_TRUE);
			CompressedHistory refinedHistory = new CompressedHistory((CompressedHistory)history);
			refinedHistory.putAction(update, AnswerValue.AV_TRUE);
		
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation(), refinedHistory)){
					this.sendRejectAnswer(ag, desire, cexec, update, s, "success");
					history.putAction(update, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(update.getSenderId(), history);
					return true;
				}
			}
			
			// check if the failure of the update would reveal a secret
			refinedView = view.RefineViewByUpdate(update.getProposition(), AnswerValue.AV_FALSE);
			refinedHistory = new CompressedHistory((CompressedHistory)history);
			refinedHistory.putAction(update, AnswerValue.AV_FALSE);
		
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation(), refinedHistory)){
					this.sendRejectAnswer(ag, desire, cexec, update, s, "failure");
					history.putAction(update, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(update.getSenderId(), history);
					return true;
				}
			}
		}else if(v instanceof ViewWithHistory){
			ViewWithHistory view = (ViewWithHistory) v;
			history = ag.getComponent(HistoryComponent.class).getHistories().get(update.getSenderId());
			if(history == null){
				history = new History();
				ag.getComponent(HistoryComponent.class).getHistories().put(update.getSenderId(), history);
			}
			// check if update would reveal a secret
			ViewWithHistory refinedView = view.RefineViewByUpdate(update.getProposition(), AnswerValue.AV_TRUE);
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation())) {
					this.sendRejectAnswer(ag, desire, cexec, update, s, "success");
					history.putAction(update, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(update.getSenderId(), history);
					return true;
				}
			}
			
			// check if the failure of the update would reveal a secret
			refinedView = view.RefineViewByUpdate(update.getProposition(), AnswerValue.AV_FALSE);
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation())) {
					this.sendRejectAnswer(ag, desire, cexec, update, s, "failure");
					history.putAction(update, AnswerValue.AV_REJECT);
					ag.getComponent(HistoryComponent.class).getHistories().put(update.getSenderId(), history);
					return true;
				}
			}
		}else if(v instanceof BetterView){
			BetterView view = (BetterView) v;
			
			// check if update would reveal a secret
			BetterView refinedView = view.RefineViewByUpdate(update.getProposition(), AnswerValue.AV_TRUE);
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation())) {
					this.sendRejectAnswer(ag, desire, cexec, update, s, "success");
					return true;
				}
			}
			
			// check if the failure of the update would reveal a secret
			refinedView = view.RefineViewByUpdate(update.getProposition(), AnswerValue.AV_FALSE);
			for(Secret s : conf.getSecrets()) {
				if(cexec.scepticalInference(refinedView, s.getInformation())) {
					this.sendRejectAnswer(ag, desire, cexec, update, s, "failure");
					return true;
				}
			}
		}

		// update would not breach confidentiality, continue updating beliefbase
		BaseBeliefbase bbase = ag.getBeliefs().getWorldKnowledge();
		
		OperatorCallWrapper changeOp = bbase.getChangeOperator();
		boolean success = false;
		if(changeOp.getImplementation() instanceof PLWithKnowledgeUpdate) {
			PLWithKnowledgeUpdate updateOp = (PLWithKnowledgeUpdate) changeOp.getImplementation();
			success = updateOp.simulateUpdate(update.getProposition(), ((PLWithKnowledgeBeliefbase)bbase).getKnowledge());
		}
		
		AnswerValue answerValue = AnswerValue.AV_FALSE;
		if(success) {
			pp.report("Update will be successful. Send answer 'true' and add '" + update.getProposition().toString() + "' to belief base");
			answerValue = AnswerValue.AV_TRUE;
		} else {
			pp.report("Update will be not be successful. Send answer 'false'.");
			answerValue = AnswerValue.AV_FALSE;
		}
		
		if(history != null){
			history.putAction(update,answerValue);
			ag.getComponent(HistoryComponent.class).getHistories().put(update.getSenderId(), history);
		}
		
		// send answer
		UpdateAnswer answer = new UpdateAnswer(ag,update.getSenderId(), update.getProposition(), answerValue);
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		pp.report("Add the new action '"+ UpdateAnswer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
		
		return true;
	}
	
	public boolean processScripting(Desire d, PlanParameter pp, Agent ag){
		ScriptingComponent script = ag.getComponent(ScriptingComponent.class);
		Desires desires = ag.getComponent(Desires.class);
		desires.remove(d);
		List<Intention> intentions = script.getIntentions();
		String text = intentions.toString();

		int i = 0;
		for(Intention intention : intentions) {
			Desire des = new Desire(new FOLAtom(new Predicate("script"+ i++)));
			desires.add(des);
			Subgoal sg = new Subgoal(ag, des);
			sg.newStack(intention);
			ag.getPlanComponent().addPlan(sg);
		}

		pp.report("Add the new  actions '" + text + 
				"' to the plan, chosen by desire: " + d.toString(), 
				ag.getPlanComponent());
		return true;
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
		Desires desComp = ag.getComponent(Desires.class);
		if(desComp == null)
			return false;
		
		for(Desire desire : desComp.getDesires()) {
			// only add a plan if no plan for the desire exists.
			if(ag.getPlanComponent().countPlansFor(desire) > 0)
				continue;
			
			FolFormula formula = desire.getFormula();
			String atomStr = formula.toString().trim();
			boolean revisionDesire = atomStr.startsWith("r_");
			boolean queryDesire = atomStr.startsWith("q_");
			
			if(revisionDesire || queryDesire) {
				int si = formula.toString().indexOf("_")+1;
				int li = formula.toString().indexOf("(", si);
				if(si == -1 || li == -1)
					continue;
				String recvName = formula.toString().substring(si, li);
				
				si = formula.toString().indexOf("(")+1;
				li = formula.toString().lastIndexOf(")");
				if(si == -1 || li == -1)
					continue;
				String content = formula.toString().substring(si,li);
				
				LOG.info("'{}' wants '"+recvName+"' to believe: '{}'",  ag.getName(), content);
		
				Subgoal sg = new Subgoal(ag, desire);
				FolParserB parser = new FolParserB(new StringReader(content));
				FOLAtom a = null;
				try {
					a = parser.atom(new FolSignature());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				if(revisionDesire) {
					sg.newStack( new Revision(ag, recvName, a));
					ag.getPlanComponent().addPlan(sg);
					pp.report("Add the new atomic action '" + Revision.class.getSimpleName() + 
							"' to the plan, chosen by desire: " + desire.toString(), 
							ag.getPlanComponent());
				} else {
					sg.newStack( new Query(ag, recvName, a));
					ag.getPlanComponent().addPlan(sg);
					pp.report("Add the new atomic action '" + Query.class.getSimpleName() + 
							"' to the plan, chosen by desire: " + desire.toString(), 
							ag.getPlanComponent());
				}
				
				reval = true;
			}
		}
		return reval;
	}
	
	private void sendRejectAnswer(Agent ag, Desire desire, CensorComponent cexec, SpeechAct action, Secret s, String answerValue){
		Answer answer = null;
		String act = null;
		if(action instanceof Update){
			Update update = (Update) action;
			answer = new Answer(ag,update.getSenderId(), update.getProposition(), AnswerValue.AV_REJECT);
			act = "update";
		}else if(action instanceof Revision){
			Revision revision = (Revision) action;
			answer = new Answer(ag,revision.getSenderId(), revision.getProposition(), AnswerValue.AV_REJECT);
			act = "revision";
		}else if(action instanceof Query){
			Query query = (Query) action;
			answer = new Answer(ag,query.getSenderId(), query.getQuestion(), AnswerValue.AV_REJECT);
			act = "query";
		}	
		Subgoal answerGoal = new Subgoal(ag, desire);
		answerGoal.newStack(answer);
		ag.getPlanComponent().addPlan(answerGoal);
		cexec.report("The reaction '" + answerValue +  "' would reveal secret " + s + ". Reject the " + act + ".");
		cexec.report("Add the new action '"+ Answer.class.getSimpleName() + 
				"' to the plan", ag.getPlanComponent());
	}
}
