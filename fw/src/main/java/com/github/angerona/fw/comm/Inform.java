package com.github.angerona.fw.comm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.util.Utility;

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
	@ElementList(name="sentences", entry="sentence", required=true)
	private Set<FolFormula> sentences = new HashSet<FolFormula>();
	
	/** Ctor used by deserilization */
	public Inform(
			@Element(name="sender") String sender, 
			@Element(name="receiver") String receiver, 
			@ElementList(name="sentences") Set<FolFormula> sentences) {
		super(sender, receiver);
		this.sentences = sentences; 
	}
	
	/**
	 * Ctor: Creates an Inform speech act with one piece of information.
	 * @param sender	The reference to the sendeing agent
	 * @param receiver	The name of the receiver
	 * @param sentence	The piece of information as FOL formula.
	 */
	public Inform(Agent sender, String receiver, FolFormula sentence) {
		super(sender, receiver);
		this.sentences.add(sentence);
	}	
	
	/**
	 * Ctor: Creates an Inform speech act with a set of FOL formulas
	 * @param sender	The reference to the sendeing agent
	 * @param receiver	The name of the receiver
	 * @param sentences	The set containg multiple pieces of information as FOL
	 * 					formulas.
	 */
	public Inform(Agent sender, String receiver, Set<FolFormula> sentences) {
		super(sender, receiver);
		this.sentences.addAll(sentences);
	}
	
	/** @return the formula for revision */
	public Set<FolFormula> getSentences() {
		return Collections.unmodifiableSet(sentences);
	}
	
	@Override 
	public String toString() {
		return "< " + this.getSenderId() + " inform " + this.getReceiverId() + " " + sentences + " >";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(! (obj instanceof Inform))
			return false;
		
		if(!super.equals(obj))
			return false;
		
		return Utility.equals(sentences, ((Inform)obj).sentences);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + sentences.hashCode() * 5;
	}

	@Override
	public SpeechActType getType() {
		return SpeechActType.SAT_INFORMATIVE;
	}
}
