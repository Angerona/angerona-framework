package angerona.fw.logic.conditional;

import net.sf.tweety.logics.conditionallogic.BruteForceCReasoner;
import net.sf.tweety.logics.conditionallogic.semantics.RankingFunction;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

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
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
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

}
