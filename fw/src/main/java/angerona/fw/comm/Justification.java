package angerona.fw.comm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.logic.AnswerValue;
import angerona.fw.util.Utility;

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
	@Element(name="proposition", required=true)
	private FolFormula proposition;
	
	/** the answer to the formula: only {true, false, unknown} are allowed. */
	@Element(name="answerValue", required=true)
	private AnswerValue answerValue;
	
	/** a set of fol formulas explaining the deduction */
	@ElementList(name="justifications", required=true, entry="justification")
	private Set<FolFormula> justifications = new HashSet<>();
	
	public Justification(Justify source, FolFormula justification) {
		this(Angerona.getInstance().getActualSimulation().getAgentByName(
				source.getReceiverId()),
				source.getSenderId(), source.getProposition(), 
				source.getAnswerValue(), justification);
	}
	
	/** Ctor used by deserilization */
	public Justification(
			@Element(name="sender") String sender,
			@Element(name="receiver") String receiver,
			@Element(name="proposition") FolFormula proposition,
			@Element(name="answerValue") AnswerValue answerValue,
			@Element(name="justification") FolFormula justification) {
		super(sender,receiver);
		this.proposition = proposition;
		this.answerValue = answerValue;
		this.justifications.add(justification);		
	}
	
	public Justification(Agent sender, String receiver, FolFormula proposition, 
			AnswerValue answerValue, FolFormula justification) {
		super(sender,receiver);
		this.proposition = proposition;
		this.answerValue = answerValue;
		this.justifications = new HashSet<>();
		this.justifications.add(justification);
	}
	
	public Justification(Justify source, Set<FolFormula> justifications) {
		this(Angerona.getInstance().getActualSimulation().getAgentByName(
				source.getReceiverId()), source.getSenderId(), source.getProposition(), 
				source.getAnswerValue(), justifications);
	}
	
	public Justification(Agent sender, String receiver, FolFormula proposition, 
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
	public boolean equals(Object obj) {
		if(! (obj instanceof Justification))
			return false;
		
		if(! super.equals(obj)) 
			return false;
		
		Justification oj = (Justification)obj;
		
		return 	answerValue == oj.answerValue &&
				Utility.equals(proposition, oj.proposition) &&
				Utility.equals(justifications, oj.justifications);
				
	}
	
	@Override
	public int hashCode() {
		return ( super.hashCode() + answerValue.hashCode() + 
				proposition.hashCode() + justifications.hashCode()) * 7;
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " justification " + getReceiverId() + " " 
					+ proposition + " " + answerValue + " " + justifications + " >";
	}
}
