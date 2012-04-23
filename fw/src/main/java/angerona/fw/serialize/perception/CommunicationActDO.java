package angerona.fw.serialize.perception;

import org.simpleframework.xml.Attribute;

public abstract class CommunicationActDO implements PerceptionDO {

	@Attribute(name="sender")
	protected String sender;
	
	@Attribute(name="receiver")
	protected String receiver;
	
	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;
	}

	@Override
	public abstract String getPerception();

}
