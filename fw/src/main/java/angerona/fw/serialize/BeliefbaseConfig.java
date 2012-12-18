package angerona.fw.serialize;


public interface BeliefbaseConfig {
	public OperationSetConfig getReasoners();
	
	public OperationSetConfig getChangeOperators();
	
	public OperationSetConfig getTranslators();
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName();
	
	/** @return the name of the configuration */
	public String getName();
}
