package angerona.fw.comm;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.logic.AngeronaAnswer;

/**
 * Implementation of the speech act "Answer".
 * @author Tim Janus
 */
public class Answer extends SpeechAct {

	/** the formula representing the question */
	private FolFormula regarding;
	
	/** the object containing information about the answer. */
	private AngeronaAnswer answer;
	
	/**
	 * Ctor: Generates the Answer speech act by the following parameters.
	 * @param senderId		unique name of the sender of the answer
	 * @param receiverId	unique name of the receiver of the answer
	 * @param regarding		formula representing the question
	 * @param answer		the value of the answer.
	 */
	public Answer(String senderId, String receiverId, FolFormula regarding, AngeronaAnswer answer) {
		super(senderId, receiverId);
		this.regarding = regarding;
		this.answer = answer;
	}

	/** @return the formula representing the question */
	public FolFormula getRegarding() {
		return regarding;
	}
	
	public void setAnswer(AngeronaAnswer val) {
		answer = val;
	}
	
	/** @return the value of the answer */
	public AngeronaAnswer getAnswer() {
		return answer;
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " answers " + getReceiverId() + " " + regarding.toString() + "=" + answer.toString() + " >";
	}
	
	@Override 
	public boolean equals(Object obj)
	{
		if (obj instanceof Answer)
		{
			Answer ans = (Answer) obj;
			if(!super.equals(obj)) {
				return false;
			}
			if(!this.regarding.equals(ans.getRegarding())) {
				return false;
			}
			if(!this.answer.equals(ans.getAnswer())) {
				return false;
			}
			return true;
		}
		return false;
	}
}
