package angerona.fw.gui.project;

import javax.swing.JPanel;

import angerona.fw.Angerona;
import angerona.fw.gui.base.ViewComponent;

/**
 * Encapsulates the project tree view as a ViewComponent. The same
 * technique can be used for UI plugins to provide ViewComponents that
 * support the MVP pattern.
 * 
 * @author Tim Janus
 *
 */
public class ProjectTreeMVPComponent implements ViewComponent{
	
	private ProjectView view;
	
	public ProjectTreeMVPComponent() {
		view = new ProjectTreeView();
		new ProjectPresenter(Angerona.getInstance().getProject(), view);
	}
	
	@Override
	public JPanel getPanel() {
		return (JPanel)view.getRootComponent();
	}

	@Override
	public String getDefaultTitle() {
		return "Project Explorer";
	}

}
