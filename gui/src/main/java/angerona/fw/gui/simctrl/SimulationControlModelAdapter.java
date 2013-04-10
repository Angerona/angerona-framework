package angerona.fw.gui.simctrl;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.serialize.SimulationConfiguration;
import angerona.fw.util.ModelAdapter;

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
				environment.cleanupSimulation();
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
