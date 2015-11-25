package com.github.kreaturesfw.core.comm;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.simpleframework.xml.Element;

import com.github.kreaturesfw.core.Agent;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.util.Utility;

/**
 * Implementation of the SpeechAct "Justify". An agent A asks another agent B
 * why he deducts a specific answer for a given sentence.
 * 
 * For further details see Definition 21 in 
 * "Angerona - A Multiagent Framework for Logic Based Agents". (DRAFT)
 * @author Tim Janus
 */
public class Justify extends SpeechAct {

	/** The FOL formula representing the sentence the receiver has to justify. */
	@Element(name="proposition", required=true)
	private FolFormula propositon;
	
	/** the answer which is given to the sentence */
	@Element(name="answerValue", required=true)
	private AnswerValue answerValue;
	
	/** Ctor used by deserilization */
	public Justify(
			@Element(name="sender") String sender, 
			@Element(name="receiver") String receiver, 
			@Element(name="proposition") FolFormula sentence, 
			@Element(name="answerValue") AnswerValue answerValue) {
		super(sender,receiver);
		this.propositon = sentence;
		this.answerValue = answerValue;
	}
	
	/**
	 * Ctor: Creates a justify speech act using true as default answer-value.
	 * @param sender	The reference to the sendeing agent
	 * @param receiver	The name of the receiver
	 * @param sentence	The sentence
	 */
	public Justify(Agent sender, String receiver, FolFormula sentence) {
		this(sender, receiver, sentence, AnswerValue.AV_TRUE);
	}
	
	/** 
	 * Ctor: Creates a justify speech act.
	 * @param sender	The reference to the sendeing agent
	 * @param receiver
	 * @param sentence
	 * @param av
	 */
	public Justify(Agent sender, String receiver, FolFormula sentence, AnswerValue av) {
		super(sender, receiver);
		if(av == AnswerValue.AV_REJECT || av == AnswerValue.AV_COMPLEX)
			throw new IllegalArgumentException("The Justify speech act only allows the " +
					"following answers. {true, false, unknown}.");

		this.propositon = sentence;
		this.answerValue = AnswerValue.AV_TRUE;
	}

	public FolFormula getProposition() {
		return propositon;
	}
	
	public AnswerValue getAnswerValue() {
		return answerValue;
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Justify))
			return false;
		
		if(!super.equals(other)) 
			return false;
		
		Justify oj = (Justify)other;
		return	answerValue == oj.answerValue &&
				Utility.equals(propositon, oj.propositon);
	}
	
	@Override
	public int hashCode() {
		return (super.hashCode() + answerValue.hashCode() + propositon.hashCode())*19;
	}
	
	@Override
	public String toString() {
		return "< " +getSenderId() + " justify " + getReceiverId() + " " + propositon + " " +
					answerValue + ">";
	}

	@Override
	public SpeechActType getType() {
		return SpeechActType.SAT_REQUESTING;
	}

	@Override
	public Set<FolFormula> getContent() {
		Set<FolFormula> reval = new HashSet<>();
		reval.add(propositon);
		return reval;
	}
}
