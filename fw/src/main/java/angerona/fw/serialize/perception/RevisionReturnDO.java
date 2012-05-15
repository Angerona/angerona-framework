package angerona.fw.serialize.perception;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="revision-return")
public class RevisionReturnDO extends CommunicationActDO {
	@Element
	private String sentences;

	@Element
	private Boolean acceptance;
	
	public String getSentences() {
		return sentences;
	}
	
	public Boolean getAcceptance() {
		return acceptance;
	}

	@Override
	public String getPerception() {
		return "RevisionReturn";
	}
}
