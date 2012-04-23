package angerona.fw.serialize.perception;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="answer")
public class AnswerDO extends CommunicationActDO {

	@Element
	private String question;
	
	@Element
	private String answer;
	
	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	@Override
	public String getPerception() {
		return "Answer";
	}

}
