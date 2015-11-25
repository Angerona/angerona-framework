package com.github.kreatures.core.logic.asp;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.lp.asp.beliefdynamics.revision.CredibilityRevision;
import net.sf.tweety.lp.asp.syntax.Rule;

public class RevisionCredibilityPrograms extends AspRevision {

	public RevisionCredibilityPrograms() throws InstantiationException {
		super();
	}

	@Override
	protected MultipleBaseRevisionOperator<Rule> createRevisionImpl() {
		return new CredibilityRevision(wrapper.getSolver(), 100);
	}

}
