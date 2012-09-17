package angerona.fw.serialize.perception;

import org.simpleframework.xml.Element;

public class JustificationDO extends CommunicationActDO {
	@Element
	private String proposition;
	
	@Element
	private String answerValue;
	
	@Element
	private String justifications;

	public String getProposition() {
		return proposition;
	}

	public String getAnswerValue() {
		return answerValue;
	}

	public String getJustifications() {
		return justifications;
	}

	@Override
	public String getPerception() {
		return "Justification";
	}
}
