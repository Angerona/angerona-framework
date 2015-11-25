package com.github.kreatures.ocf.gui;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreatures.gui.KReaturesWindow;
import com.github.kreatures.gui.base.ViewComponent;

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
		dockable.setTitleIcon(KReaturesWindow.get().getIcons().get("monitor"));
		
	}
}
