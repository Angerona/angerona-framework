package angerona.fw.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * An answer in the Angerona framework. Extends the GenericAnswer by an extended 
 * answer enumeration with four possibles values: {true, false, unknown, reject}
 * @see angerona.fw.logic.AnswerValue
 * @author Tim Janus
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
	public AngeronaAnswer(BeliefBase beliefBase, Formula query) {
		this(beliefBase, query, AnswerValue.AV_FALSE);
	}
	
	/**
	 * Ctor: Generates an answer for the given query on the given belief base with the given answer-value.
	 * @param beliefBase	the belief base used for performing the query.
	 * @param query			the formula representing the query.
	 * @param at			the AnswerValue representing the value of the Answer.
	 */
	public AngeronaAnswer(BeliefBase beliefBase, Formula query, AnswerValue at) {
		super(beliefBase, query);
		this.answerValue = at;
		if(at == AnswerValue.AV_COMPLEX) {
			answers = new HashSet<>();
		}
		this.setAnswer(at == AnswerValue.AV_TRUE ? true : false);
		this.setAnswer(at == AnswerValue.AV_TRUE ? 1.0 : 0.0);
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
