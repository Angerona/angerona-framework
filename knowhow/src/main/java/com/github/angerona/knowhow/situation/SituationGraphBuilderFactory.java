package com.github.angerona.knowhow.situation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.knowhow.graph.Selector;

public class SituationGraphBuilderFactory {
	private static Logger LOG = LoggerFactory.getLogger(SituationGraphBuilderFactory.class);
	
	public static SituationGraphBuilder createGraphBuilder(Situation situation, Agent agent, Selector connectorNode) {
		LOG.debug("Entering createGraphBuilder({}, {}, "+connectorNode.toString()+")", 
				situation.toString(), agent.getName());
		SituationGraphBuilder reval = null;
		
		if(situation instanceof InvestigationSituation) {
			reval = new InvestigationSituationBuilder((InvestigationSituation)situation, agent, connectorNode);
		} else {
			throw new NotImplementedException("There is no SituationGraphBuilder registered at " +
					"the Factory with name '" + situation.getClass().getName() + "'");
		}
		;
		LOG.debug("Leaving createGraphBuilder() = {}", reval.toString());
		return reval;
	}
}
