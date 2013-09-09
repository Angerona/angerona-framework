package com.github.angerona.fw.gui.simctrl;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.angerona.fw.gui.base.ObservingPanel;
import com.github.angerona.fw.gui.simctrl.SimulationControlModel.SimulationState;
import com.github.angerona.fw.serialize.SimulationConfiguration;
import com.github.angerona.fw.util.Utility;

/**
 * Implementation of the SimulatioonControlView as a Bar using all the horizontal space
 * but only a small amount of vertical space.
 * @author Tim Janus
 */
public class SimulationControlBar extends ObservingPanel implements SimulationControlView {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;

	/** text field showing the status of the simulation */
	private JTextField txtSimStatus;
	
	/** a button which runs the next step of the simulation */
	private JButton btnSimState;
	
	/** a button used to load another SimulationConfiguration from the filesystem */
	private JButton btnLoad;
	
	/** a helper variable saving the name of the current selected simulation */
	private String currentSimulationName = "";
	
	/** Default Ctor: Creating the Widget hierarchy */
	public SimulationControlBar() {
		this.setLayout(new BorderLayout());
		txtSimStatus = new JTextField();
		txtSimStatus.setMinimumSize(new Dimension(200, 30));
		add(txtSimStatus, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		btnLoad = new JButton();
		btnLoad.setText("Load Simulation");
		btnLoad.setMinimumSize(new Dimension(100, 30));
		buttonPanel.add(btnLoad);
		
		btnSimState = new JButton("Run");
		btnSimState.setText("Init");
		btnSimState.setEnabled(false);
		buttonPanel.add(btnSimState);
		add(buttonPanel, BorderLayout.EAST);
	}

	/**
	 * Saves the name of the new simulation
	 */
	@Override
	public void onSimulationConfigChanged(SimulationConfiguration config) {
		if(config != null) {
			currentSimulationName = config.getName();
		} else {
			currentSimulationName = "";
		}
	}

	/**
	 * Updates the state of the SimulationState button and the status text
	 */
	@Override
	public void onSimulationStateChanged(SimulationState newState) {
		String pre = "Simulation '" + currentSimulationName + "' ";
		switch(newState) {
		case SS_UNDEFINED:
			btnSimState.setEnabled(false);
			btnSimState.setText("init");
			txtSimStatus.setText("No Simulation loaded.");
			break;
			
		case SS_LOADED:
			btnSimState.setEnabled(true);
			btnSimState.setText("Init");
			txtSimStatus.setText( pre + "ready.");
			break;
			
		case SS_INITALIZED:
			btnSimState.setEnabled(true);
			btnSimState.setText("Run");
			txtSimStatus.setText( pre + "running." );
			break;
			
		case SS_FINISHED:
			btnSimState.setEnabled(true);
			btnSimState.setText("Finish");
			txtSimStatus.setText( pre + "finished.");
			break;
		}
	}

	@Override
	public JButton getSimStateButton() {
		return btnSimState;
	}

	@Override
	public JButton getLoadButton() {
		return btnLoad;
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

}
