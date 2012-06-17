package angerona.fw.logic.dummy;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
 * Just a dummy Reasoner for testing purposes.
 * @author Tim Janus
 */
public class DummyReasoner extends BaseReasoner {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return DummyBeliefbase.class;
	}

	@Override
	public Answer query(FolFormula query) {
		if(this.actualBeliefbase == null)
			return null;
		
		DummyBeliefbase bb = (DummyBeliefbase)this.actualBeliefbase;
		boolean b = bb.fbs.contains(query);
		AnswerValue ae = b ? AnswerValue.AV_TRUE : AnswerValue.AV_FALSE;
		
		return new AngeronaAnswer(bb, query, ae);
	}

	@Override
	protected AngeronaAnswer processInt(ReasonerParameter param) {
		return (AngeronaAnswer) query(param.getBeliefbase(), param.getQuery());
	}

	@Override
	public Set<FolFormula> infer() {
		return new HashSet<FolFormula>();
	}
}
