package angerona.fw.internal;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQuery;
import angerona.fw.comm.DetailQueryAnswer;
import angerona.fw.comm.Query;
import angerona.fw.comm.RevisionRequest;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AnswerValue;
import angerona.fw.reflection.Context;
import angerona.fw.serialize.perception.AnswerDO;
import angerona.fw.serialize.perception.CommunicationActDO;
import angerona.fw.serialize.perception.DetailAnswerDO;
import angerona.fw.serialize.perception.DetailQueryDO;
import angerona.fw.serialize.perception.PerceptionDO;
import angerona.fw.serialize.perception.QueryDO;
import angerona.fw.serialize.perception.RevisionRequestDO;

public class DetailPerceptionFactory extends PerceptionFactory{

	public FolFormula createDetailAnswer(String paramValue, Context context)
	{
		if(paramValue.startsWith("$")) {
			System.out.println("ASDF Answer variable:"+paramValue);
			//TODO: support variable names for detail answers
			return null;
		}
		else
		{
			System.out.println("ASDF Answer: "+paramValue);
			return null;
		}
	}
	
	@Override
	public Perception generateFromDataObject(PerceptionDO dataObject,
			Context context)
	{
		if(dataObject instanceof CommunicationActDO) 
		{
			CommunicationActDO commAct = (CommunicationActDO)dataObject;
			String s = createString(commAct.getSender(), context);
			String r = createString(commAct.getReceiver(), context);
			if(commAct instanceof QueryDO) 
			{
				//Is there any functional difference between a normal query and a detail query? I don't think so. 
				if(commAct instanceof DetailQueryDO)
				{
					DetailQueryDO qdo = (DetailQueryDO)commAct;
					FolFormula f = createFormula(qdo.getQuestion(), context);
					return new DetailQuery(s,r,f);
				}
				else
				{
					QueryDO qdo = (QueryDO)commAct;
					FolFormula f = createFormula(qdo.getQuestion(), context);
					return new Query(s,r,f);
				}
			} 
			else if(commAct instanceof AnswerDO) 
			{
				if(commAct instanceof DetailAnswerDO)
				{
					DetailAnswerDO dado = (DetailAnswerDO) commAct;
					FolFormula q = createFormula(dado.getQuestion(), context);
					FolFormula answer = createFormula(dado.getAnswer(), context);
					System.out.println("ASDF FolFormula answer:"+answer.toString());
					return new DetailQueryAnswer(s, r, q, answer);
				}
				else
				{
					AnswerDO ado = (AnswerDO)commAct;
					FolFormula q = createFormula(ado.getQuestion(), context);
					AnswerValue av = createAnswerValue(ado.getAnswer(), context);
					return new Answer(s, r, q, av);
				}
			} 
			else if (commAct instanceof RevisionRequestDO) 
			{
				RevisionRequestDO rrdo = (RevisionRequestDO) commAct;
				Set<FolFormula> fs = createFormulaSet(rrdo.getSentences(), context);
				
				return new RevisionRequest(s, r, fs);
			}
		}
		throw new NotImplementedException("Factory doesn't support: " + dataObject.getClass().getName() + " as Parameter");
	}

}
