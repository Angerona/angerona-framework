package angerona.fw.operators.dummy;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.Beliefs;
import angerona.fw.nacts.NAAnswer;
import angerona.fw.nacts.NAQuery;
import angerona.fw.operators.BaseChangeOperator;
import angerona.fw.operators.parameter.UpdateParameter;

/**
 * Dummy Update Operator can only handle: Answer and Query negotiation actions.
 * TODO: Use the correct beliefbase operators (update/revision)
 * @author Tim Janus
 */
public class DummyChangeOperator extends BaseChangeOperator {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(DummyChangeOperator.class);
	
	@Override
	protected Beliefs processInt(UpdateParameter param) {
		LOG.info("Run Example-Change-Operator");
		Beliefs reval = param.getAgent().getBeliefs();
		String id = param.getAgent().getAgentProcess().getName();
		
		if(param.getPerception() instanceof NAAnswer) {
			NAAnswer naa = (NAAnswer)param.getPerception();
			String out = id + " Update-NA: Answer ";
			out += (naa.getSenderId().compareTo(id) == 0 ? "as sender (view)" : "as receiver (world)");
			
			Set<FolFormula> knowledge = new HashSet<FolFormula>();
			FolFormula toAdd = naa.getRegarding();
			if(naa.getAnswer() == AnswerValue.AV_FALSE) {
				toAdd = new Negation((FolFormula)toAdd);
			} 
			knowledge.add(toAdd);
			
			BaseBeliefbase bb = null;
			if(id.compareTo(naa.getReceiverId()) == 0) {
				bb = reval.getWorldKnowledge();
				bb.addNewKnowledge(knowledge);
			} else if (id.compareTo(naa.getSenderId()) == 0){
				bb = param.getAgent().getBeliefs().getViewKnowledge().get(naa.getReceiverId());
				bb.addNewKnowledge(knowledge);
			}
			
			report(out, bb);
		} else if(param.getPerception() instanceof NAQuery) {
			NAQuery naq = (NAQuery)param.getPerception();
			String out = id + " Update-NA: Query ";
			out += (naq.getSenderId().compareTo(id) == 0 ? "as sender (nonsense)" : "as receiver (view)");
			
			report(out, param.getAgent());
		} else {
			LOG.warn("Update-Operator: Cant handle perception of type: " + param.getPerception().getClass().getName());
		}
		
		return reval;
	}
	
	@Override
	public String getPosterName() {
		return "DummyChange";
	}
}
