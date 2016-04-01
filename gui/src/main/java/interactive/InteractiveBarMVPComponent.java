package interactive;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreatures.core.KReaturesEnvironment;
import com.github.kreatures.gui.KReaturesWindow;
import com.github.kreatures.gui.base.ViewComponent;

/**
 * 
 * @author Pia Wierzoch
 */
public class InteractiveBarMVPComponent implements ViewComponent {

	private InteractiveBar view;

	
	public InteractiveBarMVPComponent(KReaturesEnvironment simulationEnvironment, Thread caller) {
		InteractiveModelAdapter modelAdapter = new InteractiveModelAdapter(simulationEnvironment);
		view = new InteractiveBar(modelAdapter.getReceiver(), modelAdapter.getActionTypes());
		new InteractivePresenter(modelAdapter, view, caller);
	}
	
	@Override
	public JPanel getPanel() {
		return view;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("Interactive View");
		dockable.setTitleIcon(KReaturesWindow.get().getIcons().get("monitor"));
		
	}

}
