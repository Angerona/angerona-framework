package angerona.fw.serialize;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Commit;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

import angerona.fw.Perception;

/**
 * Class instance holding all the configuration options for a complete simulation.
 * 
 * @author Tim Janus
 */
@Root(name="simulation-configuration")
public class SimulationConfiguration {
	
	/** name of the simulation */
	@Element(name="name")
	protected String name;
	
	/** A multi level category name where '/' indicates that a new category level begins 
	 *	scm/asp is a category with two levels which is represented by two nodes in a tree
	 *	view for example. An empty string indicates no category. 
	 */
	@Element(name="category", required=false)
	protected String category = "";
	
	/** used behavior implementation */
	@Element(name="behavior", required=false)
	protected String behaviorCls;
	
	/** collection of data structure containing the agents of the simulation */
	@ElementList(name="agents", inline=true)
	protected List<AgentInstance> agents = new LinkedList<AgentInstance>();
	
	@ElementList(name="perception", entry="perception", inline=true, empty=false, required=false)
	protected List<Perception> perceptions = new LinkedList<Perception>();
	
	
	protected File filepath;
	
	public File getFile() {
		return filepath;
	}
	
	public void setFile(File file) {
		this.filepath = file;
	}
	
	/** @return name of the simulation */
	public String getName() {
		return name;
	}

	/**
	 * @return 	A multi level category name where '/' indicates that a new category level begins 
	 *			scm/asp is a category with two levels which is represented by two nodes in a tree
	 *			view for example. An empty string indicates no category.
	 */
	public String getCategory() {
		return category;
	}
	
	/** @return the name of the class of the used behavior implementation */
	public String getBehaviorCls() {
		return behaviorCls;
	}
	
	/** @return collection of data structure containing the agents of the simulation */
	public List<AgentInstance> getAgents() {
		return Collections.unmodifiableList(agents);
	}
	
	/** @return collection of perceptions which should be fired initially into the simulation */
	public List<Perception> getPerceptions() {
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
	
	@Validate
	public void validate() throws PersistenceException {
		
		// test for duplicate agent names:
		Set<String> agentNames = new HashSet<>();
		for(AgentInstance ai: agents) {
			if(agentNames.contains(ai.getName())) {
				throw new PersistenceException("Duplicate unique agent name: '%s'", ai.getName());
			}
			agentNames.add(ai.getName());
		}

		// test if agent mapping view-beliefbase-config are functional
		for(AgentInstance ai : agents) {
			if(!agentNames.containsAll(ai.fileViewMap.keySet())) {
				Set<String> notMapped = new HashSet<>(ai.fileViewMap.keySet());
				notMapped.removeAll(agentNames);
				throw new PersistenceException("The view-beliefbase-config of agent '%s'" + 
						" cannot be mapped cause an agents with names '%s' does not exist",
						ai.getName(), notMapped);
			}
		}
	}
	
	@Commit
	public void commit() {
		for(AgentInstance ai : agents) {
			for(String viewedAgent : ai.fileViewMap.keySet()) {
				File f = ai.fileViewMap.get(viewedAgent);
				BeliefbaseConfigReal conf = SerializeHelper.loadXml(
						BeliefbaseConfigReal.class, f);
				ai.realViewMap.put(viewedAgent, conf);
			}
		}
	}
}
