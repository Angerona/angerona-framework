package com.github.angerona.fw.logic.asp;

import java.util.List;
import java.util.Set;

import net.sf.tweety.lp.asp.syntax.DLPLiteral;
import net.sf.tweety.lp.asp.util.AnswerSet;

import com.github.angerona.fw.operators.parameter.ReasonerParameter;

/**
 * A reasoner for extended logic programs under the answer set semantics using
 * a credulous algorithm, that means it creates the union of all literals in all
 * answer sets. 
 * This may create inconsistent belief sets if a program contains the following rules:
 * 
 * a  :- not -a.
 * -a :- not a.
 * 
 * @author Tim Janus
 */
public class AspCredulousReasoner extends AspReasoner {
	public AspCredulousReasoner() throws InstantiationException {
		super();
	}

	@Override
	protected Set<DLPLiteral> selectAnswerSet(ReasonerParameter params,
			List<AnswerSet> answerSets) {
		return credulousSelection(answerSets);
	}
}
