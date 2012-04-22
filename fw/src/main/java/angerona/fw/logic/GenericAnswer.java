package angerona.fw.logic;

import net.sf.tweety.BeliefBase;
import net.sf.tweety.Formula;

/**
 * A generic extension of the tweety answer. The user can define an own type T
 * which contains an extended version of the answer. 
 * As an example: T could be a class representing multiple answer sets which were
 * obtained by answer set programming.
 * @author Tim Janus
 *
 * @param <T>	Type of the extended answer member.
 */
public class GenericAnswer<T> extends net.sf.tweety.Answer {
	
	/** the extended answer */
	protected T answerExtended;
	
	/**
	 * Ctor: 	Generates an answer without setting the extended answer (it's null then)
	 * @param beliefBase	the belief base used for performing the query.
	 * @param query			the formula representing the query.
	 */
	public GenericAnswer(BeliefBase beliefBase, Formula query) {
		super(beliefBase, query);
	}
	
	/**
	 * Ctor: 	Generates an answer for the given query on the given belief base with the given 
	 * 			answer-value for the extended answer member.
	 * @param beliefBase	the belief base used for performing the query.
	 * @param query			the formula representing the query.
	 * @param at			the AnswerExtension representing the value of the Answer.
	 */
	public GenericAnswer(BeliefBase beliefBase, Formula query, T at) {
		super(beliefBase, query);
		answerExtended = at;
	}
	
	/**
	 * Changes the value of the answer to the given parameter.
	 * @param answer	the new value of the answer.
	 */
	public void setAnswerExtended(T answer) {
		answerExtended = answer;
	}
	
	/** @return the value of the answer */
	public T getAnswerExtended() {
		return answerExtended;
	}
}
