package com.github.angerona.fw.logic.asp;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.beliefdynamics.MultipleBaseRevisionOperator;
import net.sf.tweety.lp.asp.beliefdynamics.revision.CredibilityRevision;
import net.sf.tweety.lp.asp.syntax.Program;
import net.sf.tweety.lp.asp.syntax.Rule;

public class RevisionCredibilityProgramsKeepRules extends AspRevision{
	
	public RevisionCredibilityProgramsKeepRules() throws InstantiationException {
		super();
	}

	@Override
	protected MultipleBaseRevisionOperator<Rule> createRevisionImpl() {
		return new CredibilityRevision(wrapper.getSolver(), 100){
			/** Seperates beliefbase1 into facts and rules. The Revision is performed
			 *  as revise(facts, newBeliefs, rules). This way the rules will not be removed.
			 * @param beliefbase1 Current Beliefs (Facts and Rules)
			 * @param beliefbase2 New Beliefs
			 */
			@Override
			public Collection<Rule> revise(Collection<Rule> beliefBase1, Collection<Rule> beliefBase2) {
				List<Collection<Rule>> param = new LinkedList<Collection<Rule>>();
				
				Collection<Rule> facts = new Program();
				Collection<Rule> rules = new Program();
				for (Rule r: beliefBase1) {
					if (r.getPremise()==null || r.getPremise().isEmpty())
						facts.add(r);
					else
						rules.add(r);
				}
				param.add(facts);
				param.add(beliefBase2);
				param.add(rules);
				return revise(param);
			}	
		};
	}

}
