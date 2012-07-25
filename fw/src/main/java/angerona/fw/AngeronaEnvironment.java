package angerona.fw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.beenuts.ap.AgentProcess;
import net.sf.beenuts.apr.APR;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.AgentIdException;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.internal.DefaultPerceptionFactory;
import angerona.fw.internal.DetailPerceptionFactory;
import angerona.fw.internal.Entity;
import angerona.fw.internal.PerceptionFactory;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Desires;
import angerona.fw.parser.BeliefbaseSetParser;
import angerona.fw.parser.ParseException;
import angerona.fw.serialize.AgentInstance;
import angerona.fw.serialize.SimulationConfiguration;
import angerona.fw.serialize.perception.PerceptionDO;

/**
 * A simulation environment for Angerona. This is actually only used for some functional tests.
 * @author Tim Janus
 */
public class AngeronaEnvironment extends APR {

	private static Logger LOG = LoggerFactory.getLogger(AngeronaEnvironment.class);
	
	/** the actual simulation tick */
	protected int tick = 0;
	
	/** the name of the simulation */
	private String name;
	
	/** implementation of the factory used for perceptions */
	//I want this to be plugin-implemented
	//private PerceptionFactory perceptionFactory = new DefaultPerceptionFactory();
	private PerceptionFactory perceptionFactory = new DetailPerceptionFactory();
	
	/** flag indicating if the environment is currently in its update process */
	protected boolean doingTick = false;
	
	/** flag indicating if the environment is correctly initialized */
	private boolean ready = false;
	
	/** a map of entity ids to the entity references */
	private Map<Long, Entity> entities = new HashMap<Long, Entity>();
	
	private EnvironmentBehavior behavior;
	
	public Map<Long, Entity> getEntityMap() {
		return Collections.unmodifiableMap(entities);
	}
	
	public AngeronaEnvironment() {
		this.behavior = new DefaultBehavior();
	}
	
	public AngeronaEnvironment(EnvironmentBehavior behavior) {
		this.behavior = behavior;
	}
	
	public Set<String> getAgentNames() {
		return agentMap.keySet();
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getName() {
		return name;
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
	
	public boolean isDoeingTick() {
		return doingTick;
	}
	
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
		
		Angerona.getInstance().onCreateSimulation(this);
		PluginInstantiator pi = PluginInstantiator.getInstance();
		tick = 0;
		String errorOutput = "";
		try {
			for(AgentInstance ai : config.getAgents()) {
				Agent highLevelAg = new Agent(ai.getConfig(), ai.getName());
				entities.put(highLevelAg.getGUID(), highLevelAg);
				
				BaseBeliefbase world = pi.createBeliefbase(ai.getBeliefbaseConfig());
				entities.put(world.getGUID(), world);
				String fn = simulationDirectory + "/" + ai.getFileSuffix() + "." + world.getFileEnding();
				
				FileInputStream fis = new FileInputStream(new File(fn));
				BeliefbaseSetParser bbsp = new BeliefbaseSetParser(fis);
				bbsp.Input();
				fis.close();
				
				StringReader sr = new StringReader(bbsp.worldContent);
				world.parse(new BufferedReader(sr));
				
				// TODO: Move
				ConfidentialKnowledge conf = new ConfidentialKnowledge();
				entities.put(conf.getGUID(), conf);
				FolSignature fsig = new FolSignature();
				fsig.fromSignature(world.getSignature());
				
				Map<String, BaseBeliefbase> views = new HashMap<String, BaseBeliefbase>();
				
				for(String key : bbsp.viewContent.keySet()) {
					BaseBeliefbase actView = pi.createBeliefbase(ai.getBeliefbaseConfig());
					entities.put(actView.getGUID(), actView);
					sr = new StringReader(bbsp.viewContent.get(key));
					actView.parse(new BufferedReader(sr));
					views.put(key, actView);
					
				}
				
				for(AgentComponent comp : highLevelAg.getComponents()) {
					entities.put(comp.getGUID(), comp);
				}
				
				highLevelAg.setBeliefs(world, views);
				highLevelAg.addSkillsFromConfig(ai.getSkills());
				addAgent(highLevelAg.getAgentProcess());
				Desires desires = highLevelAg.getDesires();
				if(desires == null && ai.getDesires().size() > 0) {
					LOG.warn("No desire-component added to agent '{}' but desires, auto-add the desire component.", highLevelAg.getName());
					desires = new Desires();
					highLevelAg.addComponent(desires);
				}
				highLevelAg.initComponents(ai.getAdditionalData());
				if(desires != null) {
					for(Atom a : ai.getDesires()) {
						highLevelAg.getDesires().add(new Desire(a));
					}
				}		
				LOG.info("Agent '{}' added", highLevelAg.getName());
			}
		} catch (AgentIdException e) {
			errorOutput = "Cannot init simulation, something went wrong during agent registration: " + e.getMessage();
			e.printStackTrace();
		} catch (AgentInstantiationException e) {
			errorOutput = "Cannot init simulation, something went wrong during agent instatiation: " + e.getMessage();
			e.printStackTrace();
		} catch (InstantiationException e) {
			errorOutput = "Cannot init simulation, something went wrong during dynamic instantiation: " + e.getMessage();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			errorOutput = "Cannot init simulation, something went wrong during dynamic instantiation: " + e.getMessage();
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			errorOutput = "Cannot init simulation, referenced file not found: " + e.getMessage();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			errorOutput = "Cannot init simulation, parsing error occured: " + e.getMessage();
			e.printStackTrace();
		}
		
		if(!errorOutput.isEmpty()) {
			LOG.error(errorOutput);
			this.cleanupSimulation();
			Angerona.getInstance().onError("Simulation Initialization", errorOutput);
			return false;
		}
		
		Angerona.getInstance().onNewSimulation(this);
		// report the initialized data of the agent to the report system.
		// TOOD: Bad smell here... why didn't do the agents this in their scope???
		for(String agName : agentMap.keySet()) {
			Agent agent = getAgentByName(agName);
			Angerona.getInstance().report("Agent: '" + agent.getName()+"' created.", agent);
			
			Angerona.getInstance().report("Desires Set of '" + agent.getName() + "' created.", agent, agent.getDesires());
			
			Beliefs b = agent.getBeliefs();
			Angerona.getInstance().report("World Beliefbase of '" + agent.getName()+"' created.", agent, b.getWorldKnowledge() );
			
			Map<String, BaseBeliefbase> views = b.getViewKnowledge();
			for(String name : views.keySet()) {
				BaseBeliefbase actView = views.get(name);
				Angerona.getInstance().report("View->'" + name +"' Beliefbase of '" + agent.getName() + "' created.", agent, actView);
			}
			
			for(AgentComponent ac : agent.getComponents()) {
				Angerona.getInstance().report("Custom component '" + ac.getClass().getSimpleName() + "' of '" + agent.getName() + "' created.", agent, ac);
			}
		}
		
		DefaultPerceptionFactory df = new DefaultPerceptionFactory();
		for(PerceptionDO p : config.getPerceptions()) {
			Perception percept = df.generateFromDataObject(p, null);
			this.sendAction(percept.getReceiverId(), percept);
		}
		return ready = true;
	}
	
	/**
	 * deletes all agents from the environment and removes the information about the last simulation.
	 */
	public void cleanupSimulation() {
		agents.clear();
		agentMap.clear();
		Angerona.getInstance().onSimulationDestroyed(this);
	}
	
	@Override
	public void sendAction(String agentName, Object action) {
		behavior.sendAction(this, (Action)action);
	}

	PerceptionFactory getPerceptionFactory() {
		return perceptionFactory;
	}

	public int getSimulationTick() {
		return tick;
	}
}
