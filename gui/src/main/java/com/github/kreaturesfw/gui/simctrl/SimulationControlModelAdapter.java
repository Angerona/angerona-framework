package com.github.kreaturesfw.gui.simctrl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.kreaturesfw.core.AngeronaEnvironment;
import com.github.kreaturesfw.core.serialize.SimulationConfiguration;
import com.github.kreaturesfw.core.util.ModelAdapter;

/**
 * Implements the SimulatonControlModel
 * @author Tim Janus
 */
public class SimulationControlModelAdapter extends ModelAdapter implements SimulationControlModel {
	/** the SimulationConfiguration of the data model */
	private SimulationConfiguration simulationConfig;
	
	private int simulationTick;
	
	/** the SimulationState of the data model */
	private SimulationState simulationState = SimulationState.SS_UNDEFINED;
	
	/** the AngeroneEnvironment representing the dynamic simulation */
	private AngeronaEnvironment environment = new AngeronaEnvironment();
	
	/** generate a thread pool using one thread (the worker thread for the simulation) */
	private final ExecutorService pool = Executors.newFixedThreadPool(1);
	
	/** 
	 * Helper method: sets the SimulationState and fires the PropertyChangeEvent
	 * @param newState	The new SimulationState
	 */
	private synchronized void setSimulationState(SimulationState newState) {
		simulationState = changeProperty("simulationState", simulationState, newState);
	}
	
	private synchronized void setSimulationTick(int tick) {
		this.simulationTick = changeProperty("simulationTick", this.simulationTick, tick);
	}
	
	@Override
	public void setSimulation(SimulationConfiguration config) {
		if(simulationConfig != null) {
			if(	simulationState == SimulationState.SS_INITALIZED ||
				simulationState == SimulationState.SS_FINISHED) {
				pool.execute(new Runnable() {
					@Override
					public void run() {
						synchronized(environment) {
							environment.cleanupEnvironment();	
						}
					}
				});
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
			pool.execute(new Runnable() {
				@Override
				public void run() {
					synchronized(environment) {
						if(environment.initSimulation(simulationConfig)) {
							setSimulationState(SimulationState.SS_INITALIZED);
							setSimulationTick(simulationTick);
						}
					}
				}
			});
		}
		return simulationState;
	}
	
	@Override
	public SimulationState runSimulation() {
		if(simulationState == SimulationState.SS_INITALIZED) {
			pool.execute(new Runnable() {
				@Override
				public void run() {
					synchronized(environment) {
						if(!environment.runOneTick()) {
							setSimulationState(SimulationState.SS_FINISHED);
						} else {
							setSimulationTick(simulationTick);
						}
					}
				}
			});
			
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
