package angerona.fw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.beenuts.ap.AgentProcess;
import net.sf.beenuts.apr.APR;
import net.sf.tweety.logics.firstorderlogic.syntax.FolSignature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import angerona.fw.error.AgentIdException;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.Beliefs;
import angerona.fw.parser.BeliefbaseSetParser;
import angerona.fw.parser.ParseException;
import angerona.fw.report.Entity;
import angerona.fw.report.ReportPoster;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * A simulation environment for Angerona. This is actually only used for some functional tests.
 * TODO: Extend the functionality.
 * @author Tim Janus
 */
public class AngeronaEnvironment extends APR implements ReportPoster {

	private static Logger LOG = LoggerFactory.getLogger(AngeronaEnvironment.class);
	
	/** the actual simulation tick */
	private int tick = 0;
	
	/** the name of the simulation, for example: 'strike_comittee_meeting' */
	private String name = "";
	
	/** implementation of the factory used for perceptions */
	private PerceptionFactory perceptionFactory = new DefaultPerceptionFactory();
	
	/** flag indicating if the environment is currently in its update process */
	private boolean doingTick = false;
	
	/** flag indicating if the environment is correctly initialized */
	private boolean ready = false;
	
	/** a map of entity ids to the entity references */
	private Map<Long, Entity> entities = new HashMap<Long, Entity>();
	
	public Map<Long, Entity> getEntityMap() {
		return Collections.unmodifiableMap(entities);
	}
	
	@Override
	public String getPosterName() {
		return name;
	}
	
	public Set<String> getAgentNames() {
		return agentMap.keySet();
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
	 * runs a simulation until no more perceptions are in the queue. 
	 * During a cycle new perceptions can be generated.
	 */
	public void runTillNoMorePerceptionsLeft() {
		boolean percept = false;
		do {
			percept = runOneTick();
		} while(percept);
	}
	
	/**
	 * runs one simulation tick. Gives every agent the ability to call its cycle method.
	 * @return true if at least one agents cylce function was called, false otherwise.
	 */
	public boolean runOneTick() {
		doingTick = true;
		++tick;
		boolean percept = false;
		for(AgentProcess ap : agents) {
			AngeronaAgentProcess aap = (AngeronaAgentProcess)ap;
			if(aap.hasPerceptions()) {
				aap.execCycle();
				percept = true;
			}
		}
		doingTick = false;
		return percept;
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
	 * 							immediatley after loading the file.
	 * @return the loaded simulation configuration if no error occurred, null otherwise.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public SimulationConfiguration loadSimulation(String filename, boolean startImmediately) throws FileNotFoundException, IOException {
		SimulationConfiguration config = null;
		
		try {
			config = SimulationConfiguration.loadXml(filename).get(0);
			
		} catch (ParserConfigurationException e) {
			config = null;
			LOG.error("Cannot start simulation, something went wrong during xml parsing: " + e.getMessage());
			e.printStackTrace();
		} catch (SAXException e) {
			config = null;
			LOG.error("Cannot load simulation, something went wrong during xml parsing: " + e.getMessage());
			e.printStackTrace();
		} 
		
		config.getName();
		
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
		
		PluginInstantiator pi = PluginInstantiator.getInstance();
		tick = 0;
		String errorOutput = "";
		try {
			for(SimulationConfiguration.AgentInstance ai : config.getAgents()) {
				Agent highLevelAg = new Agent(ai.getConfig(), ai.getName());
				entities.put(highLevelAg.getGUID(), highLevelAg);
				
				BaseBeliefbase world = pi.createBeliefbase(ai.getBeliefbaseConfig());
				entities.put(world.getGUID(), world);
				world.setEnvironment(this);
				String fn = simulationDirectory + "/" + ai.getFileSuffix() + "." + world.getFileEnding();
				
				FileInputStream fis = new FileInputStream(new File(fn));
				BeliefbaseSetParser bbsp = new BeliefbaseSetParser(fis);
				bbsp.Input();
				fis.close();
				
				StringReader sr = new StringReader(bbsp.worldContent);
				world.parse(new BufferedReader(sr));
				
				ConfidentialKnowledge conf = new ConfidentialKnowledge();
				entities.put(conf.getGUID(), conf);
				conf.setEnvironment(this);
				FolSignature fsig = new FolSignature();
				fsig.fromSignature(world.getSignature());
				conf.setSignature(fsig);
				conf.parse(getBeliefbaseFilename(simulationDirectory, ai.getFileSuffix(), conf));
				
				Map<String, BaseBeliefbase> views = new HashMap<String, BaseBeliefbase>();
				
				for(String key : bbsp.viewContent.keySet()) {
					BaseBeliefbase actView = pi.createBeliefbase(ai.getBeliefbaseConfig());
					entities.put(actView.getGUID(), actView);
					actView.setEnvironment(this);
					sr = new StringReader(bbsp.viewContent.get(key));
					actView.parse(new BufferedReader(sr));
					views.put(key, actView);
					
				}
				
								
				highLevelAg.setBeliefs(world, views, (ConfidentialKnowledge)conf);		
				highLevelAg.addSkillsFromConfig(ai.getSkillConfig());
				addAgent(highLevelAg.getAgentProcess());
				highLevelAg.getDesires().addAll(ai.getDesires());
				highLevelAg.initComponents(ai.getAdditionalData());
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
		for(String agName : agentMap.keySet()) {
			Agent agent = getAgentByName(agName);
			Angerona.getInstance().report("Agent: '" + agent.getName()+"' created.", this);
			
			Angerona.getInstance().report("Desires Set of '" + agent.getName() + "' created.", this, agent.getDesires());
			
			Beliefs b = agent.getBeliefs();
			Angerona.getInstance().report("World Beliefbase of '" + agent.getName()+"' created.", this, b.getWorldKnowledge() );
			
			Map<String, BaseBeliefbase> views = b.getViewKnowledge();
			for(String name : views.keySet()) {
				BaseBeliefbase actView = views.get(name);
				Angerona.getInstance().report("View->'" + name +"' Beliefbase of '" + agent.getName() + "' created.", this, actView);
			}
			
			Angerona.getInstance().report("Confidental Knowledge of '" + agent.getName() + "' created.", this, b.getConfidentialKnowledge());
			Angerona.getInstance().report("Master-Plan of '" + agent.getName() + "' created.", this, agent.getPlan());
			
			for(AgentComponent ac : agent.getComponents()) {
				Angerona.getInstance().report("Custom component '" + ac.getClass().getSimpleName() + "' of '" + agent.getName() + "' created.", this, ac);
			}
		}
		
		DefaultPerceptionFactory df = new DefaultPerceptionFactory();
		List<Perception> initPercepts = df.generateFromParentElement(config.getFlowElement(), null);
		for(Perception p : initPercepts) {
			this.sendAction(p.getReceiverId(), p);
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
	
	/**
	 * Helper method: Generates the filepath to a belief base.
	 * @param parentDir	The parent dir of the simulation
	 * @param suffix	suffix of the filename.
	 * @param bb		An instance of the belief base for determining the file ending.
	 * @return			String representing the path to the correct belief base file.
	 */
	private String getBeliefbaseFilename(String parentDir, String suffix, BaseBeliefbase bb) {
		return parentDir + "/" + suffix + "." + bb.getFileEnding();
	}
	
	@Override
	public void sendAction(String agentName, Object action) {
		// The action send by one agent is the perception of the other one.
		if(Action.ALL.equals(agentName)) {
			for(String name : agentMap.keySet()) {
				agentMap.get(name).perceive(action);
			}
		} else {
			agentMap.get(agentName).perceive(action);
		}
	}

	PerceptionFactory getPerceptionFactory() {
		return perceptionFactory;
	}

	@Override
	public int getSimulationTick() {
		return tick;
	}

	@Override
	public AngeronaEnvironment getSimulation() {
		return this;
	}
}
