package com.github.kreaturesfw.gui.report;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.kreaturesfw.core.report.Report;
import com.github.kreaturesfw.core.report.ReportListener;
import com.github.kreaturesfw.gui.base.Presenter;

/**
 * The ReportPresenter links a Report (data-model) to an
 * implementation of the {@link ReportView}. For this
 * it links different events like the {@link ReportListener}
 * 
 * @author Tim Janus
 */
public class ReportPresenter 
	extends Presenter<Report, ReportView> 
	implements ActionListener
	{
	
	public ReportPresenter(Report model, ReportView view) {
		setModel(model);
		setView(view);
	}

	@Override
	protected void forceUpdate() {
	}

	@Override
	protected void wireViewEvents() {
		model.addReportListener(view);
	}

	@Override
	protected void unwireViewEvents() {
		model.removeReportListener(view);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// does nothing
	}

}
