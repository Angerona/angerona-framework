package angerona.fw.example.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.FolBeliefSet;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;
import angerona.fw.util.Pair;

/**
 * Just a dummy Reasoner for testing purposes.
 * @author Tim Janus
 */
public class ExampleReasoner extends BaseReasoner {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ExampleBeliefbase.class;
	}

	@Override
	protected Pair<Set<FolFormula>, AngeronaAnswer> queryInt(ReasonerParameter params) {
		
		ExampleBeliefbase bb = (ExampleBeliefbase)params.getBeliefBase();
		boolean b = bb.fbs.contains(params.getQuery());
		AnswerValue ae = b ? AnswerValue.AV_TRUE : AnswerValue.AV_FALSE;
		
		return new Pair<Set<FolFormula>, AngeronaAnswer>(new HashSet<FolFormula>(), new AngeronaAnswer(params.getQuery(), ae));
	}

	@Override
	protected Set<FolFormula> inferInt(ReasonerParameter params) {
		FolBeliefSet fbs = ((ExampleBeliefbase)params.getBeliefBase()).getBeliefSet();
		Set<FolFormula> reval = new HashSet<>();
		reval.addAll(fbs);
		return reval;
	}
}
