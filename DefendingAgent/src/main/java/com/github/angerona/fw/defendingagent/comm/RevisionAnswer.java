package com.github.angerona.fw.defendingagent.comm;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;

/**
 * Implementation of the speech act "RevisionAnswer".
 * Based on speech act "Answer"
 * @author Sebastian Homann, Tim Janus
 */
public class RevisionAnswer extends Answer {

	/**
	 * Ctor: Generates the Answer speech act by the following parameters.
	 * @param sendingAgent	reference to the sender of the answer
	 * @param receiverId	unique name of the receiver of the answer
	 * @param regarding		formula representing the question
	 * @param answer		the value of the answer.
	 */
	public RevisionAnswer(Agent sendingAgent, String receiverId, 
			FolFormula regarding, AngeronaAnswer answer) {
		super(sendingAgent, receiverId, regarding, answer);
	}
	
	public RevisionAnswer(Agent sendingAgent, String receiverId, FolFormula regarding, AnswerValue av) {
		super(sendingAgent, receiverId, regarding, new AngeronaAnswer(regarding, av));
	}
	
	public RevisionAnswer(Agent sendingAgent, String receiverId, FolFormula regarding, FolFormula answer) {
		super(sendingAgent, receiverId, regarding, new AngeronaAnswer(regarding, answer));
	}
	
	public RevisionAnswer(Agent sendingAgent, String receiverId, FolFormula regarding, Set<FolFormula> answers) {
		super(sendingAgent, receiverId, regarding, new AngeronaAnswer(regarding, answers));
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " answers " + getReceiverId() + "'s revision request " + getRegarding().toString() + "=" + getAnswer().toString() + " >";
	}

}
