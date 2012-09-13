package angerona.fw.comm;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.logic.AnswerValue;

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
	private FolFormula sentence;
	
	/** the answer which is given to the sentence */
	private AnswerValue answerValue;
	
	/**
	 * Ctor: Creates a justify speech act using true as default answer-value.
	 * @param sender	The name of the sender
	 * @param receiver	The name of the receiver
	 * @param sentence	The sentence
	 */
	public Justify(String sender, String receiver, FolFormula sentence) {
		this(sender, receiver, sentence, AnswerValue.AV_TRUE);
	}
	
	/** 
	 * Ctor: Creates a justify speech act.
	 * @param sender
	 * @param receiver
	 * @param sentence
	 * @param av
	 */
	public Justify(String sender, String receiver, FolFormula sentence, AnswerValue av) {
		super(sender, receiver);
		if(av == AnswerValue.AV_REJECT || av == AnswerValue.AV_COMPLEX)
			throw new IllegalArgumentException("The Justify speech act only allows the " +
					"following answers. {true, false, unknown}.");

		this.sentence = sentence;
		this.answerValue = AnswerValue.AV_TRUE;
	}

	public FolFormula getSentence() {
		return sentence;
	}
	
	public AnswerValue getAnswerValue() {
		return answerValue;
	}
	
	@Override
	public String toString() {
		return "< " +getSenderId() + " justify " + getReceiverId() + " " + sentence + " " +
					answerValue + ">";
	}
}
