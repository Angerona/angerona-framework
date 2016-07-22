package com.github.angerona.fw.logic.conditional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import net.sf.tweety.logics.cl.ClBeliefSet;
import net.sf.tweety.logics.cl.RuleBasedCReasoner;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.cl.syntax.Conditional;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.pl.syntax.PropositionalSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.BaseReasoner;
import com.github.angerona.fw.operators.parameter.ReasonerParameter;
import com.github.angerona.fw.util.LogicTranslator;
import com.github.angerona.fw.util.Pair;
import com.github.angerona.fw.util.Utility;

/**
 * A Reasoner for conditional belief bases using c-representations
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ConditionalReasoner extends BaseReasoner {
	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(ConditionalReasoner.class);

	private Map<ClBeliefSet, RankingFunction> cache = new HashMap<ClBeliefSet, RankingFunction>();
	
	public ConditionalReasoner() {
	}
	
	public RankingFunction calculateCRepresentation(ClBeliefSet bbase, Set<PropositionalFormula> formulas) {
		RankingFunction result = cache.get(bbase);
		if(result != null) {
			return result;
		}
		
		Set<Conditional> conds = new HashSet<Conditional>();
		conds.addAll(bbase);
		
		RuleBasedCReasoner reasoner = new RuleBasedCReasoner(conds, true);
		
		reasoner.prepare();
		reasoner.process();
		
		result = reasoner.getSemantic();
		result.forceStrictness(formulas);//TODO testen ob es geht nochmal paper lesen 
		cache.put(bbase, result);
		return result;
		
	}
	
	/**
	 * Calculates the conditional belief set from a conditional belief base.
	 * A ordinal conditional ranking function (ocf) kappa is calculated from
	 * the belief base using c representations. Then, the belief set is defined
	 * as the set of propositional literals, that can be defeasibly concluded 
	 * from the belief base.
	 * 
	 */
	@Override
	protected Set<FolFormula> inferImpl(ReasonerParameter params) {
		Set<FolFormula> retval = new HashSet<FolFormula>();
		ConditionalBeliefbase bbase = (ConditionalBeliefbase) params.getBeliefBase();
		
		RankingFunction ocf = calculateCRepresentation(bbase.getConditionalBeliefs(), bbase.getPropositions());
				
		Set<PropositionalFormula> propositions = bbase.getPropositions();
		Conjunction conjunction = new Conjunction(propositions);
		
		PropositionalSignature sig = (PropositionalSignature) bbase.getSignature();
		
		if( ocf.rank(conjunction) == RankingFunction.INFINITY ) {
			// premise is considered impossible, everything can be concluded
			for(Proposition prop : sig) {
				retval.add(new FOLAtom(new Predicate(prop.getName())));
			}
			return retval;
		}
		
		// TODO: rewrite to work with arbitrary formulas ...
		
		// A |~ B, i.e. B follows defeasibly from A iff k(A)=INF or B holds in all smallest
		// worlds according to k, in which A holds. This is equivalent to k(AB) < k(A -B)
		for(Proposition prop : sig) {
			Formula AandB = conjunction.combineWithAnd(prop);
			Formula AandNotB = conjunction.combineWithAnd(prop.complement());
			Integer rankAandB = ocf.rank(AandB);
			Integer rankAandNotB = ocf.rank(AandNotB);
			if(rankAandB < rankAandNotB) {
				retval.add(new FOLAtom(new Predicate(prop.getName())));
			} else if(rankAandNotB < rankAandB) {
				retval.add(new net.sf.tweety.logics.fol.syntax.Negation(new FOLAtom(new Predicate(prop.getName()))));
			}
		}

		return retval;
	}

	@Override
	protected Pair<Set<FolFormula>, AngeronaAnswer> queryImpl(ReasonerParameter params) {
		
		AnswerValue answer = AnswerValue.AV_FALSE;
		
		ConditionalBeliefbase bbase = (ConditionalBeliefbase) params.getBeliefBase();
		
		RankingFunction ocf = calculateCRepresentation(bbase.getConditionalBeliefs(), bbase.getPropositions());
				
		Set<PropositionalFormula> propositions = bbase.getPropositions();
		Conjunction conjunction = new Conjunction(propositions);
		
		if( ocf.rank(conjunction) == RankingFunction.INFINITY ) {
			// premise is considered impossible, everything can be concluded
			answer = AnswerValue.AV_TRUE;
		} else {
			Formula AandB = conjunction.combineWithAnd(LogicTranslator.FoToPl(params.getQuery()));
			Formula AandNotB = conjunction.combineWithAnd(LogicTranslator.FoToPl(params.getQuery().complement()));
			Integer rankAandB = ocf.rank(AandB);
			Integer rankAandNotB = ocf.rank(AandNotB);
			if(rankAandB < rankAandNotB) {
				answer = AnswerValue.AV_TRUE;
			} else if(Utility.equals(rankAandB, rankAandNotB)) {
				answer = AnswerValue.AV_UNKNOWN;
			}
		}
		Set<FolFormula> answers = new HashSet<FolFormula>();
		return new Pair<>(answers, new AngeronaAnswer(params.getQuery(), answer));
	}

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ConditionalBeliefbase.class;
	}
}
