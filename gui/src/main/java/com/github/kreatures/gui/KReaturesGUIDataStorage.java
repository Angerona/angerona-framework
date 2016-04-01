package com.github.kreatures.gui;

import com.github.kreatures.gui.simctrl.SimulationControlModel;
import com.github.kreatures.gui.simctrl.SimulationControlModelAdapter;

public final class KReaturesGUIDataStorage {
	private SimulationControlModelAdapter simControl = new SimulationControlModelAdapter();
	
	public SimulationControlModel getSimulationControl() {
		return simControl;
	}
	
	
	public static KReaturesGUIDataStorage get() {
		if(instance == null) {
			instance = new KReaturesGUIDataStorage();
		}
		return instance;
	}
	
	private KReaturesGUIDataStorage() {}
	
	private static KReaturesGUIDataStorage instance;
}
