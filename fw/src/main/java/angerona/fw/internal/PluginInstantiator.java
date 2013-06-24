package angerona.fw.internal;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
 * 	This class loads all the implementation of the plugins and povides 
 * 	the ability to dynamically instantiate them.
 * 
 * 	This is only a storage which saves the class implementations loaded by
 * 	the plugins and provides method to instantiate those implementations.
 * 
 * 	@author Tim Janus
 */
public class PluginInstantiator {
	
	/** reference to logger implementation */
	private Logger LOG = LoggerFactory.getLogger(PluginInstantiator.class);
	
	/** reference to the plugin manager used by jspf. */
	private PluginManager pm;
	
	/** the listeners get informed if the plugin instantiator loads implementations */ 
	private List<PluginListener> listeners = new LinkedList<PluginListener>();
	
	/** the plugin manager utiltiy */
	private PluginManagerUtil util;
	
	/** set of already loaded plugins */
	private Set<AngeronaPlugin> loadedPlugins = new HashSet<>();
	
	/** a map of Classes defining the basis type to a set of classes defining implementations */
	private Map<Class<?>, Set<Class<?>>> implMap = new HashMap<Class<?>, Set<Class<?>>>();
	
	/** Ctor: Initializes the simple plugin framework, private to force singleton paradigm. */
	private PluginInstantiator() {
		implMap = createImplementationMap(null);		
		pm = PluginManagerFactory.createPluginManager();
		util = new PluginManagerUtil(pm);
	}
	
	/** @return the utility class of the plugin API to load plugins ect. */
	public PluginManagerUtil getPluginUtil() {
		return util;
	}
	
	public Map<Class<?>, Set<Class<?>>> getImplementationMap() {
		return Collections.unmodifiableMap(implMap);
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
	 * 	Loads the plugins for the core Angerona Framework and informs the listeners about the
	 * 	loading. The listeners can load different plugins on their own like the gui extension:
	 * 	It loads UI-Components to enhance the GUI with views for the data components defined
	 * 	in different plugins.
	 * @todo listener used to load UI Plugins, find other concept
	 */
	private void loadAllPlugins() {
		List<AngeronaPlugin> plugins = new LinkedList<AngeronaPlugin>();
		plugins.addAll(util.getPlugins(AngeronaPlugin.class));
		
		for(AngeronaPlugin plugin : plugins) {
			registerPlugin(plugin);
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
			for(PluginListener listener : listeners) {
				listener.loadingPlugin(plugin);
			}
			
			Map<Class<?>, Set<Class<?>>> temp = createImplementationMap(plugin);

			for(Entry<Class<?>, Set<Class<?>>> ent : temp.entrySet()) {
				for(Class<?> impl : ent.getValue()) {
					register(ent.getKey(), impl);
				}
			}
			
			loadedPlugins.add(plugin);
			for(PluginListener listener : listeners) {
				listener.loadedPlugin(plugin);
			}
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
			for(PluginListener listener : listeners) {
				listener.unloadingPlugin(plugin);
			}
			
			Map<Class<?>, Set<Class<?>>> temp = createImplementationMap(plugin);
			for(Entry<Class<?>, Set<Class<?>>> ent : temp.entrySet()) {
				for(Class<?> impl : ent.getValue()) {
					unregister(ent.getKey(), impl);
				}
			}
			
			for(PluginListener listener : listeners) {
				listener.unloadedPlugin(plugin);
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
		onRegistered(base, impl);
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
		onUnregistered(base, impl);
		LOG.info("Unregistered implementation '{}' for '{}'", impl.getName(), base.getName());
	}

	/** 
	 * Helper method invokes the listeners and informs them about the unregistration of
	 * the implementation.
	 * @param base
	 * @param impl
	 */
	protected void onUnregistered(Class<?> base, Class<?> impl) {
		for(PluginListener listener : listeners) {
			listener.implementationUnregistered(base, impl);
		}
	}
	
	/** 
	 * Helper method invokes the listeners and informs them about the registration of
	 * the implementation.
	 * @param base
	 * @param impl
	 */
	protected void onRegistered(Class<?> base, Class<?> impl) {
		for(PluginListener listener : listeners) {
			listener.implementationRegistered(base, impl);
		}
	}
	
	/**
	 * Helper method: creates a temporary map from base classes to implementation for use in
	 * the registerPlugin() and unregisterPlugin() methods.
	 * @param plugin	The plugin acting as data basis for the map, might be null then the returned
	 * 					map only contains empty sets for the implementations.
	 * @return			A map containg a mapping between base classes to implementations.
	 */
	private Map<Class<?>, Set<Class<?>>> createImplementationMap(
			AngeronaPlugin plugin) {
		Map<Class<?>, Set<Class<?>>> temp = new HashMap<Class<?>, Set<Class<?>>>();
		temp.put(AgentComponent.class, new HashSet<Class<?>>());
		
		temp.put(BaseBeliefbase.class, new HashSet<Class<?>>());
		temp.put(BaseReasoner.class, new HashSet<Class<?>>());
		temp.put(BaseChangeBeliefs.class, new HashSet<Class<?>>());
		temp.put(BaseTranslator.class, new HashSet<Class<?>>());
		
		temp.put(BaseOperator.class, new HashSet<Class<?>>());
		
		temp.put(EnvironmentBehavior.class, new HashSet<Class<?>>());
		
		if(plugin != null) {
			temp.get(AgentComponent.class).addAll(plugin.getAgentComponentImpl());
			
			temp.get(BaseBeliefbase.class).addAll(plugin.getBeliefbaseImpl());
			temp.get(BaseReasoner.class).addAll(plugin.getReasonerImpl());
			temp.get(BaseChangeBeliefs.class).addAll(plugin.getChangeImpl());
			temp.get(BaseTranslator.class).addAll(plugin.getTranslatorImpl());
			
			temp.get(BaseOperator.class).addAll(plugin.getOperators());
			
			temp.get(EnvironmentBehavior.class).addAll(plugin.getEnvironmentBehaviors());
		}
		return temp;
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
	
	/** singleton instance */
	private static PluginInstantiator instance;
	
	/** @return the only instance of the PluginInstantiator */
	public static PluginInstantiator getInstance() {
		if(instance == null) {
			instance = new PluginInstantiator();
		}
		return instance;
	}
}