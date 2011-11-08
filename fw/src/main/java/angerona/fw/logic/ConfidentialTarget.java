package angerona.fw.logic;

import java.util.Set;

import net.sf.tweety.Formula;

/**
 * A confidential target as defined in Def 3. in 
 * "Torwards Enforcement of Confidentially in Agent Interactions"
 * by Biskup, Kern-Isberner, Thimm
 * 
 * @author Tim Janus
 */
public class ConfidentialTarget implements Cloneable {
	/** name of the agent who should not get the information */
	private String name;
	
	/** formula representing the confidential information */
	private Formula information;
	
	/** set of answers which aren't allowed (the subject should never belief one of the answers). */
	private Set<AnswerValue> forbiddenAnswersSet;
	
	/**
	 * Ctor: Generates a new confidential target with the given parameters.
	 * @param name					name of the agent who should not get the information
	 * @param information			formula representing the confidential information
	 * @param forbiddenAnswersSet	set of answers which aren't allowed (the subject should never belief one of the answers)
	 */
	public ConfidentialTarget(String name, Formula information, Set<AnswerValue> forbiddenAnswersSet) {
		this.name = name;
		this.information = information;
		this.forbiddenAnswersSet = forbiddenAnswersSet;
	}
	
	/**	@return name of the agent who should not get the information */
	public String getSubjectName() {
		return name;
	}
	
	/** @return formula representing the confidential information */
	public Formula getInformation() {
		return information;
	}
	
	/**
	 * Proofs if the confidential target's forbidden set contains the given answer
	 * @param a	reference to an answer instance.
	 * @return	true if the answer is in the set of forbidden answers, false otherwise.
	 */
	public boolean contains(AngeronaAnswer a) {
		return contains(a.getAnswerExtended());
	}
	
	/**
	 * Proofs if the confidential target's forbidden set contains the given answer-value 
	 * @param av	answer-value
	 * @return		true if the given answer-value is in the set of forbidden answers, false otherwise.
	 */
	public boolean contains(AnswerValue av) {
		return forbiddenAnswersSet.contains(av);
	}
	
	@Override
	public Object clone() {
		return new ConfidentialTarget(name, information, forbiddenAnswersSet);
	}
	
	@Override
	public String toString() {
		return "(" + name + ", " + information + ", " + forbiddenAnswersSet + ")";
	}
}
