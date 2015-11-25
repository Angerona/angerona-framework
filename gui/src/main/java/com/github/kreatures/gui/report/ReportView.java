package com.github.kreatures.gui.report;

import com.github.kreatures.gui.base.View;
import com.github.kreatures.core.report.ReportListener;

/**
 * A ReportView extends the {@link View} interface by a 
 * {@link ReportListener}, such that it can receive updates
 * of the report.
 * 
 * @author Tim Janus
 */
public interface ReportView extends View, ReportListener{
	
}
