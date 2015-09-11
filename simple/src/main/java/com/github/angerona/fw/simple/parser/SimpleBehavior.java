package com.github.angerona.fw.simple.parser;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.def.DefaultBehavior;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class SimpleBehavior extends DefaultBehavior {

	@Override
	public abstract void sendAction(AngeronaEnvironment env, Action act);

	@Override
	public void receivePerception(AngeronaEnvironment env, Perception percept) {}
	
	// TODO: implement
	
}
