package com.github.angerona.fw.defendingagent.operators.def;

import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.comm.SpeechAct;
import com.github.angerona.fw.comm.Update;
import com.github.angerona.fw.defendingagent.CompressedHistory;
import com.github.angerona.fw.defendingagent.GeneralHistory;
import com.github.angerona.fw.defendingagent.GeneralView;
import com.github.angerona.fw.defendingagent.HistoryComponent;
import com.github.angerona.fw.defendingagent.View;
import com.github.angerona.fw.defendingagent.ViewDataComponent;
import com.github.angerona.fw.defendingagent.ViewWithCompressedHistory;
import com.github.angerona.fw.defendingagent.ViewWithHistory;
import com.github.angerona.fw.defendingagent.comm.RevisionAnswer;
import com.github.angerona.fw.defendingagent.comm.UpdateAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.operators.BaseUpdateBeliefsOperator;
import com.github.angerona.fw.operators.parameter.EvaluateParameter;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeUpdate;
import com.github.angerona.fw.reflection.FolFormulaVariable;

/**
 * Defensive Update Belief operator only handles proactive Answer and RevisionAnswer and UpdateAnswer speech acts
 * send by the defending agent. Whenever the answer is not a rejection, this operator
 * updates the view and belief base according to the revision/request result calculated by the censor
 * component.
 * 
 * @author Sebastian Homann, Pia Wierzoch
 */
public class UpdateBeliefsOperator extends BaseUpdateBeliefsOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(UpdateBeliefsOperator.class);
	
	@Override
	protected Beliefs processImpl(EvaluateParameter param) {
		LOG.info("Run Defending-Update-Beliefs-Operator");
		Beliefs beliefs = param.getBeliefs();
		Beliefs oldBeliefs = (Beliefs)param.getBeliefs().clone();
		String id = param.getAgent().getName();
		SpeechAct act = (SpeechAct)param.getAtom();
		if(act == null) {
			return beliefs;
		}
		String out = "Received perception: " + act.toString();
		ViewDataComponent views = param.getAgent().getComponent(ViewDataComponent.class);
		HistoryComponent histories = param.getAgent().getComponent(HistoryComponent.class);
		// ignore incoming actions
		if(!id.equals(act.getSenderId())) {
			param.report(out);
			return beliefs;
		}
		if(act instanceof RevisionAnswer) {
			// answer is a response to a revision request
			RevisionAnswer ans = (RevisionAnswer) act;
			AnswerValue value = ans.getAnswer().getAnswerValue();
			if(value == AnswerValue.AV_REJECT) {
				// do nothing
				param.report(out);
			} else {
				// refine view
				//the view only can be the class view all the other view don't use revision
				View view = (View) views.getView(act.getReceiverId());
				view = view.RefineViewByRevision(ans.getRegarding(), value);
				views.setView(act.getReceiverId(), view);
//				param.report("Refined view on agent '" + act.getReceiverId() + "': " + view.toString());
				
				BaseBeliefbase bb = null;
				if(value == AnswerValue.AV_TRUE) {
					// revision successful, add knowledge to beliefbase
					bb = beliefs.getWorldKnowledge();
					bb.addKnowledge(ans.getRegarding());
					param.report("Add new information '" + ans.getRegarding() + "' to belief base");
					
				}
				param.report("Send RevisionAnswer to revision request: " + act.toString(), bb);
			}
		} else if(act instanceof UpdateAnswer) {
			// answer must be a response to a update execution request
			UpdateAnswer ans = (UpdateAnswer) act;
			AnswerValue value = ans.getAnswer().getAnswerValue();
			Update update = new Update(ans.getReceiverId(), ans.getSenderId(), ans.getRegarding());
			GeneralHistory history = histories.getHistories().get(ans.getReceiverId());
			if(value != AnswerValue.AV_REJECT) {
				GeneralView view = views.getView(act.getReceiverId());
				if(view instanceof ViewWithCompressedHistory){
					view = ((ViewWithCompressedHistory)view).RefineViewByUpdate(update.getProposition(), value);
					((ViewWithCompressedHistory)view).calculatePossibleBeliefbases((CompressedHistory) history);
					views.setView(act.getReceiverId(), (ViewWithCompressedHistory)view);
				}else if(view instanceof ViewWithHistory){
					view = ((ViewWithHistory)view).RefineViewByUpdate(update.getProposition(), value);
					views.setView(act.getReceiverId(), (ViewWithHistory)view);
				}
			}
			
			PLWithKnowledgeBeliefbase bb = (PLWithKnowledgeBeliefbase) beliefs.getWorldKnowledge();
			PLWithKnowledgeUpdate updateOp = (PLWithKnowledgeUpdate) bb.getChangeOperator().getImplementation();
			FOLPropTranslator translator = new FOLPropTranslator();
			updateOp.processImpl(bb, translator.toPropositional(ans.getRegarding()));
			
			param.report("Add new information '" + ans.getRegarding() + "' to belief base");	
			
			param.report("Send Answer to Update: " + act.toString());
			
		}else if(act instanceof Answer) {
			// answer must be a response to a query execution request
			Answer ans = (Answer) act;
			AnswerValue value = ans.getAnswer().getAnswerValue();
			// refine view
			if(value != AnswerValue.AV_REJECT) {
				GeneralView view = views.getView(act.getReceiverId());
				if(view instanceof View){
					view = ((View)view).RefineViewByQuery(ans.getRegarding(), value);
					views.setView(act.getReceiverId(), view);
					//param.report("Refined view on agent '" + act.getReceiverId() + "': " + view.toString());
				}else{
					Query query = new Query(ans.getReceiverId(), ans.getSenderId(), new FolFormulaVariable(ans.getRegarding()));
					GeneralHistory history = histories.getHistories().get(ans.getReceiverId());
					
					if(view instanceof ViewWithCompressedHistory){
						((ViewWithCompressedHistory)view).calculatePossibleBeliefbases((CompressedHistory) history);	
					}else if(view instanceof ViewWithHistory){
						view = ((ViewWithHistory)view).RefineViewByUpdate(query.getQuestion(), value);
						views.setView(act.getReceiverId(), (ViewWithHistory)view);
					}	
				}			
			}
			param.report("Send Answer to query: " + act.toString());
		}
		
		
		// Inform agent listeners about update invocation
		if(beliefs.getCopyDepth() == 0) {
			param.getAgent().onUpdateBeliefs((Perception)param.getAtom(), oldBeliefs);
		}
		
		return beliefs;
	}
}
