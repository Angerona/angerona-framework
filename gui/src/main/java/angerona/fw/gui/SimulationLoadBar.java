package angerona.fw.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextField;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationLoadBar extends BaseComponent {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;

	private JTextField txtSimStatus;
	
	private JButton btnRun;
	
	private String simulationDirectory;
	
	private SimulationConfiguration actConfig;
	
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	
	public SimulationLoadBar() {
		super("Simulation Loader");
		txtSimStatus = new JTextField();
		txtSimStatus.setMinimumSize(new Dimension(200, 30));
		add(txtSimStatus);
		
		JButton btnLoad = new JButton();
		btnLoad.setText("Load Simulation");
		btnLoad.setMinimumSize(new Dimension(100, 30));
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				onLoadClicked();
			}
		});
		add(btnLoad);
		
		btnRun = new JButton("Run");
		btnRun.setText("Run");
		btnRun.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				environment.cleanupSimulation();
				environment.initSimulation(actConfig, simulationDirectory);
				environment.runTillNoMorePerceptionsLeft();
			}
		});
		add(btnRun);
		updateConfigView();
	}
	
	private void onLoadClicked() {
		JFileChooser fileDialog = new JFileChooser();
		fileDialog.setCurrentDirectory(new File("."));
		int reval = fileDialog.showOpenDialog(this);
		if(reval == JFileChooser.APPROVE_OPTION) {
			File file = fileDialog.getSelectedFile();
			try {
				actConfig = environment.loadSimulation(file.getAbsolutePath(), false);
				simulationDirectory = file.getParent();
				updateConfigView();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void updateConfigView() {
		if(actConfig == null) {
			btnRun.setEnabled(false);
			txtSimStatus.setText("No Simulation loaded.");
		} else {
			btnRun.setEnabled(true);
			if(environment.isRunning()) {
				txtSimStatus.setText("Simulation '" + actConfig.getName() + "' running.");
			} else {
				txtSimStatus.setText("Simulation '" + actConfig.getName() + "' ready.");
			}
		}
	}
}
