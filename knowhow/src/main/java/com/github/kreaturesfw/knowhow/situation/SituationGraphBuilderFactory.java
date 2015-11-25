package com.github.kreaturesfw.knowhow.situation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.Agent;
import com.github.kreaturesfw.core.error.NotImplementedException;

/**
 * This factory is responsible to generate the correct graph builders for the given situation.
 * 
 * @author Tim Janus
 */
public class SituationGraphBuilderFactory {
	private static Logger LOG = LoggerFactory.getLogger(SituationGraphBuilderFactory.class);
	
	public static SituationGraphBuilder createGraphBuilder(Situation situation, Agent agent) {
		LOG.debug("Entering createGraphBuilder({}, {})", 
				situation.toString(), agent.getName());
		SituationGraphBuilder reval = null;
		
		if(situation instanceof InvestigationSituation) {
			reval = new InvestigationSituationBuilder((InvestigationSituation)situation, agent);
		} else if(situation instanceof DefendingSituation) {
			reval = new DefendingSituationBuilder((DefendingSituation)situation, agent);
		} else {
			throw new NotImplementedException("There is no SituationGraphBuilder registered at " +
					"the Factory with name '" + situation.getClass().getName() + "'");
		}
		;
		LOG.debug("Leaving createGraphBuilder() = {}", reval.toString());
		return reval;
	}
}
