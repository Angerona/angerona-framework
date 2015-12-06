package com.github.kreaturesfw.gui.report;

import com.github.kreaturesfw.core.report.ReportListener;
import com.github.kreaturesfw.gui.base.View;

/**
 * A ReportView extends the {@link View} interface by a 
 * {@link ReportListener}, such that it can receive updates
 * of the report.
 * 
 * @author Tim Janus
 */
public interface ReportView extends View, ReportListener{
	
}
