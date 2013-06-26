package interactive;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import angerona.fw.gui.base.ObservingPanel;
import angerona.fw.gui.simctrl.SimulationControlModel.SimulationState;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * Implementation of the SimulatioonControlView as a Bar using all the horizontal space
 * but only a small amount of vertical space.
 * @author Tim Janus
 */
public class InteractiveBar extends ObservingPanel {
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
	public InteractiveBar() {
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

}
