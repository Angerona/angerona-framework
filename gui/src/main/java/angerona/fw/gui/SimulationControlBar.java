package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.view.BaseView;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationControlBar extends BaseView {
	/** kill warning */
	private static final long serialVersionUID = -5662460862082002346L;

	private JTextField txtSimStatus;
	
	private JButton btnRun;
	
	private String simulationDirectory;
	
	private SimulationConfiguration actConfig;
	
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	private boolean simFinished = true;
	
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
	
	private void onRunButtonClicked() {
		if(simFinished) {
			boolean doIt = Angerona.getInstance().getActualReport() == null;
			if(!doIt) {
				int res = JOptionPane.showConfirmDialog(this, "Do you really want to delete the content of the old simulation? " +
					"There is no serialisation implemented in Angerona yet.", "Really delete old results?", JOptionPane.YES_NO_OPTION);
				doIt = res == JOptionPane.YES_OPTION;
			}
			if(doIt) {
				environment.cleanupSimulation();
				environment.initSimulation(actConfig, simulationDirectory);
				simFinished = false;
				updateConfigView(simFinished);
			}
		} else {
			simFinished = !environment.runOneTick();
			updateConfigView(simFinished);
		}
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
		try {
			actConfig = environment.loadSimulation(file.getAbsolutePath(), false);
			simulationDirectory = file.getParent();
			updateConfigView(false);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	public String getComponentTypeName() {
		return "Simulation Control-Bar";
	}
}
