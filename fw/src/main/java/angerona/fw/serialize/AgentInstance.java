package angerona.fw.serialize;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;


/**
 * This class is responsible for the simple xml serialization of the agent
 * instances in a simulation. An agent instance contains the name of the
 * agent, a reference to the agent's configuration file, a reference to the
 * belief base configuration file of the belief base instantiated by the
 * agent, a default file name suffix for this belief base, a list of desires
 * a list of capabilities and a list key data pairs representing the initial configuration
 * of the agent's components.
 * @author Tim Janus
 */
@Root(name="agent-instance")
public class AgentInstance {	
	
	@Root(name="view-beliefbase-config")
	protected static class ViewBeliefbaseConfig extends BeliefbaseConfigImport{
		@Attribute 
		public String agentName;
	}
	
	/** the unique name of the agent */
	@Element(name="name")
	protected String name;
	
	/** a data structure with type information of the agents operators */
	@Element(name="agent-config", type=AgentConfigImport.class)
	protected AgentConfig config;
	
	/** data structure containing information about the belief base type and operators */
	@Element(name="beliefbase-config", type=BeliefbaseConfigImport.class)
	protected BeliefbaseConfig beliefbaseConfig;
	
	@ElementList(entry="view-beliefbase-config", type=ViewBeliefbaseConfig.class, inline=true, required=false)
	protected List<ViewBeliefbaseConfig> viewBeliefbaseConfigs = new LinkedList<>();

	/** the file suffix identifying the belief base */
	@Element(name="beliefbase-name")
	protected String filePrefix;
	
	/** list of fol-formulas representing the initial desires of the agent */
	@ElementList(entry="desire", inline=true, required=false, empty=false)
	protected List<Atom> desires = new LinkedList<Atom>();
	
	@ElementList(entry="capability", inline=true, required=false, empty=false)
	protected List<String> capabilities = new LinkedList<>();
	
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
	
	public BeliefbaseConfig getBeliefbaseConfig() {
		return beliefbaseConfig;
	}
	
	public BeliefbaseConfig getBeliefBaseConfig(String agName) {
		for(ViewBeliefbaseConfig vbbc : viewBeliefbaseConfigs) {
			if(vbbc.agentName.equals(agName)) {
				return vbbc;
			}
		}
		return null;
	}
	
	public List<Atom> getDesires() {
		return Collections.unmodifiableList(desires);
	}
	
	public List<String> getCapabilities() {
		return Collections.unmodifiableList(capabilities);
	}
	
	public Map<String, String> getAdditionalData() {
		return Collections.unmodifiableMap(additionalData);
	}

	public String getFileSuffix() {
		return filePrefix;
	}
}
