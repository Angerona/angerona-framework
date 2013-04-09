package angerona.fw.gui;

public final class AngeronaDataStorage {
	private SimulationControlModelAdapter simControl = new SimulationControlModelAdapter();
	
	public SimulationControlModel getSimulationControl() {
		return simControl;
	}
	
	
	public static AngeronaDataStorage get() {
		if(instance == null) {
			instance = new AngeronaDataStorage();
		}
		return instance;
	}
	
	private AngeronaDataStorage() {}
	
	private static AngeronaDataStorage instance;
}
