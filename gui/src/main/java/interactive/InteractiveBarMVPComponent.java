package interactive;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.gui.AngeronaWindow;
import com.github.angerona.fw.gui.base.ViewComponent;

/**
 * 
 * @author Pia Wierzoch
 */
public class InteractiveBarMVPComponent implements ViewComponent {

	private InteractiveBar view;
	private InteractivePresenter presenter;

	
	public InteractiveBarMVPComponent(AngeronaEnvironment simulationEnvironment, Thread caller) {
		InteractiveModelAdapter modelAdapter = new InteractiveModelAdapter(simulationEnvironment);
		view = new InteractiveBar(modelAdapter.getReceiver(), modelAdapter.getActionTypes());
		presenter = new InteractivePresenter(modelAdapter, view, caller);
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
