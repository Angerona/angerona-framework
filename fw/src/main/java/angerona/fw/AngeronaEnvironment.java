package angerona.fw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.AgentIdException;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.internal.Entity;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.Desires;
import angerona.fw.serialize.AgentInstance;
import angerona.fw.serialize.SimulationConfiguration;
import angerona.fw.util.ObservableMap;

/**
 * A simulation environment for Angerona. This is actually only used for some functional tests.
 * @author Tim Janus
 */
public class AngeronaEnvironment  {

	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(AngeronaEnvironment.class);
	
	/** the name of the simulation */
	private String name;
		
	/** flag indicating if the environment is correctly initialized */
	private boolean ready = false;
	
	/** a map of entity ids to the entity references */
	private Map<Long, Entity> entities = new HashMap<Long, Entity>();
	
	/** the behavior of the environment, allows different communication protocols and external simulations */
	private EnvironmentBehavior behavior;
	
	/** the root folder of the actual loaded simulation in this environment */
	private String simDirectory;
	
	private ObservableMap<String, Agent> agentMap = new ObservableMap<>("agentMap");
	
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
	
	/** @return 	a string identifying the root directory of the actual 
	 * 			 	running simulation or null if no simulation is initialized. 
	 */
	public String getDirectory() {
		return simDirectory;
	}
	
	/**
	 * Adds the agents with the given name to the environment
	 * @param agent	
	 * @return	true if everything was fine, false if the same agent process was already registered.
	 * @throws AgentIdException Is thrown if the name of the agent process is not unique (two processes have the same name).
	 */
	public boolean addAgent(Agent agent) throws AgentIdException {
		if(agentMap.containsKey(agent.getName())) {
			if(agentMap.get(agent.getName()) == agent)
				return false;
			
			throw new AgentIdException("agent with name: '" + agent.getName() + "' already registered.");
		}
		
		agentMap.put(agent.getName(), agent);
		return true;
	}
	
	public Collection<Agent> getAgents() {
		return Collections.unmodifiableCollection(agentMap.values());
	}
	
	/**
	 * Gets the high level agent with the given name
	 * @param name		unique name of the agent.
	 * @return			Reference to the agent called 'name', if no agent with the given name exists null is returned.
	 */
	public Agent getAgentByName(String name) {
		return agentMap.get(name);
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
		return behavior.isDoingTick();
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
			return initSimulation(config) ? config : null;
		}
		return config;
	}
	
	/**
	 * Initializes an Angerona simulation, with the given config. The root directory of the simulation is determined from the configuration file.
	 * @param config	reference to the data-structure containing the configuration of the simulation.
	 * @return	true if everything was fine, false if an error occurred.
	 */
	public boolean initSimulation(SimulationConfiguration config) {
		LOG.info("Starting simulation: " + config.getName());
		this.simDirectory = config.getFile().getParent();

		// inform listener of start of simulation creation:
		Angerona.getInstance().onCreateSimulation(this);
		
		if(!createBehavior(config))
			return false;
		
		if(!registerAgents(config))
			return false;
		
		if(!createAgents(config))
			return false;
				
		Angerona.getInstance().onNewSimulation(this);
		
		// report the initialized data of the agent to the report system.
		for(AgentInstance ai : config.getAgents()) {
			Agent agent = getAgentByName(ai.getName());
			agent.reportCreation();
			
			// and initialize the desires:
			for(FolFormula a : ai.getDesires()) {
				agent.getComponent(Desires.class).add(new Desire(a));
			}
		}
		
		// post the initial perceptions defined in the simulation configuration
		// file to the environment.
		for(Perception p : config.getPerceptions()) {
			if(p instanceof Action) {
				this.sendAction(p.getReceiverId(), (Action)p);
			}
		}
		
		return ready = true;
	}

	/**
	 * Create the agents defined in the simulation configuration. That means it
	 * instantiates the different agent components and registers those as entitys
	 * to the entity-system. 
	 * @remark 	The method assumes that all the agent instances in the config are
	 * 			already registered. That means the java objects exists in the
	 * 			environment, although they are not fully initalized yet.
	 * @param config
	 * @return 	True if the creation (instatiation) of the agents is successful or false
	 * 			if an error occurred during the creation.
	 */
	private boolean createAgents(SimulationConfiguration config) {
		for(AgentInstance ai : config.getAgents()) {
			try {
				// First instantiate the agent components:
				Agent agent = getAgentByName(ai.getName());		
				LOG.info("Start the creation of Agent '{}'.", ai.getName());
				agent.create(ai, config);
				
				// Second: Register the different agent components to the entity system:
				Beliefs b = agent.getBeliefs();
				BaseBeliefbase world = b.getWorldKnowledge();
				entities.put(agent.getGUID(), agent);
				entities.put(world.getGUID(), world);
				for(BaseBeliefbase actView : b.getViewKnowledge().values()) {
					entities.put(actView.getGUID(), actView);
				}
				
				for(AgentComponent comp : agent.getComponents()) {
					entities.put(comp.getGUID(), comp);
				}
				
				LOG.info("Agent '{}' successfully created and registered.", agent.getName());
			} catch (AgentInstantiationException e) {
				errorDelegation("Cannot init simulation, something went wrong during agent instatiation: " + 
						e.getMessage());
				e.printStackTrace();
				return false;
			}
		} 
		return true;
	}

	/***
	 * Helper method: Registers all the agents in the simulation configuration as java
	 * objects to the environment. The agents are not fully initalized after the method
	 * returned successfully.
	 * @param config
	 * @return	true if the registration process is successful or false if an error occurred 
	 * 			during initialization.
	 */
	private boolean registerAgents(SimulationConfiguration config) {
		try {
			for(AgentInstance ai : config.getAgents()) {
				Agent agent = new Agent(ai.getName(), this);
				addAgent(agent);
			}
		} catch (AgentIdException e) {
			e.printStackTrace();
			errorDelegation("Cannot init simulation, something went wrong during agent registration: " + e.getMessage());
			return false;
		}
		return true;
	}

	/** 
	 * Helper method: cleans the simulation up and delegates the error message to Delegates an error message to 
	 * two sources: The logging output and all interested Angerona Error Listeners.
	 * @param errorOutput	String containing the error message.
	 */
	protected void errorDelegation(String errorOutput) {
		this.cleanupEnvironment();
		LOG.error(errorOutput);
		Angerona.getInstance().onError("Simulation Initialization", errorOutput);
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
		} else {
			String error = "No behavior given in simulation configuration file.";
			Angerona.getInstance().onError("Simulation initialization", error);
			LOG.error(error);
			return false;
		}
		return true;
	}
	
	/**
	 * deletes all agents from the environment and removes the information about the last simulation.
	 */
	public void cleanupEnvironment() {
		agentMap.clear();
		ready = false;
		Angerona.getInstance().onSimulationDestroyed(this);
	}
	
	public void sendAction(String agentName, Object action) {
		behavior.sendAction(this, (Action)action);
	}

	public int getSimulationTick() {
		return behavior.getTick();
	}
}
