package angerona.fw.operators.def;

import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.ConfidentialTarget;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.parameter.ViolatesParameter;

/**
 * This class is capable of proofing if the applying of an answer
 * action violates confidentially.
 *
 * For every other action type the default violates operator returns
 * false.
 * @author Tim Janus
 */

public class ViolatesOperator extends BaseViolatesOperator {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViolatesOperator.class);
	
	private boolean confidentialityViolated(AnswerValue ansExtended, ConfidentialTarget ct)
	{
		if (ansExtended == AnswerValue.AV_TRUE && !ct.contains(AnswerValue.AV_FALSE))
				 {
					return true;
				 }
		else if (ansExtended == AnswerValue.AV_FALSE && !ct.contains(AnswerValue.AV_TRUE))
			{
				return true;
			}
				 
		return false;
	}
	
	@Override
	protected Boolean processInt(ViolatesParameter param) {
		LOG.info("Run Default-ViolatesOperator");
		if(param.getAction() instanceof Answer) {
			// only apply violates if confidential knowledge is saved in agent.
			ConfidentialKnowledge conf = param.getAgent().getComponent(ConfidentialKnowledge.class);
			if(conf == null)
				return new Boolean(false);
			
			Answer a = (Answer) param.getAction();
			Map<String, BaseBeliefbase> views = param.getBeliefs().getViewKnowledge();
			if(views.containsKey(a.getReceiverId())) {
				BaseBeliefbase view = (BaseBeliefbase) views.get(a.getReceiverId()).clone(); //What if you want to answer to multiple agents?
			
				/*****
				 * Redefinition of confidentiality:
				 * if the attacking agent already holds that information (in the defending agent's view) then no reason to lie.
				 * I think this addition also solves the problem of false positives in finding a breach of confidentiality,
				 * assuming the agent lies and assumes the lie worked. If it doesn't say anything, then we have the same problem as before.
				 * */
				Query query = (Query) (param.getAgent().getActualPerception());
				FolFormula question = (FolFormula)query.getQuestion();
				
				if(a.getAnswer()==view.reason(question).getAnswerExtended())
				{
					return false;
				}
				/******/
				
				if(a.getAnswer() == AnswerValue.AV_TRUE) {
					view.addNewKnowledge(a.getRegarding());
				} else if(a.getAnswer() == AnswerValue.AV_FALSE) {
					view.addNewKnowledge(new Negation(a.getRegarding()));
				}
				
				//Does it even make sense to go through all confidential targets,
				//given how it's making false positives right now?
				for(ConfidentialTarget ct : conf.getTargets()) {
					if(ct.getSubjectName().equals(a.getReceiverId())) {
						AngeronaAnswer aa = view.reason((FolFormula)ct.getInformation());
						//LOG.info(id + " Found CF=" + ct + " and answer=" + aa);
						/*****
						 * Remove false positives: if the confidential target does not match the question being asked, 
						 * then don't consider it
						 * /
						
						/******/
						if (confidentialityViolated(aa.getAnswerExtended(), ct)){
							report("Confidential-Target: '" + ct + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
							//conf.removeConfidentialTarget(ct);
							return new Boolean(true);
						}
					}
				}
			}
		}
		report("No violation applying the action: '" + param.getAction() + "'", param.getAgent());
		return new Boolean(false);
	}
}
