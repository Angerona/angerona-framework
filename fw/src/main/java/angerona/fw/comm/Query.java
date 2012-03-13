package angerona.fw.comm;

import net.sf.tweety.Formula;

/**
 * Implementation of the Speech-Act: Query. 
 * @author Tim Janus
 */
public class Query extends SpeechAct {

	/** formula representing the question of the query */
	private Formula question;
	
	/**
	 * Ctor: generating the query speech act with the following parameters.
	 * @param senderId		unique name  of the sender of the query.
	 * @param receiverId	unique name  of the receiver of the query.
	 * @param question		formula representing the query question.
	 */
	public Query(String senderId, String receiverId, Formula question) {
		super(senderId, receiverId);
		this.question = question;
	}
	
	/** @return formula representing the question of the query */
	public Formula getQuestion() {
		return question;
	}
	
	@Override 
	public String toString() {
		return "query: " + question;
	}

}
