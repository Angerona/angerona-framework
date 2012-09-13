package angerona.fw.comm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.logic.AnswerValue;

/**
 * Implementation of speech act "Justification". Agent A tells another agent the reason why he
 * deducts the a sentence with a specific answer. The reason is a set of FOL formulas explaining
 * the deduction. 
 * 
 * For further details see Definition 21 in 
 * "Angerona - A Multiagent Framework for Logic Based Agents". (DRAFT)
 * @author Tim Janus
 */
public class Justification extends SpeechAct {

	/** the formula which has to be justified. */
	private FolFormula regarding;
	
	/** the answer to the formula: only {true, false, unknown} are allowed. */
	private AnswerValue answerValue;
	
	/** a set of fol formulas explaining the deduction */
	private Set<FolFormula> reason = new HashSet<>();
	
	public Justification(Justify source, Set<FolFormula> reason) {
		this(source.getReceiverId(), source.getSenderId(), source.getSentence(), 
				source.getAnswerValue(), reason);
	}
	
	public Justification(String sender, String receiver, FolFormula regarding, 
			AnswerValue answerValue, Set<FolFormula> reason) {
		super(sender, receiver);
		this.regarding = regarding;
		this.answerValue = answerValue;
		this.reason = reason;
	}

	public FolFormula getRegarding() {
		return regarding;
	}
	
	public Set<FolFormula> getReason() {
		return Collections.unmodifiableSet(reason);
	}
	
	public AnswerValue getAnswerValue() {
		return answerValue;
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " justification " + getReceiverId() + " " 
					+ regarding + " " + answerValue + " " + reason + " >";
	}
}
