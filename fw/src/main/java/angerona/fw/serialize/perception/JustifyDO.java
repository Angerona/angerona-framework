package angerona.fw.serialize.perception;

import org.simpleframework.xml.Element;

public class JustifyDO extends CommunicationActDO {

	@Element
	private String proposition;
	
	@Element
	private String answerValue;
	
	public String getProposition() {
		return proposition;
	}

	public String getAnswerValue() {
		return answerValue;
	}

	@Override
	public String getPerception() {
		return "Justify";
	}

	public static JustifyDO getTestObject() {
		JustifyDO test = new JustifyDO();
		test.sender = "Tim";
		test.receiver = "Tom";
		test.proposition = "exused";
		test.answerValue = "true";
		return test;
	}
}
