package com.github.angerona.fw.logic.asp;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.lp.asp.beliefdynamics.revision.PreferenceHandling;
import net.sf.tweety.lp.asp.syntax.Rule;

public class RevisionPreferenceHandling extends AspRevision {

	public RevisionPreferenceHandling() throws InstantiationException {
		super();
	}

	@Override
	protected MultipleBaseRevisionOperator<Rule> createRevisionImpl() {
		return new PreferenceHandling(wrapper.getSolver(), 100);
	}

}
