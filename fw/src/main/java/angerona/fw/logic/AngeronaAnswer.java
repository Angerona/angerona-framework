package angerona.fw.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * An answer in the Angerona framework. Extends the GenericAnswer by an extended 
 * answer enumeration with four possibles values: {true, false, unknown, reject}
 * @see angerona.fw.logic.AnswerValue
 * @author Tim Janus, Daniel Dilger
 */
public class AngeronaAnswer extends Answer {

	/** representation of the answer as a enumeration (true,false,unknown,reject or complex) */
	private AnswerValue answerValue;

	/** sets of answers used for complex answers (for open queries) */
	private Set<FolFormula> answers;
	
	/**
	 * Ctor: Generates an answer for the given query on the given belief base with the default answer-value false.
	 * @param beliefBase	the belief base used for performing the query
	 * @param query			the formula representing the query.
	 */
	public AngeronaAnswer(BeliefBase beliefBase, FolFormula query) {
		this(beliefBase, query, AnswerValue.AV_FALSE);
	}
	
	/**
	 * Ctor: Generates an answer for the given query on the given belief base with the given answer-value.
	 * @param beliefBase	the belief base used for performing the query.
	 * @param query			the formula representing the query.
	 * @param av			the AnswerValue representing the value of the Answer.
	 */
	public AngeronaAnswer(BeliefBase beliefBase, FolFormula query, AnswerValue av) {
		super(beliefBase, query);
		setAnswer(av);
	}
	
	public AngeronaAnswer(BeliefBase beliefBase, FolFormula query, Set<FolFormula> formulas) {
		super(beliefBase, query);
		setAnswer(formulas);
	}
	
	/** helper method: Sets the other value types (boolean and double) */
	private void updateValues(boolean internal) {
		this.setAnswer(internal);
		this.setAnswer(internal ? 1.0 : 0.0);
	}
	
	public void setAnswer(AnswerValue av) {
		if(av == AnswerValue.AV_COMPLEX) {
			throw new IllegalArgumentException("Use the setAnswer method with the formula set as parameter for complex answer-values.");
		}
		this.answerValue = av;
		updateValues(av == AnswerValue.AV_TRUE);
	}
	
	public void setAnswer(Set<FolFormula> formulas) {
		if(formulas == null) {
			throw new IllegalArgumentException("Parameter formulas must not be null.");
		}
		answers = new HashSet<>(formulas);
		updateValues(!formulas.isEmpty());
	}
	
	public FolFormula getQueryFOL() {
		return (FolFormula)this.getQuery();
	}
	
	
	public Set<FolFormula> getAnswers() {
		return Collections.unmodifiableSet(answers);
	}
	
	public AnswerValue getAnswerValue() {
		return answerValue;
	}

	public static AnswerValue valueOf(String s) {
		if(s.compareToIgnoreCase("true") == 0) {
			return AnswerValue.AV_TRUE;
		} else if(s.compareToIgnoreCase("false") == 0) {
			return AnswerValue.AV_FALSE;
		} else if(s.compareToIgnoreCase("unknown") == 0) {
			return AnswerValue.AV_UNKNOWN;
		} else if(s.compareToIgnoreCase("reject") == 0) {
			return AnswerValue.AV_REJECT;
		} else if(s.compareToIgnoreCase("complex") == 0){
			return AnswerValue.AV_COMPLEX;
		}
		return null;
	}
	
	/** @return a set of AnswerExtensions only containing true. */
	public static Set<AnswerValue> getTAnswerSet() {
		Set<AnswerValue> reval = new HashSet<AnswerValue>();
		reval.add(AnswerValue.AV_TRUE);
		return reval;
	}
	
	/** @return a set of AnswerExtensions containing true and false */
	public static Set<AnswerValue> getTFAnswerSet() {
		Set<AnswerValue> reval = new HashSet<AnswerValue>();
		reval.add(AnswerValue.AV_TRUE);
		reval.add(AnswerValue.AV_FALSE);
		return reval;
	}
	
	/** @return a set of AnswerExtensions containing true and unknown */
	public static Set<AnswerValue> getTUAnswerSet() {
		Set<AnswerValue> reval = new HashSet<AnswerValue>();
		reval.add(AnswerValue.AV_TRUE);
		reval.add(AnswerValue.AV_UNKNOWN);
		return reval;
	}
	
	/** @return a set of AnswerExtensions only containing false */
	public static Set<AnswerValue> getFAnswerSet() {
		Set<AnswerValue> reval = new HashSet<AnswerValue>();
		reval.add(AnswerValue.AV_FALSE);
		return reval;
	}
	
	/** @return a set of AnswerExtensions containing false and unknown */
	public static Set<AnswerValue> getFUAnswerSet() {
		Set<AnswerValue> reval = new HashSet<AnswerValue>();
		reval.add(AnswerValue.AV_FALSE);
		reval.add(AnswerValue.AV_UNKNOWN);
		return reval;
	}
	
	/** @return a set of AnswerExtensions only containing unknown */
	public static Set<AnswerValue> getUAnswerSet() {
		Set<AnswerValue> reval = new HashSet<AnswerValue>();
		reval.add(AnswerValue.AV_UNKNOWN);
		return reval;
	}
}
