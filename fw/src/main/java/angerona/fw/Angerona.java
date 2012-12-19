package angerona.fw;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import angerona.fw.internal.Entity;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.listener.ErrorListener;
import angerona.fw.listener.SimulationListener;
import angerona.fw.operators.OperatorVisitor;
import angerona.fw.report.Report;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;
import angerona.fw.report.ReportPoster;
import angerona.fw.serialize.AgentConfigReal;
import angerona.fw.serialize.BeliefbaseConfigReal;
import angerona.fw.serialize.GlobalConfiguration;
import angerona.fw.serialize.SerializeHelper;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * Main class of Angerona manages all resources.
 * Give the user the ability to add new folders
 * as resource folders, all the files in those folders
 * will be loaded.
 * @author Tim Janus
 * TODO: Also handle plugins as resource.
 *
 */
public class Angerona {
	
	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(Angerona.class);
	
	/** the only instnce of angerona */
	private static Angerona instance = null;
	
	/** the list of registered report listeners */
	private List<ReportListener> reportListeners = new LinkedList<ReportListener>();
	
	/** the list of registered simulation listeners */
	private List<SimulationListener> simulationListeners = new LinkedList<SimulationListener>();
	
	/** the list of registered error listeners */
	private List<ErrorListener> errorListeners = new LinkedList<ErrorListener>();
	
	// TODO: Differentiate between environment and simulation.
	/** A Map containing the Report instances for specific simulations */
	private Map<AngeronaEnvironment, Report> reports = new HashMap<AngeronaEnvironment, Report>(); 
	
	/** reference to the report of the actual running simulation */
	private Report actualReport;
	
	/** reference to the actual loaded simulation */
	private AngeronaEnvironment actualSimulation;
	
	/** reference to the configuration of Angerona */
	private GlobalConfiguration config = null;

	private String configFilePath = "config/configuration.xml";
	
	/**
	 * 	Implements the singleton pattern.
	 * 	@return the application wide unique instance of the Angerona class.
	 */
	public static Angerona getInstance() {
		if(instance == null)
			instance = new Angerona();
		return instance;
	}
	
	/** @return the path to the config file */
	public String getConfigFilePath() {
		return configFilePath;
	}
	
	/**
	 * Changes the path to the config file
	 * @param configFilePath	new path to the config file as string
	 */
	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
	
	/**
	 * 	Loads the global configuration if it is not loaded yet and gives the
	 * 	global configurations contents to the caller.
	 * 	@return	A reference to the global configuration.
	 */
	public GlobalConfiguration getConfig() {
		if(config == null) {
			String filename = getConfigFilePath();
			File defConfigFile = new File(filename);
			if(defConfigFile.exists()) {
				config = GlobalConfiguration.loadXml(defConfigFile);
				
				if(config == null) {
					config = new GlobalConfiguration();
					onError("Configuration File not Found", 
							"Cannot find the file: '" + filename +
							"' close the Application and create your own\n" +
							"by renaming '.../software/test/src/main/config/configuration_template.xml'" +
							"to 'configuration.xml'\nand replace the placeholders.");
				}
			} else {
				onError("Configuration File not Found", 
						"Cannot find the file: '" + filename +
						"' close the Application and create your own\n" +
						"by renaming '.../software/test/src/main/config/configuration_template.xml'" +
						"to 'configuration.xml'\nand replace the placeholders.");
			}
		}
		return config;
	}
	
	/**
	 * Writes a report entry with the given message of the given poster
	 * to the Angerona Report-System. No attachment is given to the report entry.
	 * @param msg		String representing the message.
	 * @param poster	Reference to the poster of the report (an operator or an agent)
	 */
	public void report(String msg, OperatorVisitor scope, ReportPoster poster) {
		report(msg, null, scope, poster);
	}
	
	/**
	 * Writes a report entry with the given message and attachment of the given poster
	 * to the Angerona Report-System. A copy of the attachment is saved in the report
	 * system.
	 * @param msg			String representing the message.
	 * @param attachment	A reference to the attachment which has to be an entity like
	 * 						the Secrecy-Knowledge or a Belief base. 
	 * @param poster		Reference to the poster of the report(an operator or an agent)
	 */
	public void report(String msg, Entity attachment, OperatorVisitor scope, ReportPoster poster) {
		String logOut = msg;
		
		if (poster == null){
			throw new IllegalArgumentException("poster must not be null");
		}
		if(scope == null) {
			throw new IllegalArgumentException("scope must not be null");
		}

		logOut += " by " + poster.getPosterName();
		
		// Every report will also be logged by our logging facility.
		LOG.info("REPORT: " + logOut);
		
		ReportEntry entry = new ReportEntry(msg, attachment, scope, poster, this.actualSimulation);
		Angerona.getInstance().getReport(entry.getSimulation()).saveEntry(entry);
		for(ReportListener listener : reportListeners) {
			listener.reportReceived(entry);
		}
	}
	
	/** @return the report of the last started simulation. */
	public Report getActualReport() {
		return actualReport;
	}
	
	/** @return the reference to the last loaded simulation */
	public AngeronaEnvironment getActualSimulation() {
		return actualSimulation;
	}
	
	/**
	 * @param simulation a reference to a simulation.
	 * @return the report belonging to the given simulation.
	 */
	public Report getReport(AngeronaEnvironment simulation) {
		return reports.get(simulation);
	}
	
	/**
	 * registers a listener which will be informed if a report is posted.
	 * @param listener	reference to the listener which should be registered.
	 */
	public void addReportListener(ReportListener listener) {
		reportListeners.add(listener);
	}
	
	/**
	 * removes a registered report-listener from the list of report-listeners.
	 * @param listener
	 * @return flag indicating if the remove operation was successful.
	 */
	public boolean removeReportListener(ReportListener listener) {
		return reportListeners.remove(listener);
	}
	
	/** removes all registered report-listeners. */
	public void removeAllReportListeners() {
		reportListeners.clear();
	}
	
	public void addSimulationListener(SimulationListener listener) {
		simulationListeners.add(listener);
	}
	
	public void removeReportListener(SimulationListener listener) {
		simulationListeners.remove(listener);
	}
	
	public void removeAllSimulationListeners() {
		simulationListeners.clear();
	}
	
	public void addErrorListener(ErrorListener listener) {
		errorListeners.add(listener);
	}
	
	public boolean removeErrorListener(ErrorListener listener) {
		return errorListeners.remove(listener);
	}
	
	public void removeAllErrorListeners() {
		errorListeners.clear();
	}
	
	/** 
	 * 	Called when a new simulation is initialized. 
	 * 	It updates the reference to the actual report and the actual simulation.
	 * 	@param ev	The reference to the new simulation simulation.
	 */
	protected void onCreateSimulation(AngeronaEnvironment ev) {
		actualReport = new Report(ev);
		actualSimulation = ev;
		reports.put(ev, actualReport);
	}
	
	/**
	 * Informs the simulation listeners about the finished initialization
	 * of the given simulation
	 * @param ev		reference to the initialized simulation.
	 */
	protected void onNewSimulation(AngeronaEnvironment ev) {
		for(SimulationListener l : simulationListeners) {
			l.simulationStarted(ev);
		}
	}
	
	/**
	 * called when a simulation is finished an the clean up method is called, informs
	 * the simulation listeners about the cleanup of the simulation.
	 * @param ev	A reference to the simulation.
	 */
	protected void onSimulationDestroyed(AngeronaEnvironment ev) {
		actualReport = null;
		reports.clear();
		for(SimulationListener l : simulationListeners) {
			l.simulationDestroyed(ev);
		}
	}
	
	/**
	 * Informs the simulations listeners when a tick of the simulation is done.
	 * @param ev		Reference to the simulation
	 * @param finished	Flag indicating if the simulation is finished now.
	 */
	protected void onTickDone(AngeronaEnvironment ev, boolean finished) {
		for(SimulationListener l : simulationListeners) {
			l.tickDone(ev, finished);
		}
	}
	
	/**
	 * Helper method: Called to inform all Angerona error listeners about an error.
	 * @param title		The title for the error
	 * @param message	The error message
	 */
	public void onError(String title, String message) {
		for(ErrorListener l : errorListeners) {
			l.onError(title, message);
		}
	}
	
	/**
	 * Handle different resource file loadings by fileformat.
	 * @author Tim Janus
	 */
	private interface FileLoader {
		void load(File file, Angerona container) throws ParserConfigurationException, SAXException, IOException;
	}
	
	/**
	 * Implementation for the Agent-Configuration file format.
	 * @author Tim Janus
	 */
	private class AgentConfigLoader implements FileLoader {
		@Override
		public void load(File file, Angerona container) {
			AgentConfigReal ac = SerializeHelper.loadXml(AgentConfigReal.class, file);
			container.agentConfigurations.put(ac.getName(), ac);
		}
	}
	
	/**
	 * Implementation for the Belief-Base-configuration file format.
	 * @author Tim Janus
	 */
	private class BeliefbaseConfigLoader implements FileLoader {
		@Override
		public void load(File file, Angerona container) {
			BeliefbaseConfigReal bbc = BeliefbaseConfigReal.loadXml(file);
			container.beliefbaseConfigurations.put(bbc.getName(), bbc);
		}
	}
	
	/**
	 * Implementation for the Simulation-Configuration file format.
	 * @author Tim Janus
	 *
	 */
	private class SimulationConfigLoader implements FileLoader {
		@Override
		public void load(File file, Angerona container) throws ParserConfigurationException, SAXException, IOException {
			SimulationConfiguration sc = SimulationConfiguration.loadXml(file);
			sc.setFilePath(file.getAbsolutePath());
			container.simulationConfigurations.put(sc.getName(), sc);
		}
	}
	
	/** flag indicating if the bootstrap process is already done. */
	private boolean bootstrapDone = false;
	
	/** set containing all folders where agent configuration files are stored. */
	private Set<String> agentConfigFolders = new HashSet<String>();
	
	/** set containing all folders where belief base configuration files are stored. */
	private Set<String> bbConfigFolders = new HashSet<String>();
	
	/** set containing all folders where simulation configuration files are stored. */
	private Set<String> simulationFolders = new HashSet<String>();
	
	/** map containing all loaded Agent Configurations ordered by name */
	private Map<String, AgentConfigReal> agentConfigurations = new HashMap<String, AgentConfigReal>();
	
	/** map containing all loaded Beliefbase Configurations ordered by name */
	private Map<String, BeliefbaseConfigReal> beliefbaseConfigurations = new HashMap<String, BeliefbaseConfigReal>();
	
	/** map containing all loaded Simulation Configurations ordered by name */
	private Map<String, SimulationConfiguration> simulationConfigurations = new HashMap<String, SimulationConfiguration>();
	
	private Angerona() {}
	
	/**
	 * Adds the given folder to the set of AgentConfiguration folders, if bootstrap is already done
	 * the loading start immediatley otherwise the files in the folder will be load after a call of 
	 * bootstrap
	 * @param folder	name of the folder containing AgentConfiguration files.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void addAgentConfigFolder(String folder) throws ParserConfigurationException, SAXException, IOException {
		if(agentConfigFolders.add(folder) && bootstrapDone) {
			forAllFilesIn(folder, new AgentConfigLoader());
		}
	}
	
	/**
	 * Adds the given folder to the set of BeliefbaseConfiguration folders, if bootstrap is already done
	 * the loading start immediately otherwise the files in the folder will be load after a call of 
	 * bootstrap
	 * @param folder	name of the folder containing BeliefbaseConfiguration files.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void addBeliefbaseConfigFolder(String folder) throws IOException, ParserConfigurationException, SAXException {
		if(bbConfigFolders.add(folder) && bootstrapDone) {
			forAllFilesIn(folder, new BeliefbaseConfigLoader());
		}
	}
	
	/**
	 * Adds the given folder to the set of Simulation folders, if bootstrap is already done
	 * the loading start immediately otherwise the files in the folder will be load after a call of 
	 * bootstrap
	 * @param folder	name of the folder containing Simulation files.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void addSimulationFolders(String folder) throws IOException, ParserConfigurationException, SAXException {
		if(simulationFolders.add(folder) && bootstrapDone) {
			forAllFilesIn(folder, new SimulationConfigLoader());
		}
	}
	
	/**
	 * Loads the resources in the folders registered so far. First of all the
	 * AgentConfigurations are loaded then the Beliefbase Configurations and
	 * after that the Simulation templates.
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void bootstrap() throws IOException, ParserConfigurationException, SAXException {
		if(!bootstrapDone) {
			AgentConfigLoader acl = new AgentConfigLoader();
			for(String folder : agentConfigFolders) {
				forAllFilesIn(folder, acl);
			}
			
			BeliefbaseConfigLoader bbcl = new BeliefbaseConfigLoader();
			for(String folder : bbConfigFolders) {
				forAllFilesIn(folder, bbcl);
			}
			
			SimulationConfigLoader scl = new SimulationConfigLoader();
			for(String folder : simulationFolders) {
				forAllFilesIn(folder, scl);
			}
		}
		PluginInstantiator.getInstance().addPlugins(getConfig().getPluginPaths());
		bootstrapDone = true;
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
	
	/** @return true if a bootstrap method was called otherwise false. */
	public boolean isBootstrapDone() {
		return bootstrapDone;
	}
	
	/**
	 * Helper method: Recursively tries to load all files in the directory and in
	 * its sub-directories using the given FileLoader implementation.
	 * @param folder	root folder to scan for files to load.
	 * @param loader	Implementation of the used loader.
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private void forAllFilesIn(String folder, FileLoader loader) throws IOException, ParserConfigurationException, SAXException {
		File dir = new File(folder);
		if(!dir.isDirectory())
			throw new IOException(folder + " is no directory.");
		
		File [] files = dir.listFiles();
		if(files == null)
			return;
		for(File actFile : files) {
			try {
				if(actFile.isFile() && actFile.getPath().endsWith("xml"))
					loader.load(actFile, this);
				else if(actFile.isDirectory())
					forAllFilesIn(actFile.getAbsolutePath(), loader);
			} catch(Exception ex) {
				LOG.warn("Cannot load file: '"+actFile.getName()+"' " + ex.getMessage());
			} 
		}
	}

	public void onActionPerformed(Agent agent, Action act) {
		for(SimulationListener listener : simulationListeners) {
			listener.actionPerformed(agent, act);
		}
	}
}
