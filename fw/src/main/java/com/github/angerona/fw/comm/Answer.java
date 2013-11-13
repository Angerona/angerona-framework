package com.github.angerona.fw.comm;

import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.reflection.Context;
import com.github.angerona.fw.util.Utility;

/**
 * Implementation of the speech act "Answer".
 * @author Tim Janus
 */
public class Answer extends SpeechAct {

	/** the formula representing the question */
	private FolFormula regarding;
	
	/** the object containing information about the answer. */
	private AngeronaAnswer answer;
	
	/**
	 * Ctor: Generates the Answer speech act by the following parameters.
	 * @param sendingAgent	reference to the sender of the answer
	 * @param receiverId	unique name of the receiver of the answer
	 * @param regarding		formula representing the question
	 * @param answer		the value of the answer.
	 */
	public Answer(Agent sendingAgent, String receiverId, 
			FolFormula regarding, AngeronaAnswer answer) {
		super(sendingAgent, receiverId);
		this.regarding = regarding;
		this.answer = answer;
	}
	
	public Answer(Agent sendingAgent, String receiverId, FolFormula regarding, AnswerValue av) {
		this(sendingAgent, receiverId, regarding, new AngeronaAnswer(regarding, av));
	}
	
	public Answer(Agent sendingAgent, String receiverId, FolFormula regarding, FolFormula answer) {
		this(sendingAgent, receiverId, regarding, new AngeronaAnswer(regarding, answer));
	}
	
	public Answer(Agent sendingAgent, String receiverId, FolFormula regarding, Set<FolFormula> answers) {
		this(sendingAgent, receiverId, regarding, new AngeronaAnswer(regarding, answers));
	}

	/** @return the formula representing the question */
	public FolFormula getRegarding() {
		return regarding;
	}
	
	public void setAnswer(AngeronaAnswer val) {
		answer = val;
	}
	
	/** @return the value of the answer */
	public AngeronaAnswer getAnswer() {
		return answer;
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " answers " + getReceiverId() + " " + regarding.toString() + "=" + answer.toString() + " >";
	}
	
	@Override 
	public boolean equals(Object obj)
	{
		if(! (obj instanceof Answer))	return false;
	
		Answer ans = (Answer) obj;
		if(!super.equals(obj)) 	return false;
		
		if(!Utility.equals(regarding, ans.regarding)) {
			return false;
		}
		if(!Utility.equals(answer, ans.answer)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public int hashCode() {
		return (super.hashCode() + regarding.hashCode() + answer.hashCode()) * 3;
	}
	
	@Override 
	public Context getContext() {
		Context reval = super.getContext();
		AngeronaAnswer answer = getAnswer();
		if(answer.getAnswerValue() == AnswerValue.AV_COMPLEX) {
			// TODO: Support more than one answer
			reval.set("answer", answer.getAnswers().iterator().next());
		} else {
			reval.set("answer", answer.getAnswerValue());
		}
		return reval;
	}

	@Override
	public SpeechActType getType() {
		return SpeechActType.SAT_INFORMATIVE;
	}
}
