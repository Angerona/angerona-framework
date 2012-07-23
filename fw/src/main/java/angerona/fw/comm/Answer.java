package angerona.fw.comm;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Action;
import angerona.fw.logic.AnswerValue;

/**
 * Implementation of the speech act "Answer".
 * @author Tim Janus
 */
public class Answer extends SpeechAct {

	/** the formula representing the question */
	private FolFormula regarding;
	
	/** the value of the answer */
	private AnswerValue answer;
	
	/**
	 * Ctor: Generates the Answer speech act by the following parameters.
	 * @param senderId		unique name of the sender of the answer
	 * @param receiverId	unique name of the receiver of the answer
	 * @param regarding		formula representing the question
	 * @param answer		the value of the answer.
	 */
	public Answer(String senderId, String receiverId, FolFormula regarding, AnswerValue answer) {
		super(senderId, receiverId);
		this.regarding = regarding;
		this.answer = answer;
	}

	/** @return the formula representing the question */
	public FolFormula getRegarding() {
		return regarding;
	}
	
	/** @return the formula representing the question */
	public void setAnswer(AnswerValue val) {
		answer = val;
	}
	
	/** @return the value of the answer */
	public AnswerValue getAnswer() {
		return answer;
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " answers " + getReceiverId() + " " + regarding.toString() + "=" + answer.toString() + " >";
	}
	//************Begin Daniel's changes*************//
	@Override 
	public boolean equals(Action a)
	{
		if (a instanceof Answer)
		{
			Answer ans = (Answer) a;
			if(!super.equals(a))
			{
				return false;
			}
			if(!this.regarding.equals(ans.getRegarding()))
			{
				return false;
			}
			if(this.answer != ans.getAnswer())
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	//************End Daniel's changes*************//
	
}
