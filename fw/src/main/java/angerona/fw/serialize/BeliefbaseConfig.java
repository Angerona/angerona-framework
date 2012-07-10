package angerona.fw.serialize;


public interface BeliefbaseConfig {
	public OperatorSetConfig getReasoners();
	
	public OperatorSetConfig getChangeOperators();
	
	public OperatorSetConfig getTranslators();
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName();
	
	/** @return the name of the configuration */
	public String getName();
}
