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
	
	private static Logger LOG = LoggerFactory.getLogger(Angerona.class);
	
	private static Angerona instance = null;
	
	private List<ReportListener> reportListeners = new LinkedList<ReportListener>();
	
	private List<SimulationListener> simulationListeners = new LinkedList<SimulationListener>();
	
	private List<ErrorListener> errorListeners = new LinkedList<ErrorListener>();
	
	// TODO: Differentiate between environment and simulation.
	private Map<AngeronaEnvironment, Report> reports = new HashMap<AngeronaEnvironment, Report>(); 
	
	private Report actualReport;
	
	private AngeronaEnvironment actualSimulation;
	
	public static Angerona getInstance() {
		if(instance == null)
			instance = new Angerona();
		return instance;
	}
	
	private static GlobalConfiguration config = null;
	
	public static GlobalConfiguration getConfig() {
		if(config == null) {
			File defConfigFile = new File("config/configuration.xml");
			config = GlobalConfiguration.loadXml(defConfigFile);
			
			if(config == null)
				config = new GlobalConfiguration();
		}
		return config;
	}
	
	public void report(String msg, ReportPoster sender) {
		report(msg, sender, null);
	}
	
	public void report(String msg, ReportPoster sender, Entity attachment) {
		String logOut = msg;
		
		if (sender == null){
			throw new IllegalArgumentException("sender must not be null");
		}

		logOut += " by " + sender.getPosterName() + " in " + sender.getSimulationTick()+":"+sender.getSimulation();
		
		// Every report will also be logged by our logging facility.
		LOG.info("REPORT: " + logOut);
		
		ReportEntry entry = new ReportEntry(msg, sender, attachment);
		Angerona.getInstance().getReport(entry.getPoster().getSimulation()).saveEntry(entry);
		for(ReportListener listener : reportListeners) {
			listener.reportReceived(entry);
		}
	}
	
	/** @return the report of the last started simulation. */
	public Report getActualReport() {
		return actualReport;
	}
	
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
	
	public void onNewSimulation(AngeronaEnvironment ev) {
		actualReport = new Report(ev);
		actualSimulation = ev;
		reports.put(ev, actualReport);
		for(SimulationListener l : simulationListeners) {
			l.simulationStarted(ev);
		}
	}
	
	public void onSimulationDestroyed(AngeronaEnvironment ev) {
		actualReport = null;
		reports.clear();
		for(SimulationListener l : simulationListeners) {
			l.simulationDestroyed(ev);
		}
	}
	
	public void onTickDone(AngeronaEnvironment ev, boolean finished) {
		for(SimulationListener l : simulationListeners) {
			l.tickDone(ev, finished);
		}
	}
	
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
