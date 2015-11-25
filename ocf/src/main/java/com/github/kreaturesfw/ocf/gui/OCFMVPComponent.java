package com.github.kreaturesfw.ocf.gui;

import javax.swing.JPanel;

import com.github.kreaturesfw.gui.AngeronaWindow;
import com.github.kreaturesfw.gui.base.ViewComponent;

import bibliothek.gui.dock.DefaultDockable;

public class OCFMVPComponent implements ViewComponent {

	private OCFFrame view;
	
	public OCFMVPComponent() {
		view = new OCFFrame();
		OCFModel model = new OCFModelAdapter();
		new OCFPresenter(model, view);
	}
	
	@Override
	public JPanel getPanel() {
		return view;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("OCF Calculation");
		dockable.setTitleIcon(AngeronaWindow.get().getIcons().get("monitor"));
		
	}
}
