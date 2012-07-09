package angerona.fw.comm;

import net.sf.tweety.Formula;

public class DetailQuery extends Query{
	public DetailQuery(String senderId, String receiverId, Formula question) 
	{
		super(senderId, receiverId, question);
	}
	@Override 
	public String toString() {
		return "< " + getSenderId() + " detailed query " + getReceiverId() + " " + super.getQuestion() + " >";
	}
}
