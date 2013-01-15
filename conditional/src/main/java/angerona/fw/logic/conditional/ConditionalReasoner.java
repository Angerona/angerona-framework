package angerona.fw.logic.conditional;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.ClassicalFormula;
import net.sf.tweety.Formula;
import net.sf.tweety.logics.conditionallogic.BruteForceCReasoner;
import net.sf.tweety.logics.conditionallogic.semantics.RankingFunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.operators.parameter.ReasonerParameter;
import angerona.fw.util.Pair;

/**
 * A Reasoner for conditional belief bases using c-representations
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ConditionalReasoner extends BaseReasoner {
	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(ConditionalReasoner.class);
	
	public RankingFunction ocf;
	
	public ConditionalReasoner() {
	}
	
	public void calculateCRepresentation(ConditionalBeliefbase bbase) {
		// Calculate c-representation
		BruteForceCReasoner creasoner = new BruteForceCReasoner(bbase.getConditionalBeliefs(), true);
		
		log.info("compute c-representation (bruteforce)");
		long startTime = System.currentTimeMillis();
		ocf = creasoner.getCRepresentation();
		long duration = System.currentTimeMillis() - startTime;
		log.info("done. duration: {}ms", duration);
	}

	/*
	@Override
	protected Pair<Set<FolFormula>, AngeronaAnswer> processInternal(ReasonerParameter params) {
		if(params.getQuery() == null) {
			Pair<Set<FolFormula>, AngeronaAnswer> reval = new Pair<>();
			reval.first = inferInternal((ConditionalBeliefbase)params.getBeliefBase());
			return reval;
		} else {
			return queryInternal((ConditionalBeliefbase)params.getBeliefBase(), params.getQuery());
		}
	}
	*/
	
	/**
	 * Calculates the conditional belief set from a conditional belief base.
	 * A ordinal conditional ranking function (ocf) kappa is calculated from
	 * the belief base using c representations. Then, the belief set is defined
	 * as the set of propositional literals, that can be defeasibly concluded 
	 * from the belief base.
	 * 
	 */
	@Override
	protected Set<FolFormula> inferInt(ReasonerParameter params) {
		Set<FolFormula> retval = new HashSet<FolFormula>();
		ConditionalBeliefbase bbase = (ConditionalBeliefbase) params.getBeliefBase();
		
		if(this.ocf == null) {
			calculateCRepresentation(bbase);
		}
		
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
		// worlds according to k, in which A holds. This is equivalent to k(AB) < k(A -B)
		for(Proposition prop : sig) {
			Formula AandB = conjunction.combineWithAnd(prop);
			Formula AandNotB = conjunction.combineWithAnd(prop.complement());
			Integer rankAandB = ocf.rank(AandB);
			Integer rankAandNotB = ocf.rank(AandNotB);
			if(rankAandB < rankAandNotB) {
				retval.add(new Atom(new Predicate(prop.getName())));
			}
		}
		// the same for negations
		for(Proposition prop : sig) {
			ClassicalFormula nprop = prop.complement();
			Formula AandB = conjunction.combineWithAnd(nprop);
			Formula AandNotB = conjunction.combineWithAnd(nprop.complement());
			Integer rankAandB = ocf.rank(AandB);
			Integer rankAandNotB = ocf.rank(AandNotB);
			if(rankAandB < rankAandNotB) {
				retval.add(new Negation(new Atom(new Predicate(prop.getName()))));
			}
		}
		return retval;
	}

	@Override
	protected Pair<Set<FolFormula>, AngeronaAnswer> queryInt(ReasonerParameter params) {
		Set<FolFormula> answers = inferInt(params);
		AnswerValue av = AnswerValue.AV_UNKNOWN;
		FolFormula query = params.getQuery();
		
		if(answers.contains(query)) {
			av = AnswerValue.AV_TRUE;
		} else if( answers.contains(new Negation(query)) ) {
			av = AnswerValue.AV_FALSE;
		}
		return new Pair<>(answers, new AngeronaAnswer(params.getBeliefBase(), query, av));
	}

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ConditionalBeliefbase.class;
	}
}
