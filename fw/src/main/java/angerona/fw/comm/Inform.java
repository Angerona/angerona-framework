package angerona.fw.comm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * Implementation of the speech act "Inform"
 * The sender informs the receiver about his knowledge with the intention
 * that the receiver revise its own knowledge using the information of the
 * Inform speech act.
 * For further details see Definition 21 in 
 * "Angerona - A Multiagent Framework for Logic Based Agents". (DRAFT)
 * @author Tim Janus
 */
public class Inform extends SpeechAct
{
	/** the formula for revision */
	private Set<FolFormula> sentences = new HashSet<FolFormula>();
	
	/**
	 * Ctor: Creates an Inform speech act with one piece of information.
	 * @param sender	The name of the sender
	 * @param receiver	The name of the receiver
	 * @param sentence	The piece of information as FOL formula.
	 */
	public Inform(String sender, String receiver, FolFormula sentence) {
		super(sender, receiver);
		this.sentences.add(sentence);
	}	
	
	/**
	 * Ctor: Creates an Inform speech act with a set of FOL formulas
	 * @param sender	The name of the sender
	 * @param receiver	The name of the receiver
	 * @param sentences	The set containg multiple pieces of information as FOL
	 * 					formulas.
	 */
	public Inform(String sender, String receiver, Set<FolFormula> sentences) {
		super(sender, receiver);
		this.sentences.addAll(sentences);
	}
	
	/** @return the formula for revision */
	public Set<FolFormula> getSentences() {
		return Collections.unmodifiableSet(sentences);
	}
	
	@Override 
	public String toString() {
		return "< " + this.getSenderId() + " revision-request " + this.getReceiverId() + " " + sentences + " >";
	}
}
