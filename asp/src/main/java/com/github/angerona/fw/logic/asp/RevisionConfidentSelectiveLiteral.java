package com.github.angerona.fw.logic.asp;

import net.sf.tweety.arg.lp.semantics.attack.ConfidentAttack;
import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.lp.asp.beliefdynamics.selectiverevision.ParameterisedArgumentativeSelectiveRevisionOperator;
import net.sf.tweety.lp.asp.beliefdynamics.selectiverevision.ParameterisedArgumentativeSelectiveRevisionOperator.TransformationType;
import net.sf.tweety.lp.asp.syntax.Rule;

/**
 * This class implements the confident selective revision operator
 * for literals introduced in the master thesis of Sebastian Homann.
 * 
 * The key features of this selective revision operator * include:
 *  - Inclusion: P * Q contains only rules from P and Q
 *  - Vacuity + Inclusion: If P \cup Q is consistent, then P * Q = P \cup Q
 *  - Full Consistency: P * Q is always consistent
 *  - NP-Core-Retainment: For every literal in Q not accepted by this revision,
 *      there exists a consistent rebuttal of this literal in P \cup Q
 * 
 * @author Sebastian Homann
 *
 */
public class RevisionConfidentSelectiveLiteral extends AspRevision {

	public RevisionConfidentSelectiveLiteral() throws InstantiationException {
		super();
	}

	@Override
	protected MultipleBaseRevisionOperator<Rule> createRevisionImpl() {
		return new ParameterisedArgumentativeSelectiveRevisionOperator(
				wrapper.getSolver(), 
				ConfidentAttack.getInstance(), 
				ConfidentAttack.getInstance(), 
				TransformationType.SCEPTICAL
				);
	}

}
