package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.view.BaseView;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationControlBar extends BaseView {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;

	/** text field showing the status of the simulation */
	private JTextField txtSimStatus;
	
	/** a button which runs the next step of the simulation */
	private JButton btnRun;
	
	private String simulationDirectory;
	
	private SimulationConfiguration actConfig;
	
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	private boolean simFinishedOrNotStarted = true;
	
	@Override
	public void init() {
		this.setLayout(new BorderLayout());
		txtSimStatus = new JTextField();
		txtSimStatus.setMinimumSize(new Dimension(200, 30));
		add(txtSimStatus, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		JButton btnLoad = new JButton();
		btnLoad.setText("Load Simulation");
		btnLoad.setMinimumSize(new Dimension(100, 30));
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onLoadClicked();
			}
		});
		buttonPanel.add(btnLoad);
		
		btnRun = new JButton("Run");
		btnRun.setText("Init");
		btnRun.setEnabled(false);
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onRunButtonClicked();
			}
		});
		buttonPanel.add(btnRun);
		updateConfigView(false);
		add(buttonPanel, BorderLayout.EAST);
	}
	
	@Override
	public void cleanup() {}
	
	/**
	 *	Helper method: When run button is clicked the simulation is either initialized,
	 *	restarted with a confirm dialog or the next tick of the simulation runs.
	 */
	private void onRunButtonClicked() {
		
		if(simFinishedOrNotStarted && !environment.isReady()) {
			environment.initSimulation(actConfig, simulationDirectory);
			simFinishedOrNotStarted = false;
		} else if(simFinishedOrNotStarted && environment.isReady()) {
			loadSimulation(new File(actConfig.getFilePath()));
		} else {
			simFinishedOrNotStarted = !environment.runOneTick();
		}
		updateConfigView(simFinishedOrNotStarted);
	}
	
	private void onLoadClicked() {
		JFileChooser fileDialog = new JFileChooser();
		fileDialog.setCurrentDirectory(new File("."));
		int reval = fileDialog.showOpenDialog(this);
		if(reval == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			loadSimulation(file);
		}
	}

	public void loadSimulation(File file) {
		boolean reallyLoad = !environment.isReady();
		if(!reallyLoad) {
			int res = JOptionPane.showConfirmDialog(this, "Do you really want to delete the content of the old simulation? " +
				"There is no serialisation implemented in Angerona yet.", "Really delete old results?", JOptionPane.YES_NO_OPTION);
			reallyLoad = res == JOptionPane.YES_OPTION;
		}
		if(reallyLoad) {
			if(environment.isReady()) {
				environment.cleanupSimulation();
			}
			try {
				actConfig = environment.loadSimulation(file.getAbsolutePath(), false);
				actConfig.setFilePath(file.getAbsolutePath());
			} catch ( IOException e) {
				AngeronaWindow.getInstance().onError("Cannot load Simulation", 
						e.getMessage());
				return;
			}
			simulationDirectory = file.getParent();
			simFinishedOrNotStarted = true;
			updateConfigView(false);
		}
	}
	
	private void updateConfigView(boolean simFinished) {
		if(actConfig == null) {
			btnRun.setEnabled(false);
			txtSimStatus.setText("No Simulation loaded.");
		} else {
			btnRun.setEnabled(true);
			
			String pre = "Simulation '" + actConfig.getName() + "' ";
			if(environment.isReady() && !simFinished) {
				txtSimStatus.setText( pre + "running." );
				btnRun.setText("Run");
			} else if(environment.isReady() && simFinished) {
				txtSimStatus.setText( pre + "finished.");
				btnRun.setText("Restart");
			} else {
				txtSimStatus.setText( pre + "ready.");
				btnRun.setText("Init");
			}
		}
	}

	public AngeronaEnvironment getEnvironment() {
		return environment;
	}

	@Override
	public Class<?> getObservedType() {
		return null;
	}

}
