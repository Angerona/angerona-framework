package angerona.fw.logic.conditional;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.Formula;
import net.sf.tweety.logics.conditionallogic.BruteForceCReasoner;
import net.sf.tweety.logics.conditionallogic.semantics.RankingFunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalSignature;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
 * A Reasoner for conditional belief bases using c-representations
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ConditionalReasoner extends BaseReasoner {

	/**
	 * Calculates the conditional belief set from a conditional belief base.
	 * A ordinal conditional ranking function (ocf) kappa is calculated from
	 * the belief base using c representations. Then, the belief set is defined
	 * as the set of propositions, that can be defeasibly concluded from the belief base
	 * 
	 */
	@Override
	protected Set<FolFormula> inferInt() {
		ConditionalBeliefbase bbase = (ConditionalBeliefbase) this.actualBeliefbase;
		Set<FolFormula> retval = new HashSet<FolFormula>();
		
		// Calculate c-representation
		BruteForceCReasoner creasoner = new BruteForceCReasoner(bbase.getConditionalBeliefs(), true);
		RankingFunction ocf = creasoner.getCRepresentation();
		
		Set<PropositionalFormula> propositions = bbase.getPropositions();
		Conjunction conjunction = new Conjunction(propositions);
		
		PropositionalSignature sig = (PropositionalSignature) bbase.getSignature();
		
		if( ocf.rank(conjunction) == RankingFunction.INFINITY ) {
			// premise is considered impossible, everything can be concluded
			for(Proposition prop : sig) {
				retval.add(new Atom(new Predicate(prop.getName())));
			}
			return retval;
		}
		
		// A |~ B, i.e. B follows defeasibly from A iff k(A)=INF or B holds in all smallest
		// worlds (according to k), in which A holds. This is equivalent to k(AB) < k(A -B)
		for(Proposition prop : sig) {
			Formula AandB = conjunction.combineWithAnd(prop);
			Formula AandNotB = conjunction.combineWithAnd(prop.complement());
			Integer rankAandB = ocf.rank(AandB);
			Integer rankAandNotB = ocf.rank(AandNotB);
			if(rankAandB < rankAandNotB) {
				retval.add(new Atom(new Predicate(prop.getName())));
			}
		}
		return retval;
	}

	@Override
	protected Answer queryInt(FolFormula query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ConditionalBeliefbase.class;
	}

	@Override
	protected AngeronaAnswer processInt(ReasonerParameter param) {
		// TODO Auto-generated method stub
		return null;
	}

}
