package com.github.kreaturesfw.core.basic;

import com.github.kreaturesfw.core.legacy.AngeronaEnvironment;

/**
 * This interface defines an behavior for an environment.
 *
 * Also see the DefaultBehavior class it implements a simulation
 * cycle. Subclasses can adapt a lot by overloading sendAction,
 * receivePerception and isSimulationReady.
 * 
 * @author Tim Janus
 */
public interface EnvironmentBehavior {
	/**
	 * Is called by Angerona when an agent does an action.
	 * @param env		Visitor environment to fetch further information.
	 * @param action	Reference to the action.
	 */
	void sendAction(AngeronaEnvironment env, Action action);
	
	/**
	 * Is called by an external simulation if Angerona should receive an perception
	 * @param env		Visitor environment to fetch further information.
	 * @param percept	Reference to the perception received from the external simulation.
	 */
	default void receivePerception(AngeronaEnvironment env, Perception percept) {}
	
	/**
	 * performs a cycle on every agent.
	 * @param env	Visitor environment which performs the run.
	 * @return		true if at least one agent does something, false otherwise.
	 */
	boolean runOneTick(AngeronaEnvironment env);
	
	/**
	 * Performs runOneTick until it returns false.
	 * @param env	Visitor environment which performs the run.
	 * @return		true if no errors occurred in the simulation, false otherwise.	
	 */
	default boolean run(AngeronaEnvironment env) {
		while(runOneTick(env)) {}
		
		return false;
	}
	
	/**
	 * @return	true if Angerona has performed it agent cycles,
	 * 			false otherwise. 
	 */
	default boolean isAngeronaReady() { return false; };
	
	/**
	 * @return	true if the external simulation is ready, false otherwise.
	 */
	default boolean isSimulationReady() { return true; }
	
	/**
	 * @return 	true if the Angerona simulation is in its tick processing, false otherwise.
	 */
	default boolean isDoingTick() { return true; };
	
	/**
	 * @return the actual Angerona simulation tick.
	 */
	int getTick();
}
