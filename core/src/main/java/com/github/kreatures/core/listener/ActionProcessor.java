package com.github.kreatures.core.listener;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.logic.Beliefs;

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
