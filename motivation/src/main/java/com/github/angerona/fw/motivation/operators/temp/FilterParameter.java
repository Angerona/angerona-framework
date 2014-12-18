package com.github.angerona.fw.motivation.operators.temp;

import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.operators.parameter.OperatorPluginParameter;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class FilterParameter extends OperatorPluginParameter {

	public Desires getDesires() {
		return getAgent().getComponent(Desires.class);
	}

	public Intentions getIntention() {
		return getAgent().getComponent(Intentions.class);
	}

}
