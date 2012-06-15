package angerona.fw.mary;

import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;

public class LyingOperator {
	protected AnswerValue lie(AngeronaAnswer truth)
	{
		if(truth.getAnswerExtended() == AnswerValue.AV_TRUE)
			return AnswerValue.AV_FALSE;
		else if(truth.getAnswerExtended() == AnswerValue.AV_FALSE)
			return AnswerValue.AV_TRUE;
		return AnswerValue.AV_UNKNOWN;
	}
}
