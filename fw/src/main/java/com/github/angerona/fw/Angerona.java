package com.github.angerona.fw;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.tweety.logics.commons.LogicalSymbols;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.github.angerona.fw.def.FrameworkPlugin;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.internal.OperatorMap;
import com.github.angerona.fw.internal.PluginInstantiator;
import com.github.angerona.fw.listener.FrameworkListener;
import com.github.angerona.fw.listener.SimulationListener;
import com.github.angerona.fw.operators.OperatorStack;
import com.github.angerona.fw.report.Report;
import com.github.angerona.fw.report.ReportEntry;
import com.github.angerona.fw.report.ReportListener;
import com.github.angerona.fw.report.ReportPoster;
import com.github.angerona.fw.serialize.GlobalConfiguration;

/**
 * Main class of Angerona manages all resources.
 * 
 * Give the user the ability to add new folders
 * as resource folders, all the files in those folders
 * will be loaded.
 * 
 * @author Tim Janus
 * @todo Also handle plug-ins as resource.
 */
public class Angerona {
	
	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(Angerona.class);
	
	/** the only instance of Angerona */
	private static Angerona instance = null;
	
	/** the list of registered report listeners */
	private List<ReportListener> reportListeners = new LinkedList<ReportListener>();
	
	/** the list of registered simulation listeners */
	private List<SimulationListener> simulationListeners = new LinkedList<SimulationListener>();
	
	/** the list of registered error listeners */
	private List<FrameworkListener> frameworkListeners = new LinkedList<FrameworkListener>();
	
	/** flag indicating if the bootstrap process is already done. */
	private boolean bootstrapDone = false;
	
	
	/** 
	 * A Map containing the Report instances for specific simulations 
	 * @todo Differentiate between environment and simulation.
	 */
	private Map<AngeronaEnvironment, Report> reports = new HashMap<AngeronaEnvironment, Report>(); 
	
	/** reference to the report of the actual running simulation */
	private Report actualReport;
	
	/** reference to the actual loaded simulation */
	private AngeronaEnvironment actualSimulation;
	
	/** reference to the configuration of Angerona */
	private GlobalConfiguration config = null;

	private String configFilePath = "config/configuration.xml";
	
	private AngeronaProject currentProject = new AngeronaProject();
	
	/**
	 * 	Implements the singleton pattern.
	 * 	@return the application wide unique instance of the Angerona class.
	 */
	public static Angerona getInstance() {
		if(instance == null)
			instance = new Angerona();
		return instance;
	}
	
	public AngeronaProject getProject() {
		return currentProject;
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
			} else {
				defConfigFile = new File("app/target/" + getConfigFilePath());
				
				if(defConfigFile.exists()) {
					config = GlobalConfiguration.loadXml(defConfigFile);
				}
			}
			
			if(config == null) {
				config = new GlobalConfiguration();
				onError("Configuration File not Found", 
						"Cannot find the file: '" + filename +"'."
						+ "Make sure that your working directory contains the config directory."
						+ "\nIf you do not have a file 'configuration.xml' "
						+ "in your config directory then close the application "
						+ "and create your own by moving \n"
						+ "'.../software/app/src/main/config/configuration_install.xml'" +
						"\nto \n'"
						+ (new File(configFilePath)).getAbsolutePath() + 
						"'\nand replacing the placeholders.");
			}
		}
		return config;
	}
	
	/**
	 * Writes a report entry with the given message of the given poster
	 * to the Angerona Report-System. No attachment is given to the report entry.
	 * @param msg		String representing the message.
	 * @param scope		The scope of the report as call-stack of operators.
	 * @param poster	Reference to the poster of the report (an operator or an agent)
	 */
	public void report(String msg, OperatorStack scope, ReportPoster poster) {
		report(msg, null, scope, poster);
	}
	
	/**
	 * Writes a report entry with the given message and attachment of the given poster
	 * to the Angerona Report-System. A copy of the attachment is saved in the report
	 * system.
	 * @param msg			String representing the message.
	 * @param attachment	A reference to the attachment which has to be an entity like
	 * 						the Secrecy-Knowledge or a Belief base.
	 * @param scope		The scope of the report as call-stack of operators.
	 * @param poster		Reference to the poster of the report(an operator or an agent)
	 */
	public void report(String msg, Entity attachment, OperatorStack scope, ReportPoster poster) {
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
	
	public void removeSimulationListener(SimulationListener listener) {
		simulationListeners.remove(listener);
	}
	
	public void removeAllSimulationListeners() {
		simulationListeners.clear();
	}
	
	
	public void addFrameworkListener(FrameworkListener listener) {
		frameworkListeners.add(listener);
	}
	
	public boolean removeFrameworkListener(FrameworkListener listener) {
		return frameworkListeners.remove(listener);
	}
	
	public void removeAllFrameworkListeners() {
		frameworkListeners.clear();
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
	
	protected void onAgentAdded(AngeronaEnvironment env, Agent added) {
		for(SimulationListener l : simulationListeners) {
			l.agentAdded(env, added);
		}
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
	 * Informs the simulations listeners when a tick of the simulation is starting.
	 * @param ev		Reference to the simulation
	 */
	public void onTickStarting(AngeronaEnvironment ev) {
		for(SimulationListener l : simulationListeners) {
			l.tickStarting(ev);
		}
	}
	
	/**
	 * Informs the simulations listeners when a tick of the simulation is done.
	 * @param ev		Reference to the simulation
	 */
	public void onTickDone(AngeronaEnvironment ev) {
		for(SimulationListener l : simulationListeners) {
			l.tickDone(ev);
		}
	}
	
	/**
	 * Helper method: Called to inform all Angerona error listeners about an error.
	 * @param title		The title for the error
	 * @param message	The error message
	 */
	public void onError(String title, String message) {
		for(FrameworkListener l : frameworkListeners) {
			l.onError(title, message);
		}
	}
	
	private Angerona() {}
	
	/**
	 * Loads the resources in the folders registered so far. First of all the
	 * AgentConfigurations are loaded then the belief base Configurations and
	 * after that the Simulation templates.
	 * 
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	public void bootstrap() throws IOException, ParserConfigurationException, SAXException {
		
		if(!bootstrapDone) {
			LogicalSymbols.setClassicalNegationSymbol("-");
			LogicalSymbols.setContradictionSymbol("!");
			
			// create plugin manager classes
			PluginInstantiator pi = PluginInstantiator.getInstance();
			pi.addListener(OperatorMap.get());
			pi.addPlugins(getConfig().getPluginPaths());
			pi.registerPlugin(new FrameworkPlugin());
			bootstrapDone = true;
			
			for(FrameworkListener fl : frameworkListeners) {
				fl.onBootstrapDone();
			}
		}
	}
	
	/** @return true if a bootstrap method was called otherwise false. */
	public boolean isBootstrapDone() {
		return bootstrapDone;
	}
	

	public void onActionPerformed(Agent agent, Action act) {
		for(SimulationListener listener : simulationListeners) {
			listener.actionPerformed(agent, act);
		}
	}
}
