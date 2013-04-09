package angerona.fw.gui;

import angerona.fw.gui.base.Model;
import angerona.fw.serialize.SimulationConfiguration;

public interface SimulationControlModel extends Model {
	public static enum SimulationState {
		SS_UNDEFINED,
		SS_LOADED,
		SS_INITALIZED,
		SS_FINISHED
	}
	
	void setSimulation(SimulationConfiguration simulationConfig);
	
	SimulationConfiguration getSimulation();
	
	SimulationState initSimulation();
	
	SimulationState runSimulation();
	
	SimulationState getSimulationState();
}
