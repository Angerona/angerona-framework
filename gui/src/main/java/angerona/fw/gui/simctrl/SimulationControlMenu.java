package angerona.fw.gui.simctrl;

import javax.swing.AbstractButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import angerona.fw.gui.simctrl.SimulationControlModel.SimulationState;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * This view extends a JMenu with two buttons to control the simulation
 * @author Tim Janus
 */
public class SimulationControlMenu implements SimulationControlView {

	/** the menu item used to change the state of the simulation */
	private JMenuItem miState;
	
	/** the menu item used to laod another simulation configuration */
	private JMenuItem miLoad;

	/**
	 * Adds MenuItems to the given parent menu
	 * @param parent	The menu holding the menu items.
	 */
	public SimulationControlMenu(JMenu parent) {
		miState = new JMenuItem("Init");
		miLoad = new JMenuItem("Load...");
		parent.add(miState);
		parent.add(miLoad);
	}

	@Override
	public AbstractButton getSimStateButton() {
		return miState;
	}

	@Override
	public AbstractButton getLoadButton() {
		return miLoad;
	}
	
	@Override
	public <T> void propertyChange(String propertyName, T oldValue, T newValue) {
		if(propertyName  == "simulationState") {
			SimulationState newState = (SimulationState)newValue;
			onSimulationStateChanged(newState);
		} else if(propertyName == "simulationConfig") {
			SimulationConfiguration config = (SimulationConfiguration)newValue;
			onSimulationConfigChanged(config);
		}
	}

	@Override
	public void onSimulationStateChanged(SimulationState newState) {
		switch(newState) {
		case SS_UNDEFINED:
			miState.setEnabled(false);
			miState.setText("init");
			break;
			
		case SS_LOADED:
			miState.setEnabled(true);
			miState.setText("Init");
			break;
			
		case SS_INITALIZED:
			miState.setEnabled(true);
			miState.setText("Run");
			break;
			
		case SS_FINISHED:
			miState.setEnabled(true);
			miState.setText("Finish");
			break;
		}
	}
	
	@Override
	public void onSimulationConfigChanged(SimulationConfiguration config) {
		// does nothing
	}
	
	@Override
	public <T> boolean allowPropertyChange(String propertyName, T oldValue,
			T newValue) {
		// does nothing
		return true;
	}

	@Override
	public <T> T transformPropertyChange(String propertyName, T oldValue,
			T newValue) {
		// does nothing
		return null;
	}

}
