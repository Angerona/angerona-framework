package angerona.fw.operators.dummy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.ConfidentialTarget;
import angerona.fw.operators.BasePolicyControlOperator;
import angerona.fw.operators.parameter.PolicyControlParameter;

/**
 * The dummy policy control operator gives correct answers. But if the answer would break
 * confidential it prefers to lie.
 * @author Tim Janus
 */
public class DummyPolicyControlOperator extends BasePolicyControlOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DummyPolicyControlOperator.class);
	
	@Override
	protected AngeronaAnswer processInt(PolicyControlParameter param) {
		report("Dummy: Policy-Control Operator");
		
		ConfidentialKnowledge ck = (ConfidentialKnowledge)param.getPolicyProofer().getBeliefs().getConfidentialKnowledge();
		ConfidentialTarget ct = ck.getTarget(param.getInformationReceiver(), param.getQuestion());
		AngeronaAnswer aa = param.getAnswer();
		AnswerValue newAnswer = aa.getAnswerExtended();
		String id = param.getPolicyProofer().getName();
		
		if(ct != null) {
			if(ct.contains(aa)) {
				LOG.info(id + " Found CF=" + ct + " and answer=" + aa);
				if(	aa.getAnswerExtended() == AnswerValue.AV_TRUE &&
					!ct.contains(AnswerValue.AV_FALSE)) {
					newAnswer = AnswerValue.AV_FALSE;
				} else if(aa.getAnswerExtended() == AnswerValue.AV_FALSE &&
					!ct.contains(AnswerValue.AV_TRUE)) {
					newAnswer = AnswerValue.AV_TRUE;
				}
				else {
					newAnswer = AnswerValue.AV_UNKNOWN;
				}
			}
		}
			
		return new AngeronaAnswer(aa.getKnowledgeBase(), aa.getQuery(), newAnswer);
		
	}
	
}
