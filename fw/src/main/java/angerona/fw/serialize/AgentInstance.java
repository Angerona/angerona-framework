package angerona.fw.serialize;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import angerona.fw.comm.SpeechAct;


/**
 * This class is responsible for the simple xml serialization of the agent
 * instances in a simulation. An agent instance contains the name of the
 * agent, a reference to the agent's configuration file, a reference to the
 * belief base configuration file of the belief base instantiated by the
 * agent, a default file name suffix for this belief base, a list of desires
 * a list of capabilities and a list key data pairs representing the initial 
 * configuration of the agent's components.
 * 
 * @author Tim Janus
 */
@Root(name="agent-instance")
public class AgentInstance {	
	/** the unique name of the agent */
	@Element(name="name")
	protected String name;
	
	/** the type of the agent (AI or User) */
	@Element(name="type", required=false)
	protected String type;
	
	/** a data structure with type information of the agents operators */
	@Element(name="agent-config", type=AgentConfigImport.class)
	protected AgentConfig config;
	
	/** data structure containing information about the belief base type and operators */
	@Element(name="beliefbase-config", type=BeliefbaseConfigImport.class)
	protected BeliefbaseConfig beliefbaseConfig;
	
	@ElementMap(entry="view-config", required=false, key="agent", attribute=true, 
			empty=false, inline=true, valueType=File.class)
	protected Map<String, File> fileViewMap = new HashMap<>();

	protected Map<String, BeliefbaseConfig> realViewMap = new HashMap<>();
	
	/** the file prefix identifying the belief base */
	@Element(name="beliefbase-name", required=false)
	protected String alternativeBBName;
	
	/** list of fol-formulas representing the initial desires of the agent */
	@ElementList(entry="desire", inline=true, required=false, empty=false)
	protected List<FolFormula> desires = new LinkedList<FolFormula>();
	
    /** list of speech-acts the agent is said to perform*/
	@ElementList(entry="script", inline=true, required=false, empty=false)
	protected List<SpeechAct> actions = new LinkedList<SpeechAct>();
	
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
		return realViewMap.get(agName);
	}
	
	public List<FolFormula> getDesires() {
		return Collections.unmodifiableList(desires);
	}
	
	public List<String> getCapabilities() {
		return Collections.unmodifiableList(capabilities);
	}
	
	public Map<String, String> getAdditionalData() {
		return Collections.unmodifiableMap(additionalData);
	}

	public String getBeliefbaseName() {
		return alternativeBBName == null ? getName() : alternativeBBName;
	}
	
	public List<SpeechAct> getActions(){
		return Collections.unmodifiableList(actions);
	}
	
	public String getType(){
		return type;
	}
}
