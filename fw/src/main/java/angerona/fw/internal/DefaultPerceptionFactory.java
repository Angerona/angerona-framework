package angerona.fw.internal;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Justification;
import angerona.fw.comm.Justify;
import angerona.fw.comm.Query;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.reflection.Context;
import angerona.fw.serialize.perception.AnswerDO;
import angerona.fw.serialize.perception.CommunicationActDO;
import angerona.fw.serialize.perception.InformDO;
import angerona.fw.serialize.perception.JustificationDO;
import angerona.fw.serialize.perception.JustifyDO;
import angerona.fw.serialize.perception.PerceptionDO;
import angerona.fw.serialize.perception.QueryDO;

/**
 * A factory for creating perceptions from data objects. The default
 * implementation supports Queries and Answers.
 * 
 * By inheriting from this class one can define more perception types
 * in plugins.
 * BEWARE in the actual implementation is no PerceptionFactory plugin.
 * 
 * @author Tim Janus, Daniel Dilger
 */
public class DefaultPerceptionFactory extends PerceptionFactory {

	@Override
	public Perception generateFromDataObject(PerceptionDO dataObject, Context context) {
		if(dataObject instanceof CommunicationActDO) {
			CommunicationActDO commAct = (CommunicationActDO)dataObject;
			String name = commAct.getSender();
			Agent s = Angerona.getInstance().getActualSimulation().getAgentByName(name);
			String r = createString(commAct.getReceiver(), context);
			if(commAct instanceof QueryDO) {
				QueryDO qdo = (QueryDO)commAct;
				FolFormula f = createFormula(qdo.getQuestion(), context);
				return new Query(s,r,f);
			} else if(commAct instanceof AnswerDO) {
				AnswerDO ado = (AnswerDO)commAct;
				FolFormula q = createFormula(ado.getQuestion(), context);
				AngeronaAnswer aa = null;
				if(q.isGround()) {
					AnswerValue av = createAnswerValue(ado.getAnswer(), context);
					aa = new AngeronaAnswer(null, q, av);
				} else {
					FolFormula an = createFormula(ado.getAnswer(), context);
					aa = new AngeronaAnswer(null, q, an);
				}
				return new Answer(s, r, q, aa);
			} else if (commAct instanceof InformDO) {
				InformDO rrdo = (InformDO) commAct;
				Set<FolFormula> fs = createFormulaSet(rrdo.getSentences(), context);
				
				return new Inform(s, r, fs);
			} else if (commAct instanceof JustifyDO) {
				JustifyDO jdo = (JustifyDO)commAct;
				FolFormula proposition = createFormula(jdo.getProposition(), context);
				AnswerValue av = createAnswerValue(jdo.getAnswerValue(), context);
				
				return new Justify(s, r, proposition, av);
			} else if(commAct instanceof JustificationDO) {
				JustificationDO jdo = (JustificationDO)commAct;
				
				FolFormula proposition = createFormula(jdo.getProposition(), context);
				AnswerValue av = createAnswerValue(jdo.getAnswerValue(), context);
				Set<FolFormula> j = createFormulaSet(jdo.getJustifications(), context);
				
				return new Justification(s, r, proposition, av, j);
			}
		} 
		
		throw new NotImplementedException("Factory doesn't support: " + dataObject.getClass().getName() + " as Parameter");
	}

}
