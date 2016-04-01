package com.github.kreatures.example.components;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.ActionHistory;
import com.github.kreatures.core.comm.Answer;
import com.github.kreatures.core.logic.AnswerValue;

/**
 * This class implements an action history specific for communcation.
 * It assumes that an agent does not forget an answer and therefore
 * does not have to ask a query multiple times or give an answer 
 * multiple times but the agent can give the answers: 'reject' and 
 * 'unknown' multiple times.
 * 
 * @author Tim Janus
 */
public class CommunicationHistory extends ActionHistory {
	@Override
	public void putAction(Action action) {
		boolean exception = false;
		if(action instanceof Answer) {
			Answer ans = (Answer)action;
			exception = ans.getAnswer().getAnswerValue() == AnswerValue.AV_REJECT ||
						ans.getAnswer().getAnswerValue() == AnswerValue.AV_UNKNOWN;
		}
		if(!exception) {
			super.putAction(action);
		}
	}
}
