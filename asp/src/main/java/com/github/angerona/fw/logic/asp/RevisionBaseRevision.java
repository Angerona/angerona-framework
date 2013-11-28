package com.github.angerona.fw.logic.asp;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.lp.asp.beliefdynamics.baserevision.ELPBaseRevisionOperator;
import net.sf.tweety.lp.asp.beliefdynamics.baserevision.MonotoneGlobalMaxichoiceSelectionFunction;
import net.sf.tweety.lp.asp.beliefdynamics.baserevision.SelectionFunction;
import net.sf.tweety.lp.asp.syntax.Rule;

/**
 * This class represents the tweety-implementation of the base revision operator
 * for extended logic programs as introduced in [KKI12]. The base revision of
 * a set of rules is defined as the screened maxi-choice consolidation of the
 * union of the belief base and the new beliefs.
 * 
 *  [KKI12] Krümpelmann, Patrick und Gabriele Kern-Isberner: 
 * 	Belief Base Change Operations for Answer Set Programming. 
 *  In: Cerro, Luis Fariñas, Andreas Herzig und Jérôme Mengin (Herausgeber):
 *  Proceedings of the 13th European conference on Logics in Artificial 
 *  Intelligence, Band 7519, Seiten 294–306, Toulouse, France, 2012. 
 *  Springer Berlin Heidelberg.
 * 
 * @author Sebastian Homann
 *
 */
public class RevisionBaseRevision extends AspRevision {

	public RevisionBaseRevision() throws InstantiationException {
		super();
	}

	@Override
	protected MultipleBaseRevisionOperator<Rule> createRevisionImpl() {
		SelectionFunction<Rule> selection = new MonotoneGlobalMaxichoiceSelectionFunction();		
		return new ELPBaseRevisionOperator(wrapper.getSolver(), selection);
	}

}
