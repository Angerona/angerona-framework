package angerona.fw.serialize.perception;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="inform")
public class InformDO extends CommunicationActDO {
	@Element
	private String sentences;

	
	public String getSentences() {
		return sentences;
	}

	@Override
	public String getPerception() {
		return "Inform";
	}
	
	public static InformDO getTestObject() {
		InformDO test = new InformDO();
		test.sender = "Tim";
		test.receiver = "Tom";
		test.sentences = "at_home";
		return test;
	}
}
