package com.github.angerona.fw.logic.conditional;

import net.sf.tweety.logics.cl.BruteForceCReasoner;
import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.pl.syntax.Conjunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.operators.parameter.ChangeBeliefbaseParameter;

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
	protected BaseBeliefbase processInternal(ChangeBeliefbaseParameter param) {
		log.info("Revision by '{}'", param.getNewKnowledge());
		ConditionalBeliefbase beliefbase = (ConditionalBeliefbase) param.getBeliefBase();
		ConditionalBeliefbase newKnowledge = (ConditionalBeliefbase) param.getNewKnowledge();
		
		BruteForceCReasoner creasoner = new BruteForceCReasoner(beliefbase.getConditionalBeliefs(), true);
		
		log.info("compute c-representation (bruteforce)");
		long startTime = System.currentTimeMillis();
		RankingFunction ranking = creasoner.getCRepresentation();
		long duration = System.currentTimeMillis() - startTime;
		log.info("done. duration: {}ms", duration);
			
		Conjunction con = new Conjunction(beliefbase.getPropositions());
		con.addAll(newKnowledge.getPropositions());
		
		if(ranking.rank(con) < RankingFunction.INFINITY) {
			beliefbase.getPropositions().addAll(newKnowledge.getPropositions());
		}
		
		return beliefbase;
	}
	
	public boolean simulateRevision(ConditionalBeliefbase bbase, ConditionalBeliefbase newKnowledge) {
		log.info("Simulating revision by '{}'", newKnowledge);
		
		BruteForceCReasoner creasoner = new BruteForceCReasoner(bbase.getConditionalBeliefs(), true);
		
		log.info("compute c-representation (bruteforce)");
		long startTime = System.currentTimeMillis();
		RankingFunction ranking = creasoner.getCRepresentation();
		long duration = System.currentTimeMillis() - startTime;
		log.info("done. duration: {}ms", duration);
				
		
		Conjunction con = new Conjunction(bbase.getPropositions());
		con.addAll(newKnowledge.getPropositions());
		
		
		return ranking.rank(con) < RankingFunction.INFINITY;
	}

}
