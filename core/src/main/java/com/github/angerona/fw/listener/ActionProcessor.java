package com.github.angerona.fw.listener;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.logic.Beliefs;

/**
 * Classes implementing the Interface are capable of processing an action.
 * The ActionProcessor is used to allow the processing of action to different processors
 * like Agent (physical) or ViolatesOperator (mental).
 * 
 * @author Tim Janus
 */
public interface ActionProcessor {
	void performAction(Action action, Agent agent, Beliefs beliefs);
}
