package angerona.test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationMonitor extends JFrame {

	/** kill warning */
	private static final long serialVersionUID = 714099953028717849L;
	
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	private SimulationConfiguration actConfig;
	
	private JTextField txtSimStatus;
	
	private JButton btnRun;
	
	private String simulationDirectory;
	
	public static void main(String[] args) {
		new SimulationMonitor();
	}

	public SimulationMonitor() {
		setTitle("Angerona - Simulation Monitor");
		setBounds(0, 0, 500, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new FlowLayout());
		
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
		pack();
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
