package angerona.fw.internal;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.AgentComponent;
import angerona.fw.AngeronaPlugin;
import angerona.fw.BaseBeliefbase;
import angerona.fw.EnvironmentBehavior;
import angerona.fw.listener.PluginListener;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.BaseOperator;
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
	
	private Set<AngeronaPlugin> loadedPlugins = new HashSet<>();
	
	/** a map of Classes defining the basis type to a set of classes defining implementations */
	private Map<Class<?>, Set<Class<?>>> implMap = new HashMap<Class<?>, Set<Class<?>>>();
	
	private Map<String, BaseOperator> operatorInstanceMap = new HashMap<>();
	
	
	/** @return the utility class of the plugin API to load plugins ect. */
	public PluginManagerUtil getPluginUtil() {
		return util;
	}
	
	/** Ctor: Initializes the simple plugin framework, private to force singleton paradigm. */
	private PluginInstantiator() {
		implMap.put(BaseOperator.class, new HashSet<Class<?>>());
		implMap.put(BaseBeliefbase.class, new HashSet<Class<?>>());
		implMap.put(BaseReasoner.class, new HashSet<Class<?>>());
		implMap.put(BaseChangeBeliefs.class, new HashSet<Class<?>>());
		implMap.put(BaseTranslator.class, new HashSet<Class<?>>());
		implMap.put(AgentComponent.class, new HashSet<Class<?>>());
		implMap.put(EnvironmentBehavior.class, new HashSet<Class<?>>());
		
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
	 * Checks if the given class is registered as an implementation at the plugin instantiator.
	 * @param impl	The implementation class
	 * @return		true if the class is registered false otherwise.
	 */
	public boolean isImplementationRegistered(Class<?> impl) {
		for(Set<Class<?>> set : implMap.values()) {
			for(Class<?> cls : set) {
				if(cls.equals(impl)) {
					return true;
				}
			}
		}
		return false;
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
	 * @return		true if the removal was successful, false otherwise.
	 */
	public boolean removeListener(PluginListener pl) {
		return this.listeners.remove(pl);
	}
	
	/** Deregisteres all listeners */
	public void removeAllListener() {
		this.listeners.clear();
	}
	
	/**
	 * Helper method: Collecting the set of all interfaces implemented by the
	 * given class
	 * @param cls	The class description
	 * @return	A set containing all interfaces implemented by the given class.
	 */
	private Set<Class<?>> getAllInterfaces(Class<?> cls) {
		Set<Class<?>> reval = new HashSet<>();
		while(cls != null) {
			for(Class<?> i : cls.getInterfaces()) {
				reval.add(i);
			}
			cls = cls.getSuperclass();
		}
		return reval;
	}
	
	/**
	 * 	Loads the plugins for the core Angerona Framework and informs the listeners about the
	 * 	loading. The listeners can load different plugins on their own like the gui extension:
	 * 	It loads UI-Components to enhance the GUI with views for the data components defined
	 * 	in different plugins.
	 * @todo listener used to load UI Plugins, find easier concept
	 */
	private void loadAllPlugins() {
		List<AngeronaPlugin> plugins = new LinkedList<AngeronaPlugin>();
		plugins.addAll(util.getPlugins(AngeronaPlugin.class));
		
		for(AngeronaPlugin plugin : plugins) {
			registerPlugin(plugin);
		}
		
		for(PluginListener pl : listeners) {
			pl.loadingImplementations(this);
		}
	}

	/**
	 * Helper method: Tests if the given cls implements the BaseOperator interface
	 * and if this is the case creates an operator instance in the operator instance
	 * map.
	 * @todo move in extra class.
	 * @param cls	The cls which might be an implementation of BaseOperator
	 */
	private void createOperatorInstance(Class<?> cls) {
		if( getAllInterfaces(cls).contains(BaseOperator.class) &&
			!operatorInstanceMap.containsKey(cls.getName())) {		
			BaseOperator op = null;
			try {
				op = (BaseOperator)createInstance(cls.getName());
				operatorInstanceMap.put(cls.getName(), op);
			} catch (InstantiationException|IllegalAccessException e) {
				e.printStackTrace();
				LOG.error("Cannot instantiate '{}': '{}'", cls.getName(), e.getMessage());
			}
		}
	}
	
	/**
	 * Tests if the given class implements the BaseOperator interface and if this
	 * is the case destroy the operator instance in the operator instance map.
	 * @todo move operator instance map in own class
	 * @param cls
	 */
	private void destroyOperatorInstance(Class<?> cls) {
		if( getAllInterfaces(cls).contains(BaseOperator.class) &&
			operatorInstanceMap.containsKey(cls.getName())) {		
			operatorInstanceMap.remove(cls.getName());
		}
	}

	/** 
	 * Registers the given plugin in the plugin instatiator. That means all
	 * implemented classes are stored in the base class to implementation map
	 * and if the class is an operator the global instance is generated.
	 * @param plugin	The AngeronaPlugin having the list of class definitions
	 */
	public void registerPlugin(AngeronaPlugin plugin) {
		if(!loadedPlugins.contains(plugin)) {
			Map<Class<?>, List<Class<?>>> temp = createTemporaryPluginMap(plugin);

			for(Class<?> base : temp.keySet()) {
				for(Class<?> impl : temp.get(base)) {
					register(base, impl);
					createOperatorInstance(impl);
				}
			}
			
			loadedPlugins.add(plugin);
			LOG.info("Plugin '{}' loaded", plugin.getClass().getName());
		} else {
			LOG.warn("Plugin '{}' already loaded", plugin.getClass().getName());
		}
	}
	
	/**
	 * Unregisters the given plugin. That means the implementation provided by the plugin
	 * are removed from the base class to implementation map of the plugin instantiator and
	 * if the implementation is an operator than the gloab operator instance is destroyed.
	 * @param plugin	The AngeronaPlugin which shall be unregistered.
	 */
	public void unregisterPlugin(AngeronaPlugin plugin) {
		if(loadedPlugins.contains(plugin)) {
			Map<Class<?>, List<Class<?>>> temp = createTemporaryPluginMap(plugin);
			for(Class<?> base : temp.keySet()) {
				for(Class<?> impl : temp.get(base)) {
					unregister(base, impl);
					destroyOperatorInstance(impl);
				}
			}
		}
	}
	
	/**
	 * Helper method: registers the given implementation in the base class to implementation map
	 * and informs the logging facility.
	 * @param base	The base class
	 * @param impl	The implementation class
	 */
	private void register(Class<?> base, Class<?> impl) {
		implMap.get(base).add(impl);
		LOG.info("Registered Implementation '{}' for '{}'.", impl.getName(), base.getName());
	}
	
	/**
	 * Helper method: unregisters the given implementation in the base class to implementation map
	 * and informs the logging facility.
	 * @param base	The base class
	 * @param impl	The implementation class
	 */
	private void unregister(Class<?> base, Class<?> impl) {
		implMap.get(base).remove(impl);
		LOG.info("Unregistered implementation '{}' for '{}'", impl.getName(), base.getName());
	}
	
	/**
	 * Helper method: creates a temporary map from base classes to implementation for use in
	 * the registerPlugin() and unregisterPlugin() methods.
	 * @param plugin	The plugin acting as data basis for the map
	 * @return			A map containg a mapping between base classes to implementations for the
	 * 					given plugin.
	 */
	private Map<Class<?>, List<Class<?>>> createTemporaryPluginMap(
			AngeronaPlugin plugin) {
		Map<Class<?>, List<Class<?>>> temp = new HashMap<Class<?>, List<Class<?>>>();
		temp.put(AgentComponent.class, new LinkedList<Class<?>>(plugin.getAgentComponentImpl()));
		
		temp.put(BaseBeliefbase.class, new LinkedList<Class<?>>(plugin.getBeliefbaseImpl()));
		temp.put(BaseReasoner.class, new LinkedList<Class<?>>(plugin.getReasonerImpl()));
		temp.put(BaseChangeBeliefs.class, new LinkedList<Class<?>>(plugin.getChangeImpl()));
		temp.put(BaseTranslator.class, new LinkedList<Class<?>>(plugin.getTranslatorImpl()));
		
		temp.put(BaseOperator.class, new LinkedList<Class<?>>(plugin.getOperators()));
		
		temp.put(EnvironmentBehavior.class, new LinkedList<Class<?>>(plugin.getEnvironmentBehaviors()));
		return temp;
	}
	
	/**
	 * @param fullJavaClsName The full java class name of the operator class which shall be returned
	 * @return	The operator with the given class name or null if it does not exists.
	 */
	public BaseOperator getOperator(String fullJavaClsName) {
		if(operatorInstanceMap.containsKey(fullJavaClsName)) {
			return operatorInstanceMap.get(fullJavaClsName);
		}
		return null;
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
	 * Creates a new agent data component like Secrecy Knowledge or Knowhow.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return	reference to the newly created agent component.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public AgentComponent createComponent(String classname) 
			throws InstantiationException, IllegalAccessException {	
		return  createInstance(classname, AgentComponent.class);
	}
	
	/**
	 * Creates a new enviornment behavior to define the communication between
	 * the enviornment, external simulation software and the agents.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return	reference to the newly created environment behavior.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public EnvironmentBehavior createEnvironmentBehavior(String classname) 
			throws InstantiationException, IllegalAccessException {
		return (EnvironmentBehavior) createInstance(classname, EnvironmentBehavior.class);
	}
	
	/**
	 * Creates an instance of the given class. The classname must be fully qualified.
	 * That means the java-package and the name of the class like: 
	 * angerona.fw.logic.Desires
	 * @param className	The fully qualified name of the class to instantiate.
	 * @return	A reference to the newly created instance.
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