package angerona.fw.DefendingAgent.comm;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Agent;
import angerona.fw.comm.Answer;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;

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
		super(sendingAgent, receiverId, regarding, new AngeronaAnswer(null, regarding, av));
	}
	
	public RevisionAnswer(Agent sendingAgent, String receiverId, FolFormula regarding, FolFormula answer) {
		super(sendingAgent, receiverId, regarding, new AngeronaAnswer(null, regarding, answer));
	}
	
	public RevisionAnswer(Agent sendingAgent, String receiverId, FolFormula regarding, Set<FolFormula> answers) {
		super(sendingAgent, receiverId, regarding, new AngeronaAnswer(null, regarding, answers));
	}

}
