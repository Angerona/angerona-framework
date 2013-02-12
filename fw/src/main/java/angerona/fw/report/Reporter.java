package angerona.fw.report;

import angerona.fw.internal.Entity;

/**
 * Classes implementing this interface provide methods 
 * to use the report system of Angerona to 
 * inform about changes.
 * @author Tim Janus
 *
 */
public interface Reporter {
	void report(String message);
	
	void report(String message, Entity attachment);
}
