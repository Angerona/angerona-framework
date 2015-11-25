package com.github.kreatures.gui.simctrl;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreatures.gui.KReaturesGUIDataStorage;
import com.github.kreatures.gui.KReaturesWindow;
import com.github.kreatures.gui.base.ViewComponent;

public class SimulationControlBarMVPComponent implements ViewComponent {

	private SimulationControlBar view;
	
	public SimulationControlBarMVPComponent() {
		view = new SimulationControlBar();
		new SimulationControlPresenter(KReaturesGUIDataStorage.get().getSimulationControl(), view);
	}
	
	@Override
	public JPanel getPanel() {
		return view;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("Simulation-Control-Bar");
		dockable.setTitleIcon(KReaturesWindow.get().getIcons().get("monitor"));
		
	}

}
