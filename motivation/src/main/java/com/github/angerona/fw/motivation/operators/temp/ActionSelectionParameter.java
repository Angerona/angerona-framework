package com.github.angerona.fw.motivation.operators.temp;

import com.github.angerona.fw.motivation.reliable.impl.ActionSequences;
import com.github.angerona.fw.operators.parameter.OperatorPluginParameter;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionSelectionParameter extends OperatorPluginParameter {

	public Intentions getIntentions() {
		return getAgent().getComponent(Intentions.class);
	}
	
	public ActionSequences getSequences() {
		return getAgent().getComponent(ActionSequences.class);
	}

}
