package com.github.kreaturesfw.asp.logic;

import java.util.List;
import java.util.Set;

import com.github.kreaturesfw.core.operators.parameter.ReasonerParameter;

import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.sf.tweety.lp.asp.util.AnswerSet;

/**
 * A reasoner for extended logic programs under the answer set semantics using
 * a skeptical algorithm, that means it creates the intersection of all literals 
 * in all answer sets. 
 * 
 * @author Tim Janus
 */
public class AspSkepticalReasoner extends AspReasoner {
	public AspSkepticalReasoner() throws InstantiationException {
		super();
	}

	@Override
	protected Set<DLPLiteral> selectAnswerSet(ReasonerParameter params,
			List<AnswerSet> answerSets) {
		return skepticalSelection(answerSets);
	}
}
