package angerona.fw.util;

/**
 * Every class which implement this interface can be used as information 
 * provider for the report mechanism of Angerona
 * 
 * @author Tim Janus
 */
public interface ReportPoster {
	/** @return the simulation cylce the report was posted */
	int getSimTick();
	
	/** @return the name of the simulation which was posting the report */
	String getSimulationName();
	
	/** @return a more detailed name of the poster like the operator name */
	String getPosterName();
}
