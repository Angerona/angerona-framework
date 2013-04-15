package angerona.fw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.serialize.AgentConfigReal;
import angerona.fw.serialize.BeliefbaseConfigReal;
import angerona.fw.serialize.SerializeHelper;
import angerona.fw.serialize.SimulationConfiguration;
import angerona.fw.util.ModelAdapter;
import angerona.fw.util.ObservableMap;

/**
 * Represents a project in the Angerona workspace.
 * @author Tim Janus
 */
public class AngeronaProject extends ModelAdapter {
	/** the property name of the agent configuration map */
	public static final String AGENT_MAP_NAME = "agentConfigMap";
	
	/** the property name of the belief base configuration map */
	public static final String BELIEFBASE_MAP_NAME = "beliefBaseConfigMap";
	
	/** the proeprty name of the simulation configuration map */
	public static final String SIMULATION_MAP_NAME = "simulationMap";
	
	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(AngeronaProject.class);
	
	/** map containing all loaded Agent Configurations ordered by name */
	private ObservableMap<String, AgentConfigReal> agentConfigurations 
		= new ObservableMap<String, AgentConfigReal>(AGENT_MAP_NAME);
	
	/** map containing all loaded Beliefbase Configurations ordered by name */
	private ObservableMap<String, BeliefbaseConfigReal> beliefbaseConfigurations 
		= new ObservableMap<String, BeliefbaseConfigReal>(BELIEFBASE_MAP_NAME);
	
	/** map containing all loaded Simulation Configurations ordered by name */
	private ObservableMap<String, SimulationConfiguration> simulationConfigurations 
		= new ObservableMap<String, SimulationConfiguration>(SIMULATION_MAP_NAME);
	
	/** Default Ctor: Registers observable maps */
	public AngeronaProject() {
		registerMap(agentConfigurations);
		registerMap(beliefbaseConfigurations);
		registerMap(simulationConfigurations);
	}
	
	/**
	 * Loads the given file as agent configuration
	 * @param file	The file containing the agent configuration
	 */
	public void loadAgentConfiguration(File file) {
		AgentConfigReal ac = SerializeHelper.loadXml(AgentConfigReal.class, file);
		agentConfigurations.put(ac.getName(), ac);
		LOG.info("Agent configuration in '{}' loaded.", file.getAbsolutePath());
	}
	
	/**
	 * Loads the given file as belief base configuration
	 * @param file	the file containing the belief base configuration
	 */
	public void loadBeliefBaseConfiguration(File file) {
		BeliefbaseConfigReal bbc = BeliefbaseConfigReal.loadXml(file);
		beliefbaseConfigurations.put(bbc.getName(), bbc);
		LOG.info("Belief base configuration in '{}' loaded.", file.getAbsolutePath());
	}
	
	/** 
	 * Loads the given file as simulation configuration
	 * @param file	The file containing the simulation configuration
	 */
	public void loadSimulationConfiguration(File file) {
		SimulationConfiguration sc = SimulationConfiguration.loadXml(file);
		sc.setFile(file);
		simulationConfigurations.put(sc.getName(), sc);
		LOG.info("Simulation configuration in '{}' loaded.", file.getAbsolutePath());
	}
	
	/**
	 * Adds all the configuration files in the given directory and it sub-directory to the
	 * project.
	 * @param directory
	 * @throws IOException
	 */
	public void addDirectory(File directory) throws IOException {
		if(!directory.isDirectory())
			throw new IOException(directory.getAbsolutePath() + " is no directory.");
		
		File [] files = directory.listFiles();
		if(files == null)
			return;
		for(File actFile : files) {
			if(actFile.isFile() && actFile.getPath().endsWith("xml")) {
				BufferedReader reader = new BufferedReader(new FileReader(actFile));
				String line = null;
				
				int type = -1;
				final int maxLines = 5;
				int counter = 0;
				
				while((line = reader.readLine()) != null && counter < maxLines) {
					if(line.contains("<agent-configuration>")) {
						type = 1;
					} else if(line.contains("<simulation-configuration>")) {
						type = 2;
					} else if(line.contains("<beliefbase-configuration>")) {
						type = 3;
					}
					
					++counter;
				}
				
				reader.close();
				
				switch(type) {
				case 1:
					loadAgentConfiguration(actFile);
					break;
					
				case 2:
					loadSimulationConfiguration(actFile);
					break;
					
				case 3:
					loadBeliefBaseConfiguration(actFile);
					break;
					
				default:
					LOG.warn("The file '{}' neither is an agent configuration nor a belief " +
							"base configuration, nor a simulation, therefore it cannot be " +
							"loaded by the Angerona Project.", actFile.getAbsolutePath());
					break;
				}
			} else if(actFile.isDirectory()) {
				addDirectory(actFile);
			}
		}
	}
	
	public AgentConfigReal getAgentConfiguration(String name) {
		return agentConfigurations.get(name);
	}
	
	public Set<String> getBeliefbaseConfigurationNames() {
		return beliefbaseConfigurations.keySet();
	}
	
	public BeliefbaseConfigReal getBeliefbaseConfiguration(String name) {
		return beliefbaseConfigurations.get(name);
	}
	
	public Set<String> getSimulationConfigurationNames() {
		return simulationConfigurations.keySet();
	}
	
	public SimulationConfiguration getSimulationConfiguration(String name) {
		return simulationConfigurations.get(name);
	}
	
	public Set<String> getAgentConfigurationNames() {
		return agentConfigurations.keySet();
	}
}
