package angerona.fw.gui;

import angerona.fw.gui.simctrl.SimulationControlModel;
import angerona.fw.gui.simctrl.SimulationControlModelAdapter;

public final class AngeronaGUIDataStorage {
	private SimulationControlModelAdapter simControl = new SimulationControlModelAdapter();
	
	public SimulationControlModel getSimulationControl() {
		return simControl;
	}
	
	
	public static AngeronaGUIDataStorage get() {
		if(instance == null) {
			instance = new AngeronaGUIDataStorage();
		}
		return instance;
	}
	
	private AngeronaGUIDataStorage() {}
	
	private static AngeronaGUIDataStorage instance;
}
