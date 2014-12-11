package com.github.angerona.fw.motivation.operators;

import java.util.Collection;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.reliable.impl.ActionSequences;
import com.github.angerona.fw.operators.parameter.OperatorPluginParameter;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class PlanOperatorParameter extends OperatorPluginParameter {

	public Collection<Desire> getDesires() {
		return getAgent().getComponent(MotiveCouplings.class).getDesires();
	}

	public ActionSequences getSequences() {
		return getAgent().getComponent(ActionSequences.class);
	}

}
