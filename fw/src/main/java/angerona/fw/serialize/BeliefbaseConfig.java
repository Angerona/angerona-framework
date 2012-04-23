package angerona.fw.serialize;

public interface BeliefbaseConfig {
	/** @return the class name used for the revision operation */
	public String getRevisionClassName();

	/** @return the class name used for the reasoning operations */
	public String getReasonerClassName();
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName();
	
	/** @return the name of the configuration */
	public String getName();
}
