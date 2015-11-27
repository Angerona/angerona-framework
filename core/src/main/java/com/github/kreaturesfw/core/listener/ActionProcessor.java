package com.github.kreaturesfw.core.listener;

import com.github.kreaturesfw.core.basic.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.logic.Beliefs;

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
