package com.github.angerona.fw.bargainingAgent.comm;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.simpleframework.xml.Element;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.bargainingAgent.util.Tupel;
import com.github.angerona.fw.comm.SpeechAct;

/**
 * Implementation of the Speech-Act: Offer.
 * A Offer speech act represents a offer by an agent.
 * It contains a pair of sets of literals.
 * @author Pia Wierzoch
 */
public class Offer extends SpeechAct {
	
	
	/** formula representing the question of the query */
	@Element(name="demandsAndPromises", required=true)
	private Tupel<HashSet<FolFormula>,HashSet<FolFormula>> demandsAndPromises;
	
	/** Ctor used by deserilization */
	public Offer(	@Element(name="sender") String sender, 
					@Element(name="receiver") String receiver, 
					@Element(name="demandsAndPromises") Tupel<HashSet<FolFormula>, HashSet<FolFormula>> demandsAndPromises2) {
		super(sender, receiver);
		this.demandsAndPromises = demandsAndPromises2;
	}
	
	/**
	 * Ctor: generating the offer speech act with the following parameters.
	 * @param sender				The reference to the sendeing agent
	 * @param receiverId			unique name  of the receiver of the query.
	 * @param demandsAndPromises	the tuple representing the demands and promises of the offer.
	 */
	public Offer(Agent sender, String receiverId, Tupel<HashSet<FolFormula>,HashSet<FolFormula>> demandsAndPromises) {
		super(sender, receiverId);
		this.demandsAndPromises = demandsAndPromises;
	}
	
	public Offer(Offer offer) {
		super(offer.getSenderId(), offer.getReceiverId());
		HashSet<FolFormula> demands = new HashSet<>(offer.getDemandsAndPromises().getFirst()), promises = new HashSet<>(offer.getDemandsAndPromises().getLast());;
		demandsAndPromises = new Tupel<HashSet<FolFormula>, HashSet<FolFormula>>(demands, promises);
	}

	/** @return tuple representing the demands and promises of the offer */
	public Tupel<HashSet<FolFormula>,HashSet<FolFormula>> getDemandsAndPromises() {
		return demandsAndPromises;
	}
	
	@Override 
	public String toString() {
		return "< " + getSenderId() + " offer " + getReceiverId() + " " + demandsAndPromises + " >";
	}


	@Override
	public SpeechActType getType() {
		return SpeechActType.SAT_INFORMATIVE;
	}

	@Override
	public Set<FolFormula> getContent() {
		Set<FolFormula> reval = new HashSet<>();
		reval.addAll(getDemandsAndPromises().getFirst());
		reval.addAll(getDemandsAndPromises().getLast());
		return reval;
	}
}
