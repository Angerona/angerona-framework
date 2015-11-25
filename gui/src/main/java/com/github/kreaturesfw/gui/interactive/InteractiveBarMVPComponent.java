package com.github.kreaturesfw.gui.interactive;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreaturesfw.core.AngeronaEnvironment;
import com.github.kreaturesfw.gui.AngeronaWindow;
import com.github.kreaturesfw.gui.base.ViewComponent;

/**
 * 
 * @author Pia Wierzoch
 */
public class InteractiveBarMVPComponent implements ViewComponent {

	private InteractiveBar view;

	
	public InteractiveBarMVPComponent(AngeronaEnvironment simulationEnvironment, Thread caller) {
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
		dockable.setTitleIcon(AngeronaWindow.get().getIcons().get("monitor"));
		
	}

}
