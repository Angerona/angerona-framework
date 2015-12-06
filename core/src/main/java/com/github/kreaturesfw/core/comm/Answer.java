package com.github.kreaturesfw.core.comm;

import java.util.HashSet;
import java.util.Set;

import com.github.kreaturesfw.core.basic.Agent;
import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.reflection.Context;
import com.github.kreaturesfw.core.util.Utility;

import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * An {@link Answer} is an informative speech-act thats is returned to a {@link Query} for
 * example.
 * 
 * Answer is special because most of the speech-acts store their entire semantic in their type name
 * but the {@link Answer} speech-act also stores an {@link AngeronaAnswer} containing an {@link AnswerValue}, 
 * which can have four different values and give the answer another semantic. AV_TRUE and AV_FALSE represent 
 * the boolean true or false for an answer regarding the given question. AV_REJECT means the agent rejects to
 * give an answer, AV_UNKNOWN means the agent does not know the answer and AV_COMPLEX means the answer contains
 * multiple first order formulas. That can happen if a query is open, like brother(X), which might return multiple
 * brothers.
 * 
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

	@Override
	public Set<FolFormula> getContent() {
		FolFormula formula = null;
		
		if(answer.getAnswerValue() == AnswerValue.AV_COMPLEX) {
			return answer.getAnswers();
		} else if(answer.getAnswerValue() == AnswerValue.AV_FALSE) {
			formula = (FolFormula)regarding.complement() ;
		} else {
			formula = regarding;
		}
		
		Set<FolFormula> reval = new HashSet<>();
		reval.add(formula);
		return reval;
	}
}
