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
	private FolFormula proposition;
	
	/** the answer to the formula: only {true, false, unknown} are allowed. */
	private AnswerValue answerValue;
	
	/** a set of fol formulas explaining the deduction */
	private Set<FolFormula> justifications = new HashSet<>();
	
	public Justification(Justify source, FolFormula justification) {
		this(source.getReceiverId(), source.getSenderId(), source.getProposition(), 
				source.getAnswerValue(), justification);
	}
	
	public Justification(String sender, String receiver, FolFormula proposition, 
			AnswerValue answerValue, FolFormula justification) {
		super(sender,receiver);
		this.proposition = proposition;
		this.answerValue = answerValue;
		this.justifications = new HashSet<>();
		this.justifications.add(justification);
	}
	
	public Justification(Justify source, Set<FolFormula> justifications) {
		this(source.getReceiverId(), source.getSenderId(), source.getProposition(), 
				source.getAnswerValue(), justifications);
	}
	
	public Justification(String sender, String receiver, FolFormula proposition, 
			AnswerValue answerValue, Set<FolFormula> justifications) {
		super(sender, receiver);
		this.proposition = proposition;
		this.answerValue = answerValue;
		this.justifications = justifications;
	}

	public FolFormula getProposition() {
		return proposition;
	}
	
	public Set<FolFormula> getJustifications() {
		return Collections.unmodifiableSet(justifications);
	}
	
	public AnswerValue getAnswerValue() {
		return answerValue;
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " justification " + getReceiverId() + " " 
					+ proposition + " " + answerValue + " " + justifications + " >";
	}
}
