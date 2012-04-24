package angerona.fw.internal;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Query;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AnswerValue;
import angerona.fw.reflection.Context;
import angerona.fw.serialize.perception.AnswerDO;
import angerona.fw.serialize.perception.CommunicationActDO;
import angerona.fw.serialize.perception.PerceptionDO;
import angerona.fw.serialize.perception.QueryDO;

/**
 * A factory for creating perceptions from data objects. The default
 * implementation supports Queries and Answers.
 * 
 * By inherting from this class one can define more perception types
 * in plugins.
 * BEWARE in the actual implementation is no PerceptionFactory plugin.
 * 
 * @author Tim Janus
 */
public class DefaultPerceptionFactory extends PerceptionFactory {

	@Override
	public Perception generateFromDataObject(PerceptionDO dataObject, Context context) {
		if(dataObject instanceof CommunicationActDO) {
			CommunicationActDO commAct = (CommunicationActDO)dataObject;
			String s = createString(commAct.getSender(), context);
			String r = createString(commAct.getReceiver(), context);
			if(commAct instanceof QueryDO) {
				QueryDO qdo = (QueryDO)commAct;
				FolFormula f = createFormula(qdo.getQuestion(), context);
				return new Query(s,r,f);
			} else if(commAct instanceof AnswerDO) {
				AnswerDO ado = (AnswerDO)commAct;
				FolFormula q = createFormula(ado.getQuestion(), context);
				AnswerValue av = createAnswerValue(ado.getAnswer(), context);
				return new Answer(s, r, q, av);
			}
		} 
		
		throw new NotImplementedException("Factory doesn't support: " + dataObject.getClass().getName() + " as Parameter");
	}

}
