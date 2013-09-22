package com.github.angerona.knowhow.situation;

import com.github.angerona.fw.error.NotImplementedException;

public class SituationGraphBuilderFactory {
	public static SituationGraphBuilder createGraphBuilder(Situation situation) {
		if(situation instanceof InvestigationSituation) {
			return null;
		} else {
			throw new NotImplementedException("There is no SituationGraphBuilder registered at " +
					"the Factory with name '" + situation.getClass().getName() + "'");
		}
	}
}
