package com.github.kreatures.example.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.FolBeliefSet;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.logic.KReaturesAnswer;
import com.github.kreatures.core.logic.AnswerValue;
import com.github.kreatures.core.logic.BaseReasoner;
import com.github.kreatures.core.operators.parameter.ReasonerParameter;
import com.github.kreatures.core.util.Pair;

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
	protected Pair<Set<FolFormula>, KReaturesAnswer> queryImpl(ReasonerParameter params) {
		
		ExampleBeliefbase bb = (ExampleBeliefbase)params.getBeliefBase();
		boolean b = bb.fbs.contains(params.getQuery());
		AnswerValue ae = b ? AnswerValue.AV_TRUE : AnswerValue.AV_FALSE;
		
		return new Pair<Set<FolFormula>, KReaturesAnswer>(new HashSet<FolFormula>(), new KReaturesAnswer(params.getQuery(), ae));
	}

	@Override
	protected Set<FolFormula> inferImpl(ReasonerParameter params) {
		FolBeliefSet fbs = ((ExampleBeliefbase)params.getBeliefBase()).getBeliefSet();
		Set<FolFormula> reval = new HashSet<>();
		reval.addAll(fbs);
		return reval;
	}
}
