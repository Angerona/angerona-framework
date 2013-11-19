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
 * 
 * @author Tim Janus
 */
public class SimulationControlBar extends ObservingPanel implements SimulationControlView {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;

	/** text field showing the status of the simulation */
	private JTextField txtSimStatus;
	
	/** a button which runs the next step of the simulation */
	private JButton btnRunOneTick;
	
	/** a button used to load another SimulationConfiguration from the filesystem */
	private JButton btnLoad;
	
	private JButton btnRunComplete;
	
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
		
		btnRunComplete = new JButton("Complete");
		btnRunComplete.setEnabled(false);
		buttonPanel.add(btnRunComplete);
		
		btnRunOneTick = new JButton("Run");
		btnRunOneTick.setText("Init");
		btnRunOneTick.setEnabled(false);
		buttonPanel.add(btnRunOneTick);
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
			btnRunComplete.setEnabled(false);
			btnRunOneTick.setEnabled(false);
			btnRunOneTick.setText("init");
			txtSimStatus.setText("No Simulation loaded.");
			break;
			
		case SS_LOADED:
			btnRunOneTick.setEnabled(true);
			btnRunOneTick.setText("Init");
			btnRunComplete.setEnabled(true);
			txtSimStatus.setText( pre + "ready.");
			break;
			
		case SS_INITALIZED:
			btnRunOneTick.setEnabled(true);
			btnRunOneTick.setText("Run");
			txtSimStatus.setText( pre + "running." );
			break;
			
		case SS_FINISHED:
			btnRunOneTick.setEnabled(true);
			btnRunOneTick.setText("Finish");
			btnRunComplete.setEnabled(false);
			txtSimStatus.setText( pre + "finished.");
			break;
		}
	}

	@Override
	public JButton getCompleteButton() {
		return btnRunComplete;
	}
	
	@Override
	public JButton getSimStateButton() {
		return btnRunOneTick;
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
