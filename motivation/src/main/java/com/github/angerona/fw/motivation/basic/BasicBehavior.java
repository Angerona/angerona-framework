package com.github.angerona.fw.motivation.basic;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.AngeronaEnvironment;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class BasicBehavior extends ParsingBehavior {

	@Override
	public void sendAction(AngeronaEnvironment env, Action act) {}

	@Override
	protected boolean _runOnTick(AngeronaEnvironment env) {
		for (Agent agnt : env.getAgents()) {
			agnt.cycle();
		}

		return false;
	}

}
