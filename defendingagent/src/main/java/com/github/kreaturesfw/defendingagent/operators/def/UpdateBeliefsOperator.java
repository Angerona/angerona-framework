package com.github.kreaturesfw.defendingagent.operators.def;

import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.basic.Perception;
import com.github.kreaturesfw.core.comm.Answer;
import com.github.kreaturesfw.core.comm.Query;
import com.github.kreaturesfw.core.comm.SpeechAct;
import com.github.kreaturesfw.core.comm.Update;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.logic.Beliefs;
import com.github.kreaturesfw.core.operators.BaseUpdateBeliefsOperator;
import com.github.kreaturesfw.core.operators.parameter.EvaluateParameter;
import com.github.kreaturesfw.core.reflection.FolFormulaVariable;
import com.github.kreaturesfw.defendingagent.BetterView;
import com.github.kreaturesfw.defendingagent.GeneralView;
import com.github.kreaturesfw.defendingagent.View;
import com.github.kreaturesfw.defendingagent.ViewDataComponent;
import com.github.kreaturesfw.defendingagent.ViewWithCompressedHistory;
import com.github.kreaturesfw.defendingagent.ViewWithHistory;
import com.github.kreaturesfw.defendingagent.comm.RevisionAnswer;
import com.github.kreaturesfw.defendingagent.comm.UpdateAnswer;
import com.github.kreaturesfw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.kreaturesfw.plwithknowledge.logic.PLWithKnowledgeUpdate;

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
			if(value != AnswerValue.AV_REJECT) {
				GeneralView view = views.getView(act.getReceiverId());
				if(view instanceof ViewWithCompressedHistory){
					view = ((ViewWithCompressedHistory)view).RefineViewByUpdate(update.getProposition(), value);
					views.setView(act.getReceiverId(), (ViewWithCompressedHistory)view);
				}else if(view instanceof ViewWithHistory){
					view = ((ViewWithHistory)view).RefineViewByUpdate(update.getProposition(), value);
					views.setView(act.getReceiverId(), (ViewWithHistory)view);
				}else if(view instanceof BetterView){
					view = ((BetterView)view).RefineViewByUpdate(update.getProposition(), value);
					views.setView(act.getReceiverId(), (BetterView)view);
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
					if(view instanceof ViewWithHistory){
						view = ((ViewWithHistory)view).RefineViewByQuery(query.getQuestion(), value);
						views.setView(act.getReceiverId(), (ViewWithHistory)view);
					}else if(view instanceof BetterView){
						view = ((BetterView)view).RefineViewByQuery(query.getQuestion(), value);
						views.setView(act.getReceiverId(), (BetterView)view);
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
