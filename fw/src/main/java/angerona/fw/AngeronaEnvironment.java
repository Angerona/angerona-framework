package angerona.fw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.beenuts.ap.AgentProcess;
import net.sf.beenuts.apr.APR;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.AgentIdException;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.internal.Entity;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.logic.Beliefs;
import angerona.fw.serialize.AgentInstance;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * A simulation environment for Angerona. This is actually only used for some functional tests.
 * @author Tim Janus
 */
public class AngeronaEnvironment extends APR {

	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(AngeronaEnvironment.class);
	
	/** the actual simulation tick */
	protected int tick = 0;
	
	/** the name of the simulation */
	private String name;
	
	/** flag indicating if the environment is currently in its update process */
	protected boolean doingTick = false;
	
	/** flag indicating if the environment is correctly initialized */
	private boolean ready = false;
	
	/** a map of entity ids to the entity references */
	private Map<Long, Entity> entities = new HashMap<Long, Entity>();
	
	/** the behavior of the environment, allows different communication protocols and external simulations */
	private EnvironmentBehavior behavior;
	
	/** the root folder of the actual loaded simulation in this environment */
	private String simDirectory;
	
	/**
	 * @return a map of ID --> Entity, the map is not modifiable.
	 */
	public Map<Long, Entity> getEntityMap() {
		return Collections.unmodifiableMap(entities);
	}
	
	/**
	 * Default Ctor: Generates the default-behavior.
	 */
	public AngeronaEnvironment() {
	}
	
	/**
	 * @return a set of strings containing all agent names.
	 */
	public Set<String> getAgentNames() {
		return agentMap.keySet();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * @return the name of the simulation.
	 */
	public String getName() {
		return name;
	}
	
	/** @ return a string identifing the root directory of the actual 
	 * 			 running simulation or null if no simulation is initialized. 
	 */
	public String getDirectory() {
		return simDirectory;
	}
	
	/**
	 * Adds the agents with the given name to the environment
	 * @param ap		agent process handling low level communication through the environment.
	 * @return	true if everything was fine, false if the same agent process was already registered.
	 * @throws AgentIdException Is thrown if the name of the agent process is not unique (two processes have the same name).
	 */
	public boolean addAgent(AngeronaAgentProcess ap) throws AgentIdException {
		if(agentMap.containsKey(ap.getName())) {
			if(agentMap.get(ap.getName()) == ap)
				return false;
			
			throw new AgentIdException("agent with name: " + ap.getName() + " already registered.");
		}
		
		agentMap.put(ap.getName(), ap);
		agents.add(ap);
		ap.setAPR(this);
		return true;
	}
	
	/**
	 * Gets the high level agent with the given name
	 * @param name		unique name of the agent.
	 * @return			Reference to the agent called 'name', if no agent with the given name exists null is returned.
	 */
	public Agent getAgentByName(String name) {
		AgentProcess ap = agentMap.get(name);
		if(ap == null)
			return null;
		return (Agent)ap.getAgentArchitecture();
	}
	
	/**
	 * runs the simulation using the behavior given at construction.
	 */
	public boolean run() {
		return behavior.run(this);
	}
	
	/**
	 * runs one simulation tick. Gives every agent the ability to call its cycle method.
	 * @return true if at least one agents cylce function was called, false otherwise.
	 */
	public boolean runOneTick() {
		return behavior.runOneTick(this);
	}
	
	/** @return	true if the environment is actually performing a tick, false otherwise. */
	public boolean isDoeingTick() {
		return doingTick;
	}
	
	/** @return true if the simulation is initialized (after the call of initSimulation), false otherwise. */
	public boolean isReady() {
		return ready;
	}
	
	/**
	 * Loads a simulation from the given filename
	 * @param filename	name of the xml file containing the configuration of the simulation.
	 * @return the loaded simulation configuration if no error occurred, null otherwise.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public SimulationConfiguration loadSimulation(String filename) throws FileNotFoundException, IOException {
		return loadSimulation(filename, true);
	}
	
	/**
	 * Loads a simulation from the given filename
	 * @param filename	name of the xml file containing the configuration of the simulation.
	 * @param startImmediately	flag indicating if the simulation defined in the file should be started
	 * 							Immediately after loading the file.
	 * @return the loaded simulation configuration if no error occurred, null otherwise.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public SimulationConfiguration loadSimulation(String filename, boolean startImmediately) throws FileNotFoundException, IOException {
		SimulationConfiguration config = null;
		
		config = SimulationConfiguration.loadXml(new File(filename));	
		name = config.getName();
		
		if(config != null && startImmediately) {
			File f = new File(filename);
			String parentDir = f.getParent();
			return initSimulation(config, parentDir) ? config : null;
		}
		return config;
	}
	
	/**
	 * Initializes an Angerona simulation, with the given config. The root directory of the simulation is also given.
	 * @param config	reference to the data-structure containing the configuration of the simulation.
	 * @param simulationDirectory	the root folder for the simulation.
	 * @return	true if everything was fine, false if an error occurred.
	 */
	public boolean initSimulation(SimulationConfiguration config, String simulationDirectory) {
		LOG.info("Starting simulation: " + config.getName());
		this.simDirectory = simulationDirectory;
		if(!createBehavior(config))
			return false;
		
		Angerona.getInstance().onCreateSimulation(this);
		tick = 0;
		String errorOutput = "";
		try {
			for(AgentInstance ai : config.getAgents()) {
				Agent highLevelAg = new Agent(ai.getName());
				addAgent(highLevelAg.getAgentProcess());		
				LOG.info("Start the creation of Agent '{}'.", ai.getName());
				highLevelAg.create(ai);
				
				Beliefs b = highLevelAg.getBeliefs();
				BaseBeliefbase world = b.getWorldKnowledge();
				entities.put(highLevelAg.getGUID(), highLevelAg);
				entities.put(world.getGUID(), world);
				for(BaseBeliefbase actView : b.getViewKnowledge().values()) {
					entities.put(actView.getGUID(), actView);
				}
				
				for(AgentComponent comp : highLevelAg.getComponents()) {
					entities.put(comp.getGUID(), comp);
				}
				LOG.info("Agent '{}' fully registered.", highLevelAg.getName());
			}
		} catch (AgentIdException e) {
			errorOutput = "Cannot init simulation, something went wrong during agent registration: " + e.getMessage();
			e.printStackTrace();
		} catch (AgentInstantiationException e) {
			errorOutput = "Cannot init simulation, something went wrong during agent instatiation: " + e.getMessage();
			e.printStackTrace();
		}

		for(String agentName : getAgentNames()) {
			Agent ag = getAgentByName(agentName);
			for(String viewName : ag.getBeliefs().getViewKnowledge().keySet()) {
				if(!getAgentNames().contains(viewName)) {
					errorOutput = "Cannot init simulation: The agent '"+agentName+"' has a view on agent '"+viewName+"' but this agent does not exists.";
					break;
				}
			}
		}
		
		if(!errorOutput.isEmpty()) {
			LOG.error(errorOutput);
			this.cleanupSimulation();
			Angerona.getInstance().onError("Simulation Initialization", errorOutput);
			return false;
		}
		
		Angerona.getInstance().onNewSimulation(this);
		
		// report the initialized data of the agent to the report system.
		for(String agName : agentMap.keySet()) {
			Agent agent = getAgentByName(agName);
			agent.reportCreation();
		}
		
		for(Perception p : config.getPerceptions()) {
			if(p instanceof Action) {
				this.sendAction(p.getReceiverId(), (Action)p);
			}
		}
		
		return ready = true;
	}

	/**
	 * Helper method: Creates the correct behavior class with the given class name
	 * form the simulations xml file.
	 * @param config		reference to the config.
	 * @return				true if the creation was successful, false otherwise.
	 */
	private boolean createBehavior(SimulationConfiguration config) {
		if(config.getBehaviorCls() != null) {
			String error = null;
			try {
				behavior = PluginInstantiator.getInstance().createEnvironmentBehavior(
						config.getBehaviorCls());
			} catch (InstantiationException e) {
				e.printStackTrace();
				error = e.getMessage();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				error = e.getMessage();
			}
			
			if(error != null) {
				Angerona.getInstance().onError("Simulation initialization", error);
				LOG.error(error);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * deletes all agents from the environment and removes the information about the last simulation.
	 */
	public void cleanupSimulation() {
		agents.clear();
		agentMap.clear();
		ready = false;
		Angerona.getInstance().onSimulationDestroyed(this);
	}
	
	@Override
	public void sendAction(String agentName, Object action) {
		behavior.sendAction(this, (Action)action);
	}

	public int getSimulationTick() {
		return tick;
	}
}
