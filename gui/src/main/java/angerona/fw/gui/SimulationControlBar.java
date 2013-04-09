package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import angerona.fw.gui.SimulationControlModel.SimulationState;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationControlBar extends JPanel implements SimulationControlView {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;

	/** text field showing the status of the simulation */
	private JTextField txtSimStatus;
	
	/** a button which runs the next step of the simulation */
	private JButton btnSimState;
	
	private JButton btnLoad;
	
	private String currentSimulationName = "";
	
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

	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		if(ev.getPropertyName() == "simulationState") {
			SimulationState newState = (SimulationState)ev.getNewValue();
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
				btnSimState.setText("Restart");
				txtSimStatus.setText( pre + "finished.");
				break;
			}
		} else if(ev.getPropertyName() == "simulationConfig") {
			SimulationConfiguration config = (SimulationConfiguration)ev.getNewValue();
			currentSimulationName = config.getName();
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

}
