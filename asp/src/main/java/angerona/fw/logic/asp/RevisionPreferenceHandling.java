package angerona.fw.logic.asp;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.logicprogramming.asplibrary.revision.PreferenceHandling;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class RevisionPreferenceHandling extends AspRevision {

	public RevisionPreferenceHandling() throws InstantiationException {
		super();
	}

	@Override
	protected MultipleBaseRevisionOperator<Rule> createRevisionImpl() {
		return new PreferenceHandling(wrapper.getSolver());
	}

}
