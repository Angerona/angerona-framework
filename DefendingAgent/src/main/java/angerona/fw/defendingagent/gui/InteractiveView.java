package angerona.fw.defendingagent.gui;

import javax.swing.JPanel;

import angerona.fw.gui.base.ViewComponent;
import bibliothek.gui.dock.DefaultDockable;

public class InteractiveView extends JPanel implements ViewComponent{

	private static final long serialVersionUID = -1117125782756694040L;

	
	public InteractiveView() {

	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("Scripting Component");
		
	}
}
