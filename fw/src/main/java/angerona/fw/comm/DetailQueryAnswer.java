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
	
	}
	public FolFormula getDetailAnswer()
	{
		return detailAnswer;
	}
	@Override
	public String toString() {
		return "< " + getSenderId() + " gives the detailed answer " + getReceiverId() + " " + getRegarding().toString() + "=" + getAnswer().toString() + " >";
	}
}
