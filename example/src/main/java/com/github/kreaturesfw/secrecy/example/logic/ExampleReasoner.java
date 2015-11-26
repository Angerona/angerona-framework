package com.github.kreaturesfw.secrecy.example.logic;

import java.util.HashSet;
import java.util.Set;

import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.AngeronaAnswer;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.core.logic.BaseReasoner;
import com.github.kreaturesfw.core.operators.parameter.ReasonerParameter;
import com.github.kreaturesfw.core.util.Pair;

import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;

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
	protected Pair<Set<FolFormula>, AngeronaAnswer> queryImpl(ReasonerParameter params) {
		
		ExampleBeliefbase bb = (ExampleBeliefbase)params.getBeliefBase();
		boolean b = bb.fbs.contains(params.getQuery());
		AnswerValue ae = b ? AnswerValue.AV_TRUE : AnswerValue.AV_FALSE;
		
		return new Pair<Set<FolFormula>, AngeronaAnswer>(new HashSet<FolFormula>(), new AngeronaAnswer(params.getQuery(), ae));
	}

	@Override
	protected Set<FolFormula> inferImpl(ReasonerParameter params) {
		FolBeliefSet fbs = ((ExampleBeliefbase)params.getBeliefBase()).getBeliefSet();
		Set<FolFormula> reval = new HashSet<>();
		reval.addAll(fbs);
		return reval;
	}
}
