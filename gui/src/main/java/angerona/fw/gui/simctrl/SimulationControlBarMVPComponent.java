package angerona.fw.gui.simctrl;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import angerona.fw.gui.AngeronaGUIDataStorage;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.base.ViewComponent;

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
