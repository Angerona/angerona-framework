package com.github.kreatures.gui.project;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreatures.core.KReatures;
import com.github.kreatures.gui.KReaturesWindow;
import com.github.kreatures.gui.base.ViewComponent;
import com.github.kreatures.gui.docking.DecoratorLibrary;

/**
 * Encapsulates the project tree view as a ViewComponent. The same technique can
 * be used for UI plug-ins to provide ViewComponents that support the MVP
 * pattern.
 * 
 * @author Tim Janus
 * 
 */
public class ProjectTreeMVPComponent implements ViewComponent {

	private ProjectView view;

	public ProjectTreeMVPComponent() {
		view = new ProjectTreeView();
		new ProjectPresenter(KReatures.getInstance().getProject(), view);
	}

	@Override
	public JPanel getPanel() {
		return (JPanel) view.getRootComponent();
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleIcon(KReaturesWindow.get().getIcons().get("resources"));
		dockable.setTitleText("Project Explorer");
		DecoratorLibrary.closeDecorator.decorate(dockable);
	}

}
