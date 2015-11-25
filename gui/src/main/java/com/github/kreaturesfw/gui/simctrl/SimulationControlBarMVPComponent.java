package com.github.kreaturesfw.gui.simctrl;

import javax.swing.JPanel;

import com.github.kreaturesfw.gui.AngeronaGUIDataStorage;
import com.github.kreaturesfw.gui.AngeronaWindow;
import com.github.kreaturesfw.gui.base.ViewComponent;

import bibliothek.gui.dock.DefaultDockable;

public class SimulationControlBarMVPComponent implements ViewComponent {

	private SimulationControlBar view;
	
	public SimulationControlBarMVPComponent() {
		view = new SimulationControlBar();
		new SimulationControlPresenter(AngeronaGUIDataStorage.get().getSimulationControl(), view);
	}
	
	@Override
	public JPanel getPanel() {
		return view;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("Simulation-Control-Bar");
		dockable.setTitleIcon(AngeronaWindow.get().getIcons().get("monitor"));
		
	}

}
