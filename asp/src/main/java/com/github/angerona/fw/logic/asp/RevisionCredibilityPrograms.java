package com.github.angerona.fw.logic.asp;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.logicprogramming.asplibrary.revision.CredibilityRevision;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class RevisionCredibilityPrograms extends AspRevision {

	public RevisionCredibilityPrograms() throws InstantiationException {
		super();
	}

	@Override
	protected MultipleBaseRevisionOperator<Rule> createRevisionImpl() {
		return new CredibilityRevision(wrapper.getSolver());
	}

}
