package angerona.fw.operators.parameter;

import net.sf.tweety.Formula;
import angerona.fw.Agent;
import angerona.fw.logic.AngeronaAnswer;

/**
 * Parameters used by the policy control operator.
 * @see angerona.fw.operators.BasePolicyControlOperator
 * @author Tim Janus
 */
public class PolicyControlParameter extends GenericOperatorParameter {

	/** reference to the agent who will send information to another agent */
	private Agent policyProofer;

	/** unique name of the agent who receives the informations */
	private String informationReceiverId;
	
	/** the answer to the question. */
	private AngeronaAnswer answer;

	/** formula representing the question */
	private Formula question;
	
	/**
	 * Ctor: Generating the policy-control parameter data-structure with the following parameters:
	 * @param policyProofer			reference to the agent who is controls the policies.
	 * @param answerReceiverId	unique name of the agent who will receive the answer.
	 * @param answer			the not yet controlled answer to the question.
	 * @param question			formula representing the question.
	 */
	public PolicyControlParameter(Agent policyProofer, String answerReceiverId, AngeronaAnswer answer, Formula question) {
		super(policyProofer.getEnvironment());
		this.policyProofer = policyProofer;
		this.informationReceiverId = answerReceiverId;
		this.answer = answer;
		this.question = question;
	}

	/** @return reference to the agent who will send information to another agent */
	public Agent getPolicyProofer() {
		return policyProofer;
	}
	
	/** @return unique name of the agent who receives the information */
	public String getInformationReceiver() {
		return informationReceiverId;
	}
	
	/** @return the answer to the question */
	public AngeronaAnswer getAnswer() {
		return answer;
	}
	
	public Formula getQuestion() {
		return question;
	}
}
