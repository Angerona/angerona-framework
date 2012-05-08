package angerona.fw.serialize.perception;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="revision-request")
public class RevisionRequestDO extends CommunicationActDO {
	@Element
	private String sentences;

	
	public String getSentences() {
		return sentences;
	}

	@Override
	public String getPerception() {
		return "RevisionRequest";
	}
	
	public static RevisionRequestDO getTestObject() {
		RevisionRequestDO test = new RevisionRequestDO();
		test.sender = "Tim";
		test.receiver = "Tom";
		test.sentences = "at_home";
		return test;
	}
}
