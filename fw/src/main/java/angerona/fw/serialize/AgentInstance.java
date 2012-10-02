package angerona.fw.serialize;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;


/**
 * An instance of this class represents one agent in the simulation
 * @author Tim Janus
 */
@Root(name="agent-instance")
public class AgentInstance {		
	/** the unique name of the agent */
	@Element(name="name")
	private String name;
	
	/** a data structure with type information of the agents operators */
	@Element(name="config")
	private AgentConfig config;
	
	/** data structure containing information about the belief base type and operators */
	@Element(name="beliefbase-config")
	private BeliefbaseConfig beliefbaseConfig;

	/** the file suffix identifying the belief base */
	@Element(name="default-suffix")
	private String fileSuffix;

	/** list of fol-formulas representing the initial desires of the agent */
	@ElementList(entry="desire", inline=true, required=false, empty=false)
	private List<Atom> desires = new LinkedList<Atom>();
	
	@ElementList(entry="skill", inline=true, required=false, empty=false)
	private List<String> skills;
	
	@ElementMap(key="key", entry="data", attribute=true, inline=true, empty=false, required=false)
	private Map<String, String> additionalData = new HashMap<String, String>();

	/** @return the unique name of the agent */
	public String getName() {
		return name;
	}

	/** @return a data structure with type information of the agents operators */
	public AgentConfig getConfig() {
		return config;
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

	public static AgentInstance getTestObject() {
		AgentInstance test = new AgentInstance();
		test.name = "Employee";
		test.additionalData.put("confidential", "(blub, bla, TRUE)");
		test.fileSuffix = "asp";
		test.config = AgentConfigImport.getTestObject();
		test.beliefbaseConfig = BeliefbaseConfigImport.getTestObject();
		test.desires.add(new Atom(new Predicate("attend_scm")));
		
		return test;
	}
}
