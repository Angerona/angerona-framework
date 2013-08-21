package interactive;

import javax.swing.JPanel;

import angerona.fw.AngeronaEnvironment;
import angerona.fw.NextActionRequester;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.base.ViewComponent;
import bibliothek.gui.dock.DefaultDockable;

/**
 * 
 * @author Pia Wierzoch
 */
public class InteractiveBarMVPComponent implements ViewComponent {

	private InteractiveBar view;
	private InteractivePresenter presenter;

	
	public InteractiveBarMVPComponent(AngeronaEnvironment simulationEnvironment) {
		InteractiveModelAdapter modelAdapter = new InteractiveModelAdapter(simulationEnvironment);
		view = new InteractiveBar(modelAdapter.getReceiver(), modelAdapter.getActionTypes());
		presenter = new InteractivePresenter(modelAdapter, view);
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
	
	public boolean getHasAction(){
		return presenter.getHasAction();
	}

}
