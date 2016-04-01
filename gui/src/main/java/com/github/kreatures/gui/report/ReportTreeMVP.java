package com.github.kreatures.gui.report;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import bibliothek.gui.dock.DefaultDockable;

import com.github.kreatures.core.KReatures;
import com.github.kreatures.core.KReaturesEnvironment;
import com.github.kreatures.gui.KReaturesWindow;
import com.github.kreatures.gui.base.ViewComponent;
import com.github.kreatures.core.listener.SimulationAdapter;
import com.github.kreatures.core.report.Report;

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
	 * view is exposed to KReatures.
	 */
	public ReportTreeMVP() {
		// generate view and presenter
		view = new ReportTreeView();
		presenter = new ReportPresenter(
				KReatures.getInstance().getActualReport(), 
				view);
		
		KReatures.getInstance().addSimulationListener(this);
	}
	
	@Override
	public void simulationStarted(KReaturesEnvironment environment) {
		final Report curModel = KReatures.getInstance().getReport(environment);
		
		// wait for swing event to finish: By setting the model
		// the listeners of the MVP component are connected, if this would run
		// Asynchronously the KReatures simulation may generate reports 
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
		dockable.setTitleIcon(KReaturesWindow.get().getIcons().get("monitor"));
		
	}
}
