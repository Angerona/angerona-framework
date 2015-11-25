package com.github.kreatures.gui.simctrl;

import com.github.kreatures.core.serialize.SimulationConfiguration;
import com.github.kreatures.core.util.Model;

/**
 * This data model allows to control a KReatures simulation. 
 * Shall change the following PropertyChange events:
 * - simulationState
 * - simulatonConfig
 * 
 * @author Tim Janus
 */
public interface SimulationControlModel extends Model {
	/**
	 * This enumeration contains different states for a simulation
	 * @author Tim Janus
	 */
	public static enum SimulationState {
		/** The simulation is not defined, this means no SimulationConfiguration is given. */
		SS_UNDEFINED,
		/** The simulation is defined, so a SimluatinConfiguration is given but it is not initialized yet. */
		SS_LOADED,
		/** The simulation is initialized */
		SS_INITALIZED,
		/** The simulatino is finished, that means no more agent action happened in the last run call */
		SS_FINISHED
	}
	
	/**
	 * Change the simulation using the configuration given as parameter.
	 * @param simulationConfig	Reference to the configuration of the simulation.
	 */
	void setSimulation(SimulationConfiguration simulationConfig);
	
	/**
	 * @return	The simulation configuration data structure
	 */
	SimulationConfiguration getSimulation();
	
	
	/**
	 * Initalizes the simulation and returns the new state.
	 * @return	SS_INITIALIZED if no error occured or another SimulationState if an error occurd.
	 */
	SimulationState initSimulation();
	
	/**
	 * Runs one simulation tick
	 * @return	SS_INITALIZED if the simulation is not finished, SS_FINISHED if the simulation
	 * 			is finished. 
	 */
	SimulationState runSimulation();
	
	/**
	 * @return The current SimulationState
	 */
	SimulationState getSimulationState();
}
