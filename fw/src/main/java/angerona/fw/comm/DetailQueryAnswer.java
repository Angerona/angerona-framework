package angerona.fw.comm;
/**
 * Implementation of the speech act "Answer", but extended for more open ("detail") questions
 * @author Daniel Dilger
 */
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.logic.AnswerValue;

public class DetailQueryAnswer extends Answer {
	private FolFormula detailAnswer;
	
	public DetailQueryAnswer(String senderId, String receiverId, FolFormula regarding, FolFormula answer) 
	{
		super(senderId, receiverId, regarding, AnswerValue.AV_REJECT);
		detailAnswer = answer;
	
	}
	public FolFormula getDetailAnswer()
	{
		return detailAnswer;
	}
	@Override
	public String toString() {
		String questionString = getRegarding().toString();
		String answerString = null;
		//Format the output
		if(detailAnswer.toString().equals(questionString))
		{
			answerString = "TRUE";
		}
		else
		{
			answerString = "FALSE";
		}
		return "< " + getSenderId() + " gives the detailed answer " + getReceiverId() + " " + getRegarding().toString() + "=" + answerString + " >";
	}
}
