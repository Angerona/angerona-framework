package com.github.kreatures.defendingagent;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.logic.AnswerValue;
import com.github.kreatures.plwithknowledge.logic.PLWithKnowledgeBeliefbase;
import com.github.kreatures.core.util.LogicTranslator;

public class ViewWithHistory extends ViewForBeliefbaseWithKnowledge{

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ViewWithHistory.class);
	
	
	public ViewWithHistory(PLWithKnowledgeBeliefbase view) {
		super(view);
	}
	
	/**
	 * Default C'tor. Empty view.
	 */
	public ViewWithHistory() {
		this(new PLWithKnowledgeBeliefbase());
	}
	
	/**
	 * Copy C'tor
	 * @param other
	 */
	public ViewWithHistory(ViewWithHistory other) {
		this.view = new PLWithKnowledgeBeliefbase(other.view);
	}
	
	/**
	 * View refinement: calculates the resulting view after a given query with a certain answer value.
	 * @param query A query action 
	 * @param av a fixed answer value
	 * @return the resulting view after the consideration of the query and the answer value
	 */
	public ViewWithHistory RefineViewByQuery(FolFormula query, AnswerValue av) {
		PropositionalFormula q = LogicTranslator.FoToPl(query);
		ViewWithHistory newView = new ViewWithHistory(this);
		
		if(av == AnswerValue.AV_TRUE) {
			newView.getView().getAssertions().addLast(q);
		} else if(av == AnswerValue.AV_FALSE) {
			newView.getView().getAssertions().addLast(new Negation(q));
		} else if(av == AnswerValue.AV_UNKNOWN) {
			//do nothing
		} else {
			LOG.warn("unexpected answer value in query view refinement: " + av);
		}
		
		return newView;
	}
	
	/**
	 * View Refinement: calculates the resulting view after the application of an update with 
	 * a presumed result notification.
	 * @param update An update action
	 * @param notification a fixed success/failure notification
	 * @return the resulting view after the consideration of the revision and the notification
	 */
	public ViewWithHistory RefineViewByUpdate(FolFormula revision, AnswerValue notification) {
		
		PropositionalFormula q = LogicTranslator.FoToPl(revision);
		ViewWithHistory newView = new ViewWithHistory(this);
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
		return "View: \n" + this.view.toString();
	}
}
