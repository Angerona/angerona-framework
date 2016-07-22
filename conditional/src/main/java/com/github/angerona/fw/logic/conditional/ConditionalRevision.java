package com.github.angerona.fw.logic.conditional;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.cl.RuleBasedCReasoner;
import net.sf.tweety.logics.cl.kappa.KappaValue;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.cl.syntax.Conditional;
import net.sf.tweety.logics.pl.semantics.PossibleWorld;
import net.sf.tweety.logics.pl.syntax.Conjunction;
import net.sf.tweety.logics.pl.syntax.Disjunction;
import net.sf.tweety.logics.pl.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.operators.parameter.ChangeBeliefbaseParameter;

/**
 *                
 * @author Sebastian Homann, Pia Wierzoch
 */

public class ConditionalRevision extends BaseChangeBeliefs {
	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(ConditionalRevision.class);

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ConditionalBeliefbase.class;
	}

	/**
	 *  Revision for conditional belief bases. Defined as
     * <k,R> ° {A} = <k, R + {A}>, if k(con(R) ^ A) != INFINITY
     *                = <k, R> else
	 */
	@Override
	protected BaseBeliefbase processImpl(ChangeBeliefbaseParameter param) {
		log.info("Revision by '{}'", param.getNewKnowledge());
		ConditionalBeliefbase beliefbase = (ConditionalBeliefbase) param.getBeliefBase();
		ConditionalBeliefbase newKnowledge = (ConditionalBeliefbase) param.getNewKnowledge();
	
		Set<Conditional> conds = new HashSet<Conditional>();
		conds.addAll(beliefbase.getConditionalBeliefs());
		
		RuleBasedCReasoner reasoner = new RuleBasedCReasoner(conds, true);
		log.info("ConditionalRevision: preparing conditional structures");
		Long before = System.currentTimeMillis();
		reasoner.prepare();
		Long duration = System.currentTimeMillis() - before;
		log.info("Generated in " + duration + "ms:");
		log.debug("Conditional Structure:");
		log.debug(reasoner.getConditionalStructure().toString());
		
		log.debug("Initial Kappa:");
		for(KappaValue kv : reasoner.getKappas()) {
			log.debug(kv.fullString());
		}
		
		log.info("processing RuleBasedCReasoner");
		before = System.currentTimeMillis();
		reasoner.process();
		duration = System.currentTimeMillis() - before;
		log.info("Evaluated in " + duration + "ms:");
		log.debug("Initial Kappa:");
		for(KappaValue kv : reasoner.getKappas()) {
			log.debug(kv.fullString());
		}
		log.debug("Ranking-Function:");
		log.debug(reasoner.getSemantic().toString());
		log.debug("");
		
		RankingFunction ranking = reasoner.getSemantic();
	
		Conjunction con = new Conjunction(beliefbase.getPropositions());
		con.addAll(newKnowledge.getPropositions());
		
		if(ranking.rank(con) < RankingFunction.INFINITY) {
			beliefbase.getPropositions().addAll(newKnowledge.getPropositions());
		}
		
		return beliefbase;
	}
	
	public boolean simulateRevision(ConditionalBeliefbase bbase, ConditionalBeliefbase newKnowledge) {
		log.info("Simulating revision by '{}'", newKnowledge);

		
		Set<Conditional> conds = new HashSet<Conditional>();
		conds.addAll(bbase.getConditionalBeliefs());
		
		RuleBasedCReasoner reasoner = new RuleBasedCReasoner(conds, true);
		log.info("ConditionalRevision: preparing conditional structures");
		Long before = System.currentTimeMillis();
		reasoner.prepare();
		Long duration = System.currentTimeMillis() - before;
		log.info("Generated in " + duration + "ms:");
		log.debug("Conditional Structure:");
		log.debug(reasoner.getConditionalStructure().toString());
		
		log.debug("Initial Kappa:");
		for(KappaValue kv : reasoner.getKappas()) {
			log.debug(kv.fullString());
		}
		
		log.info("processing RuleBasedCReasoner");
		before = System.currentTimeMillis();
		reasoner.process();
		duration = System.currentTimeMillis() - before;
		log.info("Evaluated in " + duration + "ms:");
		log.debug("Initial Kappa:");
		for(KappaValue kv : reasoner.getKappas()) {
			log.debug(kv.fullString());
		}
		log.debug("Ranking-Function:");
		log.debug(reasoner.getSemantic().toString());
		log.debug("");
		
		
		RankingFunction ranking = reasoner.getSemantic();
		
		Conjunction con = new Conjunction(bbase.getPropositions());
		con.addAll(newKnowledge.getPropositions());
		
		return ranking.rank(con) < RankingFunction.INFINITY;
	}
	
	
	/**
	 * OCF revision for conditional belief bases. Defined as
     * k ° A (w) = k(w|A)                     , if w satisfies A
     * 			   k(w|notA) + max{k(notA),1} , if w satisfies notA 
	 * 
	 * @return The new rankingfunction
	 */
	public RankingFunction simulateOCFRevisionByFormulas(ConditionalBeliefbase bbase, PropositionalFormula formulaA){
		log.info("Simulateing OCF revision by '{}'", formulaA);
		
		ConditionalBeliefbase beliefbase = new ConditionalBeliefbase(bbase);
		
		RuleBasedCReasoner reasoner = new RuleBasedCReasoner(beliefbase.getConditionalBeliefs());
		reasoner.prepare();
		reasoner.process();
		
		RankingFunction ranking = reasoner.getSemantic();
		ranking.forceStrictness(beliefbase.getPropositions());
		
		Set<PossibleWorld> worlds = ranking.getPossibleWorlds();
		for(PossibleWorld world: worlds){
			if(world.satisfies((PropositionalFormula)formulaA)){
				int newWorldRank = ranking.rank(world) - ranking.rank(formulaA);
				
				ranking.setRank(world, newWorldRank);
			}else{
				Negation formulaNegA = new Negation(formulaA);
				int newWorldRank = ranking.rank(world) - ranking.rank(formulaNegA) + Math.max(ranking.rank(formulaNegA), 1);
				
				ranking.setRank(world, newWorldRank);
			}
		}
		
		return ranking;
	}

	
	/**
	 * OCF revision for conditional belief bases. Defined as
     * k ° (B|A)(w) = k0 + k(w) + k-, if w satisfies A and notB
     * 				  k0 + k(w)     , if w satisfies notA or B 
     * 
     * with 
     * k0 = 0            , if k(A and B) < k(A and notB)
     *      - k(A and B) , if k(A and B) >= k(A and notB)
     * 
     * k- = max{0, k(A and B) - K(A and negB) + 1}
	 * 
	 * @return The new rankingfunction
	 */
	public RankingFunction simulateOCFRevisionByConditional(ConditionalBeliefbase bbase, Conditional conditional){
		log.info("Simulateing OCF revision by '{}'", conditional);
		
		ConditionalBeliefbase beliefbase = new ConditionalBeliefbase(bbase);
		
		RuleBasedCReasoner reasoner = new RuleBasedCReasoner(beliefbase.getConditionalBeliefs());
		reasoner.prepare();
		reasoner.process();
		
		RankingFunction ranking = reasoner.getSemantic();
		ranking.forceStrictness(beliefbase.getPropositions());
		
		PropositionalFormula a = new Conjunction(conditional.getPremise()), b = conditional.getConclusion();
		Conjunction formulaANegB = new Conjunction(a, new Negation(b));
		
		Set<PossibleWorld> worlds = ranking.getPossibleWorlds();
		for(PossibleWorld world: worlds){
			if(world.satisfies((PropositionalFormula)formulaANegB)){
				int kappa0, kappaWorld, kappaMinus;
				
				kappaMinus = Math.max(0, ranking.rank(new Conjunction(a, b)) - ranking.rank(new Conjunction(a, new Negation(b))) + 1);
				
				if(ranking.rank(new Conjunction(a, b)) < ranking.rank(new Conjunction(a, new Negation(b)))){
					kappa0 = 0;
				}else{
					kappa0 = (-1) * ranking.rank(new Disjunction(new Negation(a), b));
				}
				kappaWorld = ranking.rank(world);
				
				ranking.setRank(world, kappa0 + kappaWorld - kappaMinus);
			}else{
				int kappa0, kappaWorld;
				
				if(ranking.rank(new Conjunction(a, b)) < ranking.rank(new Conjunction(a, new Negation(b)))){
					kappa0 = 0;
				}else{
					kappa0 = (-1) * ranking.rank(new Disjunction(new Negation(a), b));
				}
				
				kappaWorld = ranking.rank(world);
				
				ranking.setRank(world, kappa0 + kappaWorld);
			}
		}
		
		return ranking;
	}
}
