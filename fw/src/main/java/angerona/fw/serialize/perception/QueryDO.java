package angerona.fw.serialize.perception;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="query")
public class QueryDO extends CommunicationActDO {
	
	@Element
	private String question;

	
	public String getQuestion() {
		return question;
	}

	@Override
	public String getPerception() {
		return "Query";
	}
	
	public static QueryDO getTestObject() {
		QueryDO test = new QueryDO();
		test.sender = "Tim";
		test.receiver = "Tom";
		test.question = "at_home";
		return test;
	}
}
