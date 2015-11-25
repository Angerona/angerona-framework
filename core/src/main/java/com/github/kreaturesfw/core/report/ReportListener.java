package com.github.kreaturesfw.core.report;


/**
 * Informs about reports received from a simulation of the Angerona 
 * framework.
 * 
 * @author Tim Janus
 */
public interface ReportListener {
	/**
	 * is called if a new report entry was posted to the simulation.
	 * @param entry	reference to the new report entry.
	 */
	void reportReceived(ReportEntry entry);
}
