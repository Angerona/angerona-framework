package com.github.angerona.fw.bargainingAgent;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.bargainingAgent.comm.Offer;
import com.github.angerona.fw.bargainingAgent.util.Tupel;

/**
 * Stores the history of the interaction between two agents. It's a sequenz of tuples with an offer and the corresponding goals.
 * @author Pia Wierzoch
 */
public class HistoryComponent extends BaseAgentComponent{
	
	private LinkedList<Tupel<Offer, TreeMap<Integer, FolFormula>>> history = new LinkedList<Tupel<Offer, TreeMap<Integer, FolFormula>>>();
	
	/** Default Ctor: Used for dynamic creation, creates empty history */
	public HistoryComponent() {}
	
	/** Copy Ctor */
	public HistoryComponent(HistoryComponent other) {
		super(other);
	}
		
	
	public void putEntry(Offer offer, TreeMap<Integer, FolFormula> goals) {
		history.add(new Tupel<Offer, TreeMap<Integer, FolFormula>>(offer, goals));
	}
	
	public LinkedList<Tupel<Offer, TreeMap<Integer, FolFormula>>> getHistory(){
		return history;
	}
	
	public String toString(){
		String reval = "History:";
		for(Tupel<Offer, TreeMap<Integer, FolFormula>> entry : history){
			reval += "<" + entry.getFirst() + ", " + entry.getLast() + ">";
		}
		return reval;
	}
	
	@Override
	public BaseAgentComponent clone() {
		return new HistoryComponent(this);
	}
	
	@Override
	public void init(Map<String, String> additionalData) {

	}
	
}
