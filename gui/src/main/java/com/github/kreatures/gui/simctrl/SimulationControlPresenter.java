package com.github.kreatures.gui.simctrl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.github.kreatures.gui.base.Presenter;
import com.github.kreatures.gui.simctrl.SimulationControlModel.SimulationState;
import com.github.kreatures.core.serialize.SimulationConfiguration;
import com.github.kreatures.core.util.PropertyObserver;
import com.github.kreatures.core.util.Utility;

/**
 * This class is responsible to wire a SimulationControlModel with a
 * SimulationControlView.
 * 
 * @author Tim Janus
 */
public class SimulationControlPresenter 
	extends Presenter<SimulationControlModel, SimulationControlView>
	implements ActionListener, PropertyObserver {
	
	private boolean complete = false;
	
	/** Default Ctor: The user has to call setModel() and setView(). */
	public SimulationControlPresenter() {}
	
	/** 
	 * Ctor: Invokes setModel() and setView()
	 * @param model	The used model.
	 * @param view	The used view.
	 */
	public SimulationControlPresenter(SimulationControlModel model, SimulationControlView view) {
		setModel(model);
		setView(view);
	}
	
	@Override
	public void setModel(SimulationControlModel model) {
		if(this.model != null) {
			this.model.removePropertyObserver(this);
		}
		super.setModel(model);
		if(this.model != null) {
			this.model.addPropertyObserver(this);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		if(ev.getSource() == view.getLoadButton()) {
			onLoad();
		} else if(ev.getSource() == view.getSimStateButton()) {
			changeSimState();
		} else if(ev.getSource() == view.getCompleteButton()) {
			complete = true;
			changeSimState();
		}
	}

	/** 
	 * Is called when the SimulationState change button is called and updates the 
	 * simulation of the SimulationControlModel depending on the current SimulationState
	 */
	private void changeSimState() {
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
			
		default:
			break;
		}
	}

	/**
	 * Is called when the load button of the view is called, then it opens a file dialog
	 * and lets the user decide which file is loaded as SimulationConfiguration. This
	 * SimulationConfiguration becomes the new simulation of the SimulationControlModel. 
	 */
	private void onLoad() {
		boolean reallyLoad = true;
		if(	model.getSimulationState() == SimulationState.SS_INITALIZED || 
			model.getSimulationState() == SimulationState.SS_FINISHED) {
			int res = JOptionPane.showConfirmDialog(view.getLoadButton(), "Do you really want to delete the content of the old simulation? " +
					"There is no serialisation implemented in KReatures yet.", "Really delete old results?", JOptionPane.YES_NO_OPTION);
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
		view.onSimulationConfigChanged(model.getSimulation());
		view.onSimulationStateChanged(model.getSimulationState());
	}

	@Override
	protected void wireViewEvents() {
		view.getLoadButton().addActionListener(this);
		view.getSimStateButton().addActionListener(this);
		view.getCompleteButton().addActionListener(this);
	}

	@Override
	protected void unwireViewEvents() {
		view.getLoadButton().removeActionListener(this);
		view.getSimStateButton().removeActionListener(this);
		view.getCompleteButton().removeActionListener(this);
	}

	@Override
	public <T> boolean allowPropertyChange(String propertyName, T oldValue,
			T newValue) {
		return true;
	}

	@Override
	public <T> T transformPropertyChange(String propertyName, T oldValue,
			T newValue) {
		return null;
	}

	@Override
	public <T> void propertyChange(String propertyName, T oldValue, T newValue) {
		if(Utility.equals(propertyName, "simulationTick") && complete) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					changeSimState();
				}
			});
		} else if(Utility.equals(propertyName, "simulationState")) {
			SimulationState ss = (SimulationState)newValue;
			if(ss == SimulationState.SS_FINISHED) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						complete = false;
					}
				});
			}
		}
	}
}
