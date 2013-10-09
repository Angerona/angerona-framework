package com.github.angerona.fw.comm;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.simpleframework.xml.Element;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.reflection.FolFormulaVariable;
import com.github.angerona.fw.util.Utility;

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
	@Element(name="question", required=true)
	private FolFormulaVariable question;
	
	/** Ctor used by deserilization */
	public Query(	@Element(name="sender") String sender, 
					@Element(name="receiver") String receiver, 
					@Element(name="question") FolFormulaVariable question) {
		super(sender, receiver);
		this.question = question;
	}
	
	/**
	 * Ctor: generating the query speech act with the following parameters.
	 * @param sender		The reference to the sendeing agent
	 * @param receiverId	unique name  of the receiver of the query.
	 * @param question		formula representing the query question.
	 */
	public Query(Agent sender, String receiverId, FolFormula question) {
		super(sender, receiverId);
		this.question = new FolFormulaVariable(question);
	}
	
	/** @return formula representing the question of the query */
	public FolFormula getQuestion() {
		return question.getInstance(getAgent() == null ? null : getAgent().getContext());
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Query)) 
			return false;
		
		if(!super.equals(other))
			return false;
		
		Query oq = (Query)other;
		return Utility.equals(question, oq.question);
	}
	
	@Override
	public int hashCode() {
		return (super.hashCode() + question.hashCode()) * 11;
	}
	
	@Override 
	public String toString() {
		return "< " + getSenderId() + " query " + getReceiverId() + " " + question + " >";
	}

	/** @return true if the question is an open question, false if the question is not open.*/
	public boolean isOpen() {
		return !question.getInstance(getAgent().getContext()).isGround();
	}

	@Override
	public SpeechActType getType() {
		return SpeechActType.SAT_REQUESTING;
	}
}
