package com.github.kreatures.core;

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
	 * Is called by KReatures when an agent does an action.
	 * @param env		Visitor environment to fetch further information.
	 * @param action	Reference to the action.
	 */
	void sendAction(KReaturesEnvironment env, Action action);
	
	/**
	 * Is called by an external simulation if KReatures should receive an perception
	 * @param env		Visitor environment to fetch further information.
	 * @param percept	Reference to the perception received from the external simulation.
	 */
	void receivePerception(KReaturesEnvironment env, Perception percept);
	
	/**
	 * performs a cycle on every agent.
	 * @param env	Visitor environment which performs the run.
	 * @return		true if at least one agent does something, false otherwise.
	 */
	boolean runOneTick(KReaturesEnvironment env);
	
	/**
	 * Performs runOneTick until it returns false.
	 * @param env	Visitor environment which performs the run.
	 * @return		true if no errors occurred in the simulation, false otherwise.	
	 */
	boolean run(KReaturesEnvironment env);
	
	/**
	 * @return	true if KReatures has performed it agent cycles,
	 * 			false otherwise. 
	 */
	boolean isKReaturesReady();
	
	/**
	 * @return	true if the external simulation is ready, false otherwise.
	 */
	boolean isSimulationReady();
	
	/**
	 * @return 	true if the KReatures simulation is in its tick processing, false otherwise.
	 */
	boolean isDoingTick();
	
	/**
	 * @return the actual KReatures simulation tick.
	 */
	int getTick();
}