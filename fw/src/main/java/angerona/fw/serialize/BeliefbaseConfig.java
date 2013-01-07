package angerona.fw.serialize;

/**
 * Interface for serializable simple xml classes of the agent configuration file.
 * It contains all the information needed to define a belief base: The full java
 * class name of the belief base implementation and three operation sets. One for
 * the belief operators, one for the change operators and one for the translators
 * of the belief base. It also contains a name to identifiy the belief base 
 * configuration.
 * @author Tim Janus
 */
public interface BeliefbaseConfig {
	public OperationSetConfig getReasoners();
	
	public OperationSetConfig getChangeOperators();
	
	public OperationSetConfig getTranslators();
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName();
	
	/** @return the name of the configuration */
	public String getName();
}
