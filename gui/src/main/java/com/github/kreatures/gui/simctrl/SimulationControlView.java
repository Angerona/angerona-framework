package com.github.kreatures.gui.simctrl;

import javax.swing.AbstractButton;

import com.github.kreatures.gui.base.View;
import com.github.kreatures.gui.simctrl.SimulationControlModel.SimulationState;
import com.github.kreatures.core.serialize.SimulationConfiguration;

/**
 * This interface returns all important controls which form a View for
 * controlling a simulation.
 * 
 * Receives PropertyChangeEvent from a SimulatonControlModel.
 * 
 * @author Tim Janus
 */
public interface SimulationControlView extends View {
	/** @return The button responsible for changing the SimulationState. */
	AbstractButton getSimStateButton();
	
	/**	@return	The button used to load a SimulationConfiguration from the file system. */
	AbstractButton getLoadButton();
	
	AbstractButton getCompleteButton();
	
	/**
	 * This  method is called if the SimulationConfiguration changes
	 * @param config	The new SimulationConfiguration
	 */
	void onSimulationConfigChanged(SimulationConfiguration config);
	
	/**
	 * This method is called if the SimulationState changes
	 * @param newState	The new SimulationState
	 */
	void onSimulationStateChanged(SimulationState newState);
}
