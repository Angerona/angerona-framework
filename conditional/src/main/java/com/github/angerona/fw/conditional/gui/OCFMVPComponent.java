package com.github.angerona.fw.conditional.gui;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.angerona.fw.gui.AngeronaWindow;
import com.github.angerona.fw.gui.base.ViewComponent;

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
