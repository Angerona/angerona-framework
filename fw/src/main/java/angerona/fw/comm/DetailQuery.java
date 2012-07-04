package angerona.fw.comm;

import net.sf.tweety.Formula;

public class DetailQuery extends Query{
	protected boolean openQuery = false;
	public DetailQuery(String senderId, String receiverId, Formula question) 
	{
		super(senderId, receiverId, question);
		this.openQuery = openQuery;
	}
	@Override 
	public String toString() {
		return "< " + getSenderId() + " detailed query " + getReceiverId() + " " + super.getQuestion() + " >";
	}
	public boolean openQuery()
	{
		return this.openQuery;
	}
}
