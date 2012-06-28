package angerona.fw.serialize;

import java.util.Set;

public interface BeliefbaseConfig {
	/** @return the class name used for the revision operation */
	public Set<String> getRevisionClassName();

	/** @return the class name used for the reasoning operations */
	public Set<String> getReasonerClassName();
	
	public String getDefaultReasonerClass();
	
	public String getDefaultChangeClass();
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName();
	
	/** @return the name of the configuration */
	public String getName();
}
