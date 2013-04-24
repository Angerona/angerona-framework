package angerona.fw.defendingagent;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Contradiction;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.logic.AnswerValue;

/**
 * Represents the defending agents approximation of the attacking agents view
 * on the defending agents beliefs. This approximation consists of three parts:
 * 1. A set of propositions, which the defending agent beliefs.
 * 2. A set of conditional beliefs of the form "A -> B", which the defending agent believe
 * 3. A set of conditional beliefs, which the defending agent does not believe
 * 
 * @author Sebastian Homann, Pia Wierzoch
 *
 */
public class View {
	private PlBeliefSet beliefSet;
	private Set<Conditional> positiveConditionalBeliefs;
	private Set<Conditional> negativeConditionalBeliefs;
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(View.class);
	
	/**
	 * C'tor, starts with a set of beliefs and empty conditional sets.
	 * @param beliefSet set of beliefs
	 */
	public View(PlBeliefSet beliefSet) {
		this.beliefSet = beliefSet;
		positiveConditionalBeliefs = new HashSet<Conditional>();
		negativeConditionalBeliefs = new HashSet<Conditional>();
	}
	
	/**
	 * Default C'tor. Empty belief set and conditional sets.
	 */
	public View() {
		this(new PlBeliefSet());
	}
	
	/**
	 * Copy C'tor
	 * @param other
	 */
	public View(View other) {
		this.beliefSet = new PlBeliefSet(other.beliefSet);
		this.positiveConditionalBeliefs = new HashSet<Conditional>(other.positiveConditionalBeliefs);
		this.negativeConditionalBeliefs = new HashSet<Conditional>(other.negativeConditionalBeliefs);
	}
	
	/**
	 * View refinement: calculates the resulting view after a given query with a certain answer value.
	 * @param query A query action 
	 * @param av a fixed answer value
	 * @return the resulting view after the consideration of the query and the answer value
	 */
	public View RefineViewByQuery(FolFormula query, AnswerValue av) {
		PropositionalFormula q = LogicTranslator.FoToPl(query);
		View newView = new View(this);
		
		if(av == AnswerValue.AV_TRUE) {
			Conjunction conj = new Conjunction(beliefSet);
			Conditional cond = new Conditional(conj, q);
			newView.positiveConditionalBeliefs.add(cond);
		} else if(av == AnswerValue.AV_FALSE) {
			Conjunction conj = new Conjunction(beliefSet);
			Conditional cond = new Conditional(conj, new Negation(q));
			newView.positiveConditionalBeliefs.add(cond);
		} else if(av == AnswerValue.AV_UNKNOWN) {
			Conjunction conj = new Conjunction(beliefSet);
			Conditional cond = new Conditional(conj, q);
			newView.negativeConditionalBeliefs.add(cond);
			conj = new Conjunction(beliefSet);
			cond = new Conditional(conj, new Negation(q));
			newView.negativeConditionalBeliefs.add(cond);
		} else {
			LOG.warn("unexpected answer value in query view refinement: " + av);
		}
		return newView;
	}
	
	/**
	 * View Refinement: calculates the resulting view after the application of a revision with 
	 * a presumed result notification.
	 * @param revision A revision action
	 * @param notification a fixed success/failure notification
	 * @return the resulting view after the consideration of the revision and the notification
	 */
	public View RefineViewByRevision(FolFormula revision, AnswerValue notification) {
		PropositionalFormula q = LogicTranslator.FoToPl(revision);
		View newView = new View(this);
		if(notification == AnswerValue.AV_TRUE) {
			Conjunction conj = new Conjunction(beliefSet);
			conj.add(q);
			Conditional cond = new Conditional(conj, new Contradiction());
			newView.negativeConditionalBeliefs.add(cond);
			newView.beliefSet.add(q);
		} else if(notification == AnswerValue.AV_FALSE) {
			Conjunction conj = new Conjunction(beliefSet);
			conj.add(q);
			Conditional cond = new Conditional(conj, new Contradiction());
			newView.positiveConditionalBeliefs.add(cond);
		} else {
			LOG.warn("unexpected answer value in revision view refinement: " + notification);
		}
		
		return newView;
	}
	
	public String toString() {
		return "(" + positiveConditionalBeliefs.toString() + ", " + negativeConditionalBeliefs.toString() + ", "+ beliefSet.toString() + ")";
	}
	
	public PlBeliefSet getBeliefSet() {
		return beliefSet;
	}

	public Set<Conditional> getPositiveConditionalBeliefs() {
		return positiveConditionalBeliefs;
	}

	public Set<Conditional> getNegativeConditionalBeliefs() {
		return negativeConditionalBeliefs;
	}
	
}
