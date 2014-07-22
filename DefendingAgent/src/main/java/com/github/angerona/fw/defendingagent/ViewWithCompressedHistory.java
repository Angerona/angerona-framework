package com.github.angerona.fw.defendingagent;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.angerona.fw.util.LogicTranslator;

public class ViewWithCompressedHistory extends ViewForBeliefbaseWithKnowledge{

	private Set<PLWithKnowledgeBeliefbase> possibleBeliefbases = new HashSet<>();


	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViewWithCompressedHistory.class);
	
	
	public ViewWithCompressedHistory(PLWithKnowledgeBeliefbase beliefbase) {
		super(beliefbase);
	}
	
	/**
	 * Default C'tor. Empty view.
	 */
	public ViewWithCompressedHistory() {
		this(new PLWithKnowledgeBeliefbase());
	}
		
	/**
	 * Copy C'tor
	 * @param other
	 */
	public ViewWithCompressedHistory(ViewWithCompressedHistory other) {
		this.view = new PLWithKnowledgeBeliefbase(other.view);
	}
	
	public void calculatePossibleBeliefbases(CompressedHistory history){
		//TODO implement
		
	}	
	
	/**
	 * View Refinement: calculates the resulting view after the application of an update with 
	 * a presumed result notification.
	 * @param update An update action
	 * @param notification a fixed success/failure notification
	 * @return the resulting view after the consideration of the revision and the notification
	 */
	public ViewWithCompressedHistory RefineViewByUpdate(FolFormula update, AnswerValue notification) {
		
		PropositionalFormula q = LogicTranslator.FoToPl(update);
		ViewWithCompressedHistory newView = new ViewWithCompressedHistory(this);
		if(notification == AnswerValue.AV_TRUE) {
			newView.getView().getAssertions().addLast(q);
		} else if(notification == AnswerValue.AV_FALSE) {
			newView.getView().getAssertions().addLast(q);
		} else {
			LOG.warn("unexpected answer value in update view refinement: " + notification);
		}
		return newView;
	}
	
	public String toString() {
		String retval = "View: \n" + this.view.toString() + "Possible Bleifases: ";
		for(PLWithKnowledgeBeliefbase base : possibleBeliefbases){
			retval += "\n" + base.toString();
		}
		return retval;
	}
	
	public Set<PLWithKnowledgeBeliefbase> getPossibleBeliefbases(){
		return possibleBeliefbases;
	}
		
}
