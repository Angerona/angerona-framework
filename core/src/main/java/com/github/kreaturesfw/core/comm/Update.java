package com.github.kreaturesfw.core.comm;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.simpleframework.xml.Element;

import com.github.kreaturesfw.core.legacy.Agent;

/**
 * Implementation of the Speech-Act: Update.
 * A Update speech act represents a update request by an attacking agent.
 * It contains a first order variable.
 * 
 * @author Pia Wierzoch
 */
public class Update extends SpeechAct{

	/** formula of the update */
	@Element(name="proposition", required=true)
	private FolFormula proposition;
	
	/** Ctor used by deserilization */
	public Update(	@Element(name="sender") String sender, 
					@Element(name="receiver") String receiver, 
					@Element(name="proposition") FolFormula proposition) {
		super(sender, receiver);
		this.proposition = proposition;
	}
	
	
	/**
	 * Ctor: generating the update speech act with the following parameters.
	 * @param sender		The reference to the sendeing agent
	 * @param receiverId	unique name  of the receiver of the query.
	 * @param question		formula of the update.
	 */
	public Update(Agent sender, String receiverId, FolFormula proposition) {
		super(sender, receiverId);
		this.proposition = proposition;
	}
	
	/** @return formula of the update */
	public FolFormula getProposition() {
		return proposition;
	}

	@Override
	public SpeechActType getType() {
		return SpeechAct.SpeechActType.SAT_INFORMATIVE;
	}

	@Override
	public Set<FolFormula> getContent() {
		Set<FolFormula> reval = new HashSet<>();
		reval.add(proposition);
		return reval;
	}
	
	@Override 
	public String toString() {
		return "< " + this.getSenderId() + " update " + this.getReceiverId() + " " + proposition + " >";
	}

}
