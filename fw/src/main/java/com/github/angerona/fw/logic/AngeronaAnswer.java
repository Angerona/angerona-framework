package com.github.angerona.fw.logic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * An answer in the Angerona framework. Extends the GenericAnswer by an extended 
 * answer enumeration with five possibles values: {true, false, unknown, reject, complex}
 * Closed queries for a literal receive an answer containing one of the first four answer
 * values (true, false, unknown, reject). If the answer value is complex then the answer
 * contains a set of literals which define the answer. Complex is used to answer open queries
 * like "isDeveloper(X)"
 * 
 * @see angerona.fw.logic.AnswerValue
 * @author Tim Janus
 * @author Daniel Dilger
 */
public class AngeronaAnswer {

	/** representation of the answer as a enumeration (true,false,unknown,reject or complex) */
	private AnswerValue answerValue;

	/** sets of answers used for complex answers (for open queries) */
	private Set<FolFormula> answers;
	
	/**
	 * The original query for this answer.
	 */
	private Formula query;
	
	/**
	 * Ctor: Generates an answer for the given query on the given belief base with the default answer-value false.
	 * @param query			the formula representing the query.
	 */
	public AngeronaAnswer(FolFormula query) {
		this(query, AnswerValue.AV_FALSE);
	}
	
	/**
	 * Ctor: Generates an answer for the given query on the given belief base with the given answer-value.
	 * @param query			the formula representing the query.
	 * @param av			the AnswerValue representing the value of the Answer.
	 */
	public AngeronaAnswer(FolFormula query, AnswerValue av) {
		setAnswer(av);
		this.query = query;
	}
	
	public AngeronaAnswer(FolFormula query, FolFormula answer) {
		Set<FolFormula> answers = new HashSet<>();
		answers.add(answer);
		setAnswer(answers);
		this.query = query;
	}
	
	public AngeronaAnswer(FolFormula query, Set<FolFormula> formulas) {
		setAnswer(formulas);
		this.query = query;
	}
	
	public void setAnswer(AnswerValue av) {
		if(av == AnswerValue.AV_COMPLEX) {
			throw new IllegalArgumentException("Use the setAnswer method with the formula set as parameter for complex answer-values.");
		}
		this.answerValue = av;
	}
	
	public void setAnswer(Set<FolFormula> formulas) {
		if(formulas == null) {
			throw new IllegalArgumentException("Parameter formulas must not be null.");
		}
		answers = new HashSet<>(formulas);
		this.answerValue = AnswerValue.AV_COMPLEX;
	}
	
	/**
	 * Returns the query this answer relates to.
	 * @return the query this answer relates to.
	 */
	public Formula getQuery(){
		return this.query;
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
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof AngeronaAnswer))
			return false;
		
		AngeronaAnswer aa = (AngeronaAnswer)other;
		if(this.answerValue != aa.answerValue)
			return false;
		
		if(this.answerValue == AnswerValue.AV_COMPLEX) {
			if(!this.answers.equals(aa.answers))
				return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return (answerValue.hashCode() + 
				(answers == null ? 0 : answers.hashCode())) * 5;
	}
	
	@Override
	public String toString() {
		if(answerValue == AnswerValue.AV_COMPLEX) {
			return answers.toString();
		} else {
			return answerValue.toString();
		}
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
