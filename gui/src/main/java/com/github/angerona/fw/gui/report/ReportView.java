package com.github.angerona.fw.gui.report;

import com.github.angerona.fw.gui.base.View;
import com.github.angerona.fw.report.ReportListener;

/**
 * A ReportView extends the {@link View} interface by a 
 * {@link ReportListener}, such that it can receive updates
 * of the report.
 * 
 * @author Tim Janus
 */
public interface ReportView extends View, ReportListener{
	
}
