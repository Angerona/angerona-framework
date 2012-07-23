package angerona.fw.comm;
/**
 * Implementation of the speech act "Answer", but extended for more open ("detail") questions
 * @author Daniel Dilger
 */
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Action;
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
		else if(detailAnswer.toString().contains("("))
		{
			if(detailAnswer.toString().startsWith("dontKnow"))
			{
				answerString = detailAnswer.toString();
			}
			else
			{
				String d = detailAnswer.toString();
				answerString = d.substring(d.indexOf("(")+1, d.lastIndexOf(")"));
			}
		}
		else
		{
			answerString = "FALSE";
		}
		return "< " + getSenderId() + " gives the detailed answer " + getReceiverId() + " " + getRegarding().toString() + "=" + answerString + " >";
	}
	@Override
	public boolean equals(Action a)
	{
		if(a instanceof DetailQueryAnswer)
		{
			DetailQueryAnswer dans = (DetailQueryAnswer) a;
			if(!super.equals(a))
			{
				return false;
			}
			if(!this.detailAnswer.equals(dans.getDetailAnswer()))
			{
				return false;
			}
			return true;
		}
		return false;
	}
}
