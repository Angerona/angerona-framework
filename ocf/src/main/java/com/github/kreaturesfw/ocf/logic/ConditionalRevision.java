package com.github.kreaturesfw.ocf.logic;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.operators.parameter.ChangeBeliefbaseParameter;

import net.sf.tweety.logics.cl.RuleBasedCReasoner;
import net.sf.tweety.logics.cl.kappa.KappaValue;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.cl.syntax.Conditional;
import net.sf.tweety.logics.pl.syntax.Conjunction;

/**
 * Revision operator for conditional belief bases. Defined as
 * <k,R> ° {A} = <k, R + {A}>, if k(con(R) ^ A) != INFINITY
 *                = <k, R> else
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

	@Override
	protected BaseBeliefbase processImpl(ChangeBeliefbaseParameter param) {
		log.info("Revision by '{}'", param.getNewKnowledge());
		ConditionalBeliefbase beliefbase = (ConditionalBeliefbase) param.getBeliefBase();
		ConditionalBeliefbase newKnowledge = (ConditionalBeliefbase) param.getNewKnowledge();
	
//		BruteForceCReasoner creasoner = new BruteForceCReasoner(beliefbase.getConditionalBeliefs(), true);
//		
//		log.info("compute c-representation (bruteforce)");
//		long startTime = System.currentTimeMillis();
//		RankingFunction ranking = creasoner.getCRepresentation();
//		long duration = System.currentTimeMillis() - startTime;
//		log.info("done. duration: {}ms", duration);
//			
//		Conjunction con = new Conjunction(beliefbase.getPropositions());
//		con.addAll(newKnowledge.getPropositions());
//		
//		if(ranking.rank(con) < RankingFunction.INFINITY) {
//			beliefbase.getPropositions().addAll(newKnowledge.getPropositions());
//		}
//		
//		return beliefbase;
		
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
//		
//		BruteForceCReasoner creasoner = new BruteForceCReasoner(bbase.getConditionalBeliefs(), true);
//		
//		log.info("compute c-representation (bruteforce)");
//		long startTime = System.currentTimeMillis();
//		RankingFunction ranking = creasoner.getCRepresentation();
//		long duration = System.currentTimeMillis() - startTime;
//		log.info("done. duration: {}ms", duration);
//				
//		
//		Conjunction con = new Conjunction(bbase.getPropositions());
//		con.addAll(newKnowledge.getPropositions());
//		
//		
//		return ranking.rank(con) < RankingFunction.INFINITY;
		
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

}
