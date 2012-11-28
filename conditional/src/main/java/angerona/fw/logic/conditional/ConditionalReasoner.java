package angerona.fw.logic.conditional;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
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
		//TODO: 1) calculate c-representation 2) find satisfiing propositions
		
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
