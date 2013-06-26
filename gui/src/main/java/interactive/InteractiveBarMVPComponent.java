package interactive;

import javax.swing.JPanel;

import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.base.ViewComponent;
import bibliothek.gui.dock.DefaultDockable;

public class InteractiveBarMVPComponent implements ViewComponent {

	private InteractiveBar view;
	
	public InteractiveBarMVPComponent() {
		view = new InteractiveBar();
		new InteractivePresenter(new InteractiveModelAdapter(), view);
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
