package angerona.fw.internal;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;
import net.xeoh.plugins.base.util.uri.ClassURI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.AgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.listener.PluginListener;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.serialize.BeliefbaseConfig;

/**
 * 	This class collects all the implementation of the plugins and gives 
 * 	the user the ability to dynamically instantiate them.
 * 
 * 	By using listeners extensions of the angerona-core framework can define
 * 	their own plugins to load in the extension. This feature is used by the
 * 	gui-extension of Angerona.
 * 
 * 	@author Tim Janus
 */
public class PluginInstantiator {
	/** singleton instance */
	private static PluginInstantiator instance;
	
	/** @return the only instance of the PluginInstantiator */
	public static PluginInstantiator getInstance() {
		if(instance == null) {
			instance = new PluginInstantiator();
		}
		return instance;
	}
	
	/** reference to logger implementation */
	private Logger LOG = LoggerFactory.getLogger(PluginInstantiator.class);
	
	/** reference to the plugin manager used by jspf. */
	private PluginManager pm;
	
	/** the listeners get informed if the plugin instantiator loads implementations */ 
	private List<PluginListener> listeners = new LinkedList<PluginListener>();
	
	/** the plugin manager utiltiy */
	private PluginManagerUtil util;
	
	/** a map of Classes defining the basis type to a set of classes defining implementations */
	private Map<Class<?>, Set<Class<?>>> implMap = new HashMap<Class<?>, Set<Class<?>>>();
	
	/** @return the utility class of the plugin API to load plugins ect. */
	public PluginManagerUtil getPluginUtil() {
		return util;
	}
	
	/** Ctor: Initializes the simple plugin framework, private to force singleton paradigm. */
	private PluginInstantiator() {
		implMap.put(BaseGenerateOptionsOperator.class, new HashSet<Class<?>>());
		implMap.put(BaseIntentionUpdateOperator.class, new HashSet<Class<?>>());
		implMap.put(BaseUpdateBeliefsOperator.class, new HashSet<Class<?>>());
		implMap.put(BaseViolatesOperator.class, new HashSet<Class<?>>());
		implMap.put(BaseSubgoalGenerationOperator.class, new HashSet<Class<?>>());
		implMap.put(BaseBeliefbase.class, new HashSet<Class<?>>());
		implMap.put(BaseReasoner.class, new HashSet<Class<?>>());
		implMap.put(BaseChangeBeliefs.class, new HashSet<Class<?>>());
		implMap.put(BaseTranslator.class, new HashSet<Class<?>>());
		implMap.put(AgentComponent.class, new HashSet<Class<?>>());
		implMap.put(Action.class, new HashSet<Class<?>>());
		
		pm = PluginManagerFactory.createPluginManager();
		util = new PluginManagerUtil(pm);
	}
	
	/**
	 * adds the plugin file on the specified path.
	 * @param path	Path to the jar file containing the plugin implementation
	 */
	public void addPlugin(String path) {
		pm.addPluginsFrom(new File(path).toURI());
		loadAllPlugins();
	}
	
	/**
	 * adds the plugins on the given file paths.
	 * @param paths	A list of pathes to jar files containing plugin implementations.
	 */
	public void addPlugins(Collection<String> paths) {
		for(String path : paths) {
			pm.addPluginsFrom(new File(path).toURI());
		}
		loadAllPlugins();
	}

	/**
	 * 	Registers the given PluginListener. After registration the Listener will be informed
	 * 	if the implementation gets loaded.
	 * 	@param pl	Reference to the listener.
	 */
	public void addListener(PluginListener pl) {
		this.listeners.add(pl);
	}
	
	/**
	 * Unregisters a registered plugin listener. The listener will not be informed of any further
	 * changes.
	 * @param pl	Reference to the listener.
	 * @return		
	 */
	public boolean removeListener(PluginListener pl) {
		return this.listeners.remove(pl);
	}
	
	/** Deregisteres all listeners */
	public void removeAllListener() {
		this.listeners.clear();
	}
	
	/**
	 * 	Loads the plugins for the core Angerona Framework and informs the listeners about the
	 * 	loading. The listeners can load different plugins on their own like the gui extension:
	 * 	It loads UI-Components to enhance the GUI with views for the data components defined
	 * 	in different plugins.
	 */
	private void loadAllPlugins() {
		List<Plugin> loadedPlugins = new LinkedList<Plugin>();
		Collection<OperatorPlugin> opPlugins = new LinkedList<OperatorPlugin>(util.getPlugins(OperatorPlugin.class));
		LOG.info("Load Operator-Plugins:");
		for(OperatorPlugin ap : opPlugins) {
			if(loadedPlugins.contains(ap))
				continue;
			loadedPlugins.add(ap);
			implMap.get(BaseGenerateOptionsOperator.class).addAll(ap.getSupportedGenerateOptionsOperators());
			implMap.get(BaseIntentionUpdateOperator.class).addAll(ap.getSupportedFilterOperators());
			implMap.get(BaseChangeBeliefs.class).addAll(ap.getSupportedChangeOperators());
			implMap.get(BaseViolatesOperator.class).addAll(ap.getSupportedViolatesOperators());
			implMap.get(BaseSubgoalGenerationOperator.class).addAll(ap.getSupportedPlaners());
			
			LOG.info("Operator-Plugin '{}' loaded", ap.getClass().getName());
		}
		
		LOG.info("Load Beliefbase-Plugins:");
		loadedPlugins.clear();
		Collection<BeliefbasePlugin> bbPlugins = new LinkedList<BeliefbasePlugin>(util.getPlugins(BeliefbasePlugin.class));
		for(BeliefbasePlugin bp : bbPlugins) {
			if(loadedPlugins.contains(bp))
				continue;
			loadedPlugins.add(bp);
			implMap.get(BaseBeliefbase.class).addAll(bp.getSupportedBeliefbases());
			implMap.get(BaseReasoner.class).addAll(bp.getSupportedReasoners());
			implMap.get(BaseChangeBeliefs.class).addAll(bp.getSupportedChangeOperations());
			implMap.get(BaseTranslator.class).addAll(bp.getSupportedTranslators());
			
			LOG.info("Beliefbase-Plugin '{}' loaded", bp.getClass().getName());
		}
		
		LOG.info("Load Agent-Plugins:");
		loadedPlugins.clear();
		util.addPluginsFrom(ClassURI.PLUGIN(DefaultAgentPlugin.class));
		Collection<AgentPlugin> aPlugins = new LinkedList<AgentPlugin>(util.getPlugins(AgentPlugin.class));
		for(AgentPlugin ap : aPlugins) {
			if(loadedPlugins.contains(ap))
				continue;
			loadedPlugins.add(ap);

			implMap.get(AgentComponent.class).addAll(ap.getAgentComponents());
			for(Class<? extends AgentComponent> ac : ap.getAgentComponents()) {
				LOG.info("Agent-Component: '{}' loaded.", ac.getName());
			}
			
			implMap.get(Action.class).addAll(ap.getActions());
			for(Class<? extends Action> actc : ap.getActions()) {
				LOG.info("Agent-Action: '{}' loaded.", actc.getSimpleName());
			}
			
			LOG.info("Agent-Pluign '{}' loading complete", ap.getClass().getName());
		}
		
		for(PluginListener pl : listeners) {
			pl.loadingImplementations(this);
		}
	}
	
	/**
	 * creates a new instance of a Generate-Options Operator
	 * @param className	class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseGenerateOptionsOperator createGenerateOptionsOperator(String className) 
			throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseGenerateOptionsOperator.class);
	}
	
	/**
	 * creates a new instance of a intention update operator.
	 * @param className class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseIntentionUpdateOperator createFilterOperator(String className) throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseIntentionUpdateOperator.class);
	}
	
	/**
	 * creates a new instance of a subgoal generation operator
	 * @param className class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseSubgoalGenerationOperator createPlaner(String className) throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseSubgoalGenerationOperator.class);
	}
	
	/**
	 * creates a new instance of an update beliefs operator.
	 * @param className class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseUpdateBeliefsOperator createUpdateOperator(String className) throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseUpdateBeliefsOperator.class);
	}
	
	/**
	 * creates a new instance of a violates operator.
	 * @param className class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseViolatesOperator createViolatesOperator(String className) throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseViolatesOperator.class);
	}
	
	/**
	 * creates a new instance of a belief base.
	 * @param config data structure with configuration parameters for the belief base.
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseBeliefbase createBeliefbase(BeliefbaseConfig config) throws InstantiationException, IllegalAccessException {
		if(config == null)
			throw new IllegalArgumentException("Beliefbase configuration must not null");
		
		BaseBeliefbase bb = createBeliefbase(config.getBeliefbaseClassName());
		bb.generateOperators(config);
		
		return bb;
	}
	
	/**
	 * creates a new instance of a belief base.
	 * @param className class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	protected BaseBeliefbase createBeliefbase(String className) throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseBeliefbase.class);
	}
	
	/**
	 * creates a new instance of a reasoner.
	 * @param className class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseReasoner createReasoner(String className) throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseReasoner.class);
	}
	
	/**
	 * creates a new change beliefs operator instance (like revision or expansion).
	 * @param className class name of the new created instance (inclusive package)
	 * @return reference to the newly created change beliefs operator instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseChangeBeliefs createChange(String className) throws InstantiationException, IllegalAccessException {
		return createInstance(className, BaseChangeBeliefs.class);
	}
	
	/**
	 * Creates a new agent data component like Secrecy Knowledge or Knowhow.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return	reference to the newly created agent component.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public AgentComponent createComponent(String classname) 
			throws InstantiationException, IllegalAccessException {	
		return (AgentComponent) createInstance(classname, AgentComponent.class);
	}
	
	/**
	 * Creates an instance of the given class. The classname must be fully qualified.
	 * That means the java-package and the name of the class like: 
	 * angerona.fw.logic.Desires
	 * @param className	The fully qualified name of the class to instantiate.
	 * @return	A refenrence to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public Object createInstance(String className) throws InstantiationException, IllegalAccessException {
		for(Class<?> baseCls : implMap.keySet()) {
			for(Class<?> realCls : implMap.get(baseCls)) {
				if(realCls.getName().equals(className)) {
					return realCls.newInstance();
				}
			}
		}
		
		throw new InstantiationException("Cannot find Type " + className + " in the plugins.");
	}
	
	/**
	 * A helper method: Creating instances of a specific base type.
	 * @param className	Fully Qualified class name of the impementation type
	 * @param baseType	Class structure of the base type like BaseChangeBeliefs.class
	 * @return	An instance of the type of 'baseType' containing the implementation of 
	 * 			the given java class.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("unchecked")
	protected <T> T createInstance(String className, Class<T> baseType) 
			throws InstantiationException, IllegalAccessException {
		if(className == null)
			throw new IllegalArgumentException("The name of the class must not be null.");
		if(baseType == null)
			throw new IllegalArgumentException("The base type of the instance must not be null.");
		for(Class<?> clsIt : implMap.get(baseType)) {
			if(clsIt.getName().equals(className)) {
				return (T) clsIt.newInstance();
			}
		}
		
		throw new InstantiationException("Cannot find Sub-Type " + className + 
				" of " + baseType.getName() +  " in the plugins.");
	}
}