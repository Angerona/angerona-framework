package angerona.fw.report;

import angerona.fw.AngeronaEnvironment;

/**
 * Every class which implement this interface can be used as information 
 * provider for the report mechanism of Angerona.
 * 
 * @author Tim Janus
 */
public interface ReportPoster {
	/** @return a reference to the simulation which was posting the report */
	AngeronaEnvironment getSimulation();
	
	/** @return a more detailed name of the poster like the operator name */
	String getPosterName();
}
