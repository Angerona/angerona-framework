package angerona.fw.serialize;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import angerona.fw.serialize.perception.PerceptionDO;
import angerona.fw.serialize.perception.QueryDO;

/**
 * Class instance holding all the configuration options for a complete simulation.
 * 
 * @author Tim Janus
 */
@Root(name="simulation-configuration")
public class SimulationConfiguration {
	
	/** name of the simulation */
	@Element(name="name")
	private String name;
	
	/** collection of data structure containing the agents of the simulation */
	@ElementList(name="agents", inline=true)
	private List<AgentInstance> agents = new LinkedList<AgentInstance>();
	
	@ElementList(name="perception", entry="perception", inline=true, empty=false, required=false)
	private List<PerceptionDO> perceptions = new LinkedList<PerceptionDO>();
	
	/** @return name of the simulation */
	public String getName() {
		return name;
	}

	/** @return collection of data structure containing the agents of the simulation */
	public List<AgentInstance> getAgents() {
		return Collections.unmodifiableList(agents);
	}
	
	/** @return collection of perceptions which should be fired initially into the simulation */
	public List<PerceptionDO> getPerceptions() {
		return Collections.unmodifiableList(perceptions);
	}
	
	/**
	 * Helper method: Loads the simulation configuration data structure from the given filename
	 * @param source 	reference to the file containing the simulation configuration.
	 * @return an object containing the simulation-configuration saved in the given file.
	 */
	public static SimulationConfiguration loadXml(File source) {
		return SerializeHelper.loadXml(SimulationConfiguration.class, source);
	}
	
	public static void main(String [] args)  {
		SimulationConfiguration conf = new SimulationConfiguration();
		conf.name = "SCM";
		AgentInstance agent = AgentInstance.getTestObject();
		conf.agents.add(agent);
		
		conf.perceptions.add(QueryDO.getTestObject());
		SerializeHelper.outputXml(conf, System.out);
	}
}
