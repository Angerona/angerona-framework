package angerona.fw.operators.def;

import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.comm.Answer;
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
				BaseBeliefbase view = (BaseBeliefbase) views.get(a.getReceiverId()).clone();
				if(a.getAnswer() == AnswerValue.AV_TRUE) {
					view.addNewKnowledge(a.getRegarding());
				} else if(a.getAnswer() == AnswerValue.AV_FALSE) {
					view.addNewKnowledge(new Negation(a.getRegarding()));
				}
				
				for(ConfidentialTarget ct : conf.getTargets()) {
					if(ct.getSubjectName().equals(a.getReceiverId())) {
						AngeronaAnswer aa = view.reason((FolFormula)ct.getInformation());
						//LOG.info(id + " Found CF=" + ct + " and answer=" + aa);
						if(	(aa.getAnswerExtended() == AnswerValue.AV_TRUE &&
							 !ct.contains(AnswerValue.AV_FALSE)) ||
							(aa.getAnswerExtended() == AnswerValue.AV_FALSE &&
							 !ct.contains(AnswerValue.AV_TRUE)))  {
							report("Confidential-Target: '" + ct + "' of '" + param.getAgent().getName() + "' injured by: '" + param.getAction() + "'", view);
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
