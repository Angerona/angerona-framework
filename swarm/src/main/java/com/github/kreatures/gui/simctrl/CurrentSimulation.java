package com.github.kreatures.gui.simctrl;

public class CurrentSimulation {
	/**
	 * name of current Simulation 
	 */
	private static String name;

	public static String getName() {
		return name;
	}

	protected static void setName(String name) {
		CurrentSimulation.name = name;
	}

}
