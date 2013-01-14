package angerona.fw.serialize;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

import angerona.fw.asml.CommandSequence;


/**
 * This class is responsible for the simple xml serialization of the agent
 * instances in a simulation. An agent instance contains the name of the
 * agent, a reference to the agent's configuration file, a reference to the
 * belief base configuration file of the belief base instantiated by the
 * agent, a default file name suffix for this belief base, a list of desires
 * a list of skills and a list key data pairs representing the initial configuration
 * of the agent's components.
 * @author Tim Janus
 */
@Root(name="agent-instance")
public class AgentInstance {		
	/** the unique name of the agent */
	@Element(name="name")
	protected String name;
	
	/** a data structure with type information of the agents operators */
	@Element(name="agent-config", type=AgentConfigImport.class)
	protected AgentConfig config;
	
	/** data structure containing information about the belief base type and operators */
	@Element(name="beliefbase-config", type=BeliefbaseConfigImport.class)
	protected BeliefbaseConfig beliefbaseConfig;

	/** the file suffix identifying the belief base */
	@Element(name="default-suffix")
	protected String fileSuffix;

	/** the link to the ASML script used to execute the agent's cycle. */
	@Element(name="cycle-script", type=CommandSequenceSerializeImport.class)
	protected CommandSequenceSerialize cylceScript;
	// TODO NEXT: Getter for cycleScript and validate method for testing if the type is correct.
	
	/** list of fol-formulas representing the initial desires of the agent */
	@ElementList(entry="desire", inline=true, required=false, empty=false)
	protected List<Atom> desires = new LinkedList<Atom>();
	
	@ElementList(entry="skill", inline=true, required=false, empty=false)
	protected List<String> skills = new LinkedList<>();
	
	@ElementMap(key="key", entry="data", attribute=true, inline=true, empty=false, required=false)
	protected Map<String, String> additionalData = new HashMap<String, String>();
	
	/** @return the unique name of the agent */
	public String getName() {
		return name;
	}

	/** @return a data structure with type information of the agents operators */
	public AgentConfig getConfig() {
		return config;
	}
	
	public CommandSequence getCycleScript() {
		return (CommandSequence)this.cylceScript;
	}
	
	public List<Atom> getDesires() {
		return Collections.unmodifiableList(desires);
	}
	
	public List<String> getSkills() {
		return Collections.unmodifiableList(skills);
	}
	
	public Map<String, String> getAdditionalData() {
		return Collections.unmodifiableMap(additionalData);
	}

	public BeliefbaseConfig getBeliefbaseConfig() {
		return beliefbaseConfig;
	}

	public String getFileSuffix() {
		return fileSuffix;
	}
	
	/**
	 * Checks if the loaded agent instance is valid.
	 * @throws PersistenceException
	 */
	@Validate
	public void validation() throws PersistenceException {
		/*if(!(this.cylceScript instanceof CommandSequence)) {
			throw new PersistenceException("cycle-script is not of type '%s' but of type '%s", 
					CommandSequence.class.getName(), 
					this.cylceScript == null ? "null" : this.cylceScript.getClass().getName());
		}*/
	}
}
