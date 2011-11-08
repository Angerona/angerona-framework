package angerona.fw.nacts;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.logic.AnswerValue;

/**
 * Implementation of the negotiation act "Answer".
 * @author Tim Janus
 */
public class NAAnswer extends NegotiationAct {

	/** the formula representing the question */
	private FolFormula regarding;
	
	/** the value of the answer */
	private AnswerValue answer;
	
	/**
	 * Ctor: Generates the Answer negotiation act by the following parameters.
	 * @param senderId		unique name of the sender of the answer
	 * @param receiverId	unique name of the receiver of the answer
	 * @param regarding		formula representing the question
	 * @param answer		the value of the answer.
	 */
	public NAAnswer(String senderId, String receiverId, FolFormula regarding, AnswerValue answer) {
		super(senderId, receiverId);
		this.regarding = regarding;
		this.answer = answer;
	}

	/** @return the formula representing the question */
	public FolFormula getRegarding() {
		return regarding;
	}
	
	/** @return the value of the answer */
	public AnswerValue getAnswer() {
		return answer;
	}
	
	@Override
	public String toString() {
		return "NAAnswer: " + regarding.toString() + " --> " + answer.toString();
	}
}
