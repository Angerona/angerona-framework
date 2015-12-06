package com.github.kreaturesfw.gui.simctrl;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.github.kreaturesfw.core.serialize.SimulationConfiguration;
import com.github.kreaturesfw.core.util.Utility;
import com.github.kreaturesfw.gui.base.ViewAdapter;
import com.github.kreaturesfw.gui.simctrl.SimulationControlModel.SimulationState;

/**
 * This view extends a JMenu with two buttons to allow the user to control the simulation
 * using menu items.
 * 
 * @author Tim Janus
 */
public class SimulationControlMenu extends ViewAdapter implements SimulationControlView {

	/** the menu item used to change the state of the simulation */
	private JMenuItem miState;
	
	/** the menu item used to laod another simulation configuration */
	private JMenuItem miLoad;

	private JMenuItem miComplete;
	
	private JMenu parent;
	
	/**
	 * Adds MenuItems to the given parent menu
	 * @param parent	The menu holding the menu items.
	 */
	public SimulationControlMenu(JMenu parent) {
		this.parent = parent;
		miState = new JMenuItem("Init");
		miLoad = new JMenuItem("Load...");
		miComplete = new JMenuItem("Complete");
		parent.add(miState);
		parent.add(miLoad);
		parent.add(miComplete);
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
		if(Utility.equals(propertyName, "simulationState")) {
			SimulationState newState = (SimulationState)newValue;
			onSimulationStateChanged(newState);
		} else if(Utility.equals(propertyName, "simulationConfig")) {
			SimulationConfiguration config = (SimulationConfiguration)newValue;
			onSimulationConfigChanged(config);
		}
	}

	@Override
	public void onSimulationStateChanged(SimulationState newState) {
		switch(newState) {
		case SS_UNDEFINED:
			miComplete.setEnabled(false);
			miState.setEnabled(false);
			miState.setText("init");
			break;
			
		case SS_LOADED:
			miComplete.setEnabled(true);
			miState.setEnabled(true);
			miState.setText("Init");
			break;
			
		case SS_INITALIZED:
			miComplete.setEnabled(true);
			miState.setEnabled(true);
			miState.setText("Run");
			break;
			
		case SS_FINISHED:
			miComplete.setEnabled(false);
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
	public JComponent getRootComponent() {
		return parent;
	}

	@Override
	public JMenuItem getCompleteButton() {
		return miComplete;
	}
}
