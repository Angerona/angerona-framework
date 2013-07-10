package interactive;

import javax.swing.JPanel;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.base.ViewComponent;
import bibliothek.gui.dock.DefaultDockable;

/**
 * 
 * @author Pia Wierzoch
 */
public class InteractiveBarMVPComponent implements ViewComponent {

	private InteractiveBar view;

	
	public InteractiveBarMVPComponent(AngeronaEnvironment simulationEnvironment) {
		InteractiveModelAdapter modelAdapter = new InteractiveModelAdapter(simulationEnvironment);
		view = new InteractiveBar(modelAdapter.getReceiver(), modelAdapter.getActionTypes());
		new InteractivePresenter(modelAdapter, view);
	}
	
	@Override
	public JPanel getPanel() {
		return view;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("Interactive View");
		dockable.setTitleIcon(AngeronaWindow.get().getIcons().get("monitor"));
		
	}

}
