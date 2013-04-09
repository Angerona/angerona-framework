package angerona.fw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import angerona.fw.gui.SimulationControlModel.SimulationState;
import angerona.fw.gui.base.Presenter;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationControlPresenter 
	extends Presenter<SimulationControlModel, SimulationControlView>
	implements ActionListener {
	
	public SimulationControlPresenter(SimulationControlModel model, SimulationControlView view) {
		setModel(model);
		setView(view);
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == view.getLoadButton()) {
			onLoad();
		} else if(ev.getSource() == view.getSimStateButton()) {
			switch(model.getSimulationState()) {
			case SS_LOADED:
				model.initSimulation();
				break;
				
			case SS_INITALIZED:
				model.runSimulation();
				break;
				
			case SS_FINISHED:
				model.setSimulation(model.getSimulation());
				break;
			}
		}
	}

	private void onLoad() {
		boolean reallyLoad = true;
		if(	model.getSimulationState() == SimulationState.SS_INITALIZED || 
			model.getSimulationState() == SimulationState.SS_FINISHED) {
			int res = JOptionPane.showConfirmDialog(view.getLoadButton(), "Do you really want to delete the content of the old simulation? " +
					"There is no serialisation implemented in Angerona yet.", "Really delete old results?", JOptionPane.YES_NO_OPTION);
			reallyLoad = res == JOptionPane.YES_OPTION;
		}
		
		if(reallyLoad) {
			JFileChooser fileDialog = new JFileChooser();
			fileDialog.setCurrentDirectory(new File("."));
			
			int reval = fileDialog.showOpenDialog(view.getLoadButton());
			if(reval == JFileChooser.APPROVE_OPTION) {
				File file = fileDialog.getSelectedFile();
				SimulationConfiguration config = SimulationConfiguration.loadXml(file);
				model.setSimulation(config);
			}
		}
	}

	@Override
	protected void forceUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void wireViewEvents() {
		view.getLoadButton().addActionListener(this);
		view.getSimStateButton().addActionListener(this);
	}

	@Override
	protected void unwireViewEvents() {
		view.getLoadButton().removeActionListener(this);
		view.getSimStateButton().removeActionListener(this);
	}
}
