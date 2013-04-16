package angerona.fw.serialize;

import org.simpleframework.xml.Root;

/**
 * Interface for serializable simple xml classes of the agent configuration file.
 * It contains all the information needed to define a belief base: The full java
 * class name of the belief base implementation and three operation sets. One for
 * the belief operators, one for the change operators and one for the translators
 * of the belief base. It also contains a name to identifiy the belief base 
 * configuration.
 * @author Tim Janus
 */
@Root(name="entry")
public interface BeliefbaseConfig extends Resource {
	public static final String RESOURCE_TYPE = "Beliefbase-Configuration";
	
	public OperationSetConfig getReasoners();
	
	public OperationSetConfig getChangeOperators();
	
	public OperationSetConfig getTranslators();
	
	/** @return the class name of the beliefbase */
	public String getBeliefbaseClassName();
	
	/** @return the name of the configuration */
	public String getName();
	
	/** 
	 * @return 	null if the config is for a world belief base or if it
	 * 			is the view onto another agent then the name of the
	 * 			viewed agent is returned.
	 */
	public String getViewOn();
}
