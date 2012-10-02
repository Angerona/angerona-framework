package angerona.fw.comm;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Agent;

/**
 * Implementation of the Speech-Act: Query. 
 * The given question might be a formula with a variable. Then it is
 * called an open question.
 * In database theory the answer to an open question would return all
 * found answers. In an MAS it is the agents job to decide how many information
 * it want to reveale to answer to the open question.
 * @author Tim Janus, Daniel Dilger
 */
public class Query extends SpeechAct {

	/** formula representing the question of the query */
	private FolFormula question;
	
	/**
	 * Ctor: generating the query speech act with the following parameters.
	 * @param sender		The reference to the sendeing agent
	 * @param receiverId	unique name  of the receiver of the query.
	 * @param question		formula representing the query question.
	 */
	public Query(Agent sender, String receiverId, FolFormula question) {
		super(sender, receiverId);
		this.question = question;
	}
	
	/** @return formula representing the question of the query */
	public FolFormula getQuestion() {
		return question;
	}
	
	@Override 
	public String toString() {
		return "< " + getSenderId() + " query " + getReceiverId() + " " + question + " >";
	}

	/** @return true if the question is an open question, false if the question is not open.*/
	public boolean isOpen() {
		return !question.isGround();
	}
}
