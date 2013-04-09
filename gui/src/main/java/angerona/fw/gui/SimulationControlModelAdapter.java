package angerona.fw.gui;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.base.ModelAdapter;
import angerona.fw.serialize.SimulationConfiguration;

public class SimulationControlModelAdapter extends ModelAdapter implements SimulationControlModel {
	private SimulationConfiguration simulationConfig;
	
	private SimulationState simulationState = SimulationState.SS_UNDEFINED;
	
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	private void setSimulationState(SimulationState newState) {
		SimulationState oldValue = simulationState;
		simulationState = newState;
		firePropertyChange("simulationState", oldValue, newState);
	}
	
	@Override
	public void setSimulation(SimulationConfiguration config) {
		if(simulationConfig != null) {
			if(	simulationState == SimulationState.SS_INITALIZED ||
				simulationState == SimulationState.SS_FINISHED) {
				environment.cleanupSimulation();
			}
		}
		SimulationConfiguration oldValue = simulationConfig;
		simulationConfig = config;
		firePropertyChange("simulationConfig", oldValue, simulationConfig);
		setSimulationState(SimulationState.SS_LOADED);
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
			if(!environment.runOneTick()) {
				setSimulationState(SimulationState.SS_FINISHED);
			}
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
