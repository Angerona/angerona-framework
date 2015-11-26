package com.github.kreaturesfw.gui.report;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreaturesfw.core.Angerona;
import com.github.kreaturesfw.core.legacy.AngeronaEnvironment;
import com.github.kreaturesfw.core.listener.SimulationAdapter;
import com.github.kreaturesfw.core.report.Report;
import com.github.kreaturesfw.gui.AngeronaWindow;
import com.github.kreaturesfw.gui.base.ViewComponent;

/**
 * A MVP component combining a {@link ReportTreeView} with the {@link Report}
 * data model to show the report in a tree like structure.
 * 
 * @author Tim Janus
 */
public class ReportTreeMVP 
	extends SimulationAdapter
	implements ViewComponent {
	
	/** the tree view that shows the report */
	private ReportTreeView view;
	
	/** the presenter that acts as manager and couples view and model */
	private ReportPresenter presenter;
	
	/** 
	 * Default-Ctor initializes the MVP pattern and ensures that the 
	 * view is exposed to Angerona.
	 */
	public ReportTreeMVP() {
		// generate view and presenter
		view = new ReportTreeView();
		presenter = new ReportPresenter(
				Angerona.getInstance().getActualReport(), 
				view);
		
		Angerona.getInstance().addSimulationListener(this);
	}
	
	@Override
	public void simulationStarted(AngeronaEnvironment environment) {
		final Report curModel = Angerona.getInstance().getReport(environment);
		
		// wait for swing event to finish: By setting the model
		// the listeners of the MVP component are connected, if this would run
		// Asynchronously the Angerona simulation may generate reports 
		// before the connection of the listeners is finished, and that means 
		// the view would drop those report-entries.
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					presenter.setModel(curModel);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public JPanel getPanel() {
		return view;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		// change tab title and icon:
		dockable.setTitleText("Report-View (Experimental)");
		dockable.setTitleIcon(AngeronaWindow.get().getIcons().get("monitor"));
		
	}
}
