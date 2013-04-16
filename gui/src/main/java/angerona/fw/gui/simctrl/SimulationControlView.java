package angerona.fw.gui.simctrl;

import javax.swing.AbstractButton;

import angerona.fw.gui.simctrl.SimulationControlModel.SimulationState;
import angerona.fw.serialize.SimulationConfiguration;
import angerona.fw.util.PropertyObserver;

/**
 * This interface returns all important controls which form a View for
 * controlling a simulation.
 * 
 * Receives PropertyChangeEvent from a SimulatonControlModel.
 * 
 * @author Tim Janus
 */
public interface SimulationControlView extends PropertyObserver {
	/** @return The button responsible for changing the SimulationState. */
	AbstractButton getSimStateButton();
	
	/**	@return	The button used to load a SimulationConfiguration from the file system. */
	AbstractButton getLoadButton();
	
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
