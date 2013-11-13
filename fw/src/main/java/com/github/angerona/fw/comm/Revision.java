package com.github.angerona.fw.comm;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.simpleframework.xml.Element;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.reflection.FolFormulaVariable;
import com.github.angerona.fw.util.Utility;

/**
 * Implementation of the Speech-Act: Revision.
 * A Revision speech act represents a revision request by an attacking agent.
 * It contains a first order variable.
 * @author Pia Wierzoch, Sebastian Homann
 */
public class Revision extends SpeechAct {

	/** formula representing the question of the query */
	@Element(name="proposition", required=true)
	private FolFormulaVariable proposition;
	
	/** Ctor used by deserilization */
	public Revision(	@Element(name="sender") String sender, 
					@Element(name="receiver") String receiver, 
					@Element(name="proposition") FolFormulaVariable proposition) {
		super(sender, receiver);
		this.proposition = proposition;
	}
	
	/**
	 * Ctor: generating the query speech act with the following parameters.
	 * @param sender		The reference to the sendeing agent
	 * @param receiverId	unique name  of the receiver of the query.
	 * @param question		formula representing the query question.
	 */
	public Revision(Agent sender, String receiverId, FolFormula proposition) {
		super(sender, receiverId);
		this.proposition = new FolFormulaVariable(proposition);
	}
	
	/** @return formula representing the question of the query */
	public FolFormula getProposition() {
		return proposition.getInstance(getAgent() == null ? null : getAgent().getContext());
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Revision)) {
			return false;
		}
		
		if(!super.equals(other)) 
			return false;
		
		Revision or = (Revision)other;
		return Utility.equals(proposition, or.proposition);
	}
	
	@Override
	public int hashCode() {
		return (super.hashCode() + proposition.hashCode()) * 17;
	}
	
	@Override 
	public String toString() {
		return "< " + getSenderId() + " revision " + getReceiverId() + " " + proposition + " >";
	}

	/** @return true if the question is an open question, false if the question is not open.*/
	public boolean isOpen() {
		return !proposition.getInstance(getAgent().getContext()).isGround();
	}

	@Override
	public SpeechActType getType() {
		return SpeechActType.SAT_INFORMATIVE;
	}
}
