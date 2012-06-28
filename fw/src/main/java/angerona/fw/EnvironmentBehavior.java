package angerona.fw;

/**
 * This interface defines an behavior for an Environment.
 *
 * Also see the DefaultBehavior class it implements a simulation
 * cylce. Subclasses can adapt a lot by overloading sendAction,
 * receivePerception and isSimulationReady.
 * 
 * @author Tim Janus
 */
public interface EnvironmentBehavior {
	/**
	 * Is called by Angerona when an agent does an action.
	 * @param env		Visitor environment to fetch futher information.
	 * @param action	Reference to the action.
	 */
	void sendAction(AngeronaEnvironment env, Action action);
	
	/**
	 * Is called by an external simulation if Angerona should receive an perception
	 * @param env		Visitor environment to fetch futher informatin.
	 * @param percept	Reference to the perception received from the external simulation.
	 */
	void receivePerception(AngeronaEnvironment env, Perception percept);
	
	/**
	 * performs a cycle on every agent.
	 * @param env	Visitor environment which performs the run.
	 * @return		true if at least one agent does something, false otherwise.
	 */
	boolean runOneTick(AngeronaEnvironment env);
	
	/**
	 * Performs runOneTick until it returns false.
	 * @param env	Visitor environment which performs the run.
	 * @return		
	 */
	boolean run(AngeronaEnvironment env);
	
	/**
	 * @return	true if angerona has performs it agent cycles,
	 * 			false otherwise. 
	 */
	boolean isAngeronaReady();
	
	/**
	 * @return	true if the external simulation is ready, false otherwise.
	 */
	boolean isSimulationReady();
}
