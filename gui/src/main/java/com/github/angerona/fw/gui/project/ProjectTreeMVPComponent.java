package com.github.angerona.fw.gui.project;

import javax.swing.JPanel;

import bibliothek.gui.dock.DefaultDockable;

import com.github.angerona.fw.Angerona;
import com.github.angerona.fw.gui.AngeronaWindow;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.gui.docking.DecoratorLibrary;

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
		new ProjectPresenter(Angerona.getInstance().getProject(), view);
	}

	@Override
	public JPanel getPanel() {
		return (JPanel) view.getRootComponent();
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleIcon(AngeronaWindow.get().getIcons().get("resources"));
		dockable.setTitleText("Project Explorer");
		DecoratorLibrary.closeDecorator.decorate(dockable);
	}

}
