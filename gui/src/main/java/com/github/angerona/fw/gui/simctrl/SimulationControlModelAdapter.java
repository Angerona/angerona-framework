package com.github.angerona.fw.gui.simctrl;

import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.serialize.SimulationConfiguration;
import com.github.angerona.fw.util.ModelAdapter;

/**
 * Implements the SimulatonControlModel
 * @author Tim Janus
 */
public class SimulationControlModelAdapter extends ModelAdapter implements SimulationControlModel {
	/** the SimulationConfiguration of the data model */
	private SimulationConfiguration simulationConfig;
	
	/** the SimulationState of the data model */
	private SimulationState simulationState = SimulationState.SS_UNDEFINED;
	
	/** the AngeroneEnvironment representing the dynamic simulation */
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	/** 
	 * Helper method: sets the SimulationState and fires the PropertyChangeEvent
	 * @param newState	The new SimulationState
	 */
	private void setSimulationState(SimulationState newState) {
		simulationState = changeProperty("simulationState", simulationState, newState);
	}
	
	@Override
	public void setSimulation(SimulationConfiguration config) {
		if(simulationConfig != null) {
			if(	simulationState == SimulationState.SS_INITALIZED ||
				simulationState == SimulationState.SS_FINISHED) {
				// new Thread(new Runnable() {
				//	@Override
				//	public void run() {
						environment.cleanupEnvironment();
						
				//	}
				//}).start();
			}
		}
		simulationConfig = changeProperty("simulationConfig", simulationConfig, config);
		if(simulationConfig != null) {
			setSimulationState(SimulationState.SS_LOADED);
		} else {
			setSimulationState(SimulationState.SS_UNDEFINED);
		}
	}
	
	@Override
	public SimulationState initSimulation() {
		if(simulationState == SimulationState.SS_LOADED) {
			if(environment.initSimulation(simulationConfig)) {
				setSimulationState(SimulationState.SS_INITALIZED);
			} else {
				setSimulationState(SimulationState.SS_LOADED);
			}
		}
		return simulationState;
	}
	
	@Override
	public SimulationState runSimulation() {
		if(simulationState == SimulationState.SS_INITALIZED) {
			//new Thread(new Runnable() {
			//	@Override
			//	public void run() {
					if(!environment.runOneTick()) {
						setSimulationState(SimulationState.SS_FINISHED);
					}
			//	}
			//}).start();
			
		}
		return simulationState;
	}

	@Override
	public SimulationState getSimulationState() {
		return simulationState;
	}

	@Override
	public SimulationConfiguration getSimulation() {
		return simulationConfig;
	}
}
