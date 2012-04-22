package angerona.fw;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;
import net.xeoh.plugins.base.util.uri.ClassURI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseConsolidation;
import angerona.fw.logic.base.BaseExpansion;
import angerona.fw.logic.base.BaseReasoner;
import angerona.fw.logic.base.BaseChangeBeliefs;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BasePolicyControlOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.serialize.BeliefbaseConfiguration;

/**
 * This class collects all the implementation of the plugins and gives the user the ability to dynamically
 * instantiate them.
 * @author Tim Janus
 */
public class PluginInstantiator {
	private static PluginInstantiator instance;
	
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
	
	private List<PluginListener> listeners = new LinkedList<PluginListener>();
	
	/** the plugin manager utiltiy */
	private PluginManagerUtil util;
	
	private List<Plugin> loadedPlugins = new LinkedList<Plugin>();
	
	/** list of all classes implementing the generate options operator */
	private List<Class<? extends BaseGenerateOptionsOperator>> generateOptionsOperators = new LinkedList<Class<? extends BaseGenerateOptionsOperator>>();

	/** list of all classes implementing the filter operator */
	private List<Class<? extends BaseIntentionUpdateOperator>> filterOperators = new LinkedList<Class<? extends BaseIntentionUpdateOperator>>();

	/** list of all classes implementing the update operator */
	private List<Class<? extends BaseUpdateBeliefsOperator>> updateOperators = new LinkedList<Class<? extends BaseUpdateBeliefsOperator>>();

	/** list of all classes implementing the policy-control operator */
	private List<Class<? extends BasePolicyControlOperator>> policyControlOperators = new LinkedList<Class<? extends BasePolicyControlOperator>>();

	/** list of all classes implementing the violates operator */
	private List<Class<? extends BaseViolatesOperator>> violatesOperators = new LinkedList<Class<? extends BaseViolatesOperator>>();

	/** list of all classes implementing a planer */
	private List<Class<? extends BaseSubgoalGenerationOperator>> planers = new LinkedList<Class<? extends BaseSubgoalGenerationOperator>>();
	
	/** list of all classes implementing a beliefbase */
	private List<Class<? extends BaseBeliefbase>> beliefbases = new LinkedList<Class<? extends BaseBeliefbase>>();
	
	/** list of all classes implementing a reasoner for a belief base */
	private List<Class<? extends BaseReasoner>> reasoners = new LinkedList<Class<? extends BaseReasoner>>();
	
	/** list of all classes implementing a expansion for a belief base */
	private List<Class<? extends BaseExpansion>> expansions = new LinkedList<Class<? extends BaseExpansion>>();
	
	/** list of all classes implementing a consolidation for a belief base */
	private List<Class<? extends BaseConsolidation>> consolidations = new LinkedList<Class<? extends BaseConsolidation>>();
	
	/** list of all classes implementing a revision for a belief base */
	private List<Class<? extends BaseChangeBeliefs>> revisions = new LinkedList<Class<? extends BaseChangeBeliefs>>();
	
	/** list of all classes implementing a custom agent component */
	private List<Class<? extends AgentComponent>> components = new LinkedList<Class<? extends AgentComponent>>();
	
	public PluginManagerUtil getPluginUtil() {
		return util;
	}
	
	/**
	 * Ctor: Initializes the simple plugin framework.
	 */
	private PluginInstantiator() {
		pm = PluginManagerFactory.createPluginManager();
		util = new PluginManagerUtil(pm);
	}
	
	public void addPlugin(String path) {
		pm.addPluginsFrom(new File(path).toURI());
		loadAllPlugins();
	}
	
	public void addPlugins(Collection<String> paths) {
		for(String path : paths) {
			pm.addPluginsFrom(new File(path).toURI());
		}
		loadAllPlugins();
	}

	public void addListener(PluginListener pl) {
		this.listeners.add(pl);
	}
	
	public boolean removeListener(PluginListener pl) {
		return this.listeners.remove(pl);
	}
	
	public void removeAllListener() {
		this.listeners.clear();
	}
	
	private void loadAllPlugins() {
		Collection<OperatorPlugin> opPlugins = new LinkedList<OperatorPlugin>(util.getPlugins(OperatorPlugin.class));
		LOG.info("Load Operator-Plugins:");
		for(OperatorPlugin ap : opPlugins) {
			if(loadedPlugins.contains(ap))
				continue;
			loadedPlugins.add(ap);
			generateOptionsOperators.addAll(ap.getSupportedGenerateOptionsOperators());
			filterOperators.addAll(ap.getSupportedFilterOperators());
			updateOperators.addAll(ap.getSupportedChangeOperators());
			policyControlOperators.addAll(ap.getSupportedPolicyControlOperators());
			violatesOperators.addAll(ap.getSupportedViolatesOperators());
			planers.addAll(ap.getSupportedPlaners());
			LOG.info("Operator-Plugin '{}' loaded", ap.getClass().getName());
		}
		
		LOG.info("Load Beliefbase-Plugins:");
		Collection<BeliefbasePlugin> bbPlugins = new LinkedList<BeliefbasePlugin>(util.getPlugins(BeliefbasePlugin.class));
		for(BeliefbasePlugin bp : bbPlugins) {
			if(loadedPlugins.contains(bp))
				continue;
			loadedPlugins.add(bp);
			beliefbases.addAll(bp.getSupportedBeliefbases());
			reasoners.addAll(bp.getSupportedReasoners());
			expansions.addAll(bp.getSupportedExpansionOperations());
			consolidations.addAll(bp.getSupportedConsolidationOperations());
			revisions.addAll(bp.getSupportedRevisionOperations());
			LOG.info("Beliefbase-Plugin '{}' loaded", bp.getClass().getName());
		}
		
		LOG.info("Load Agent-Plugins:");
		util.addPluginsFrom(ClassURI.PLUGIN(DefaultAgentPlugin.class));
		Collection<AgentPlugin> aPlugins = new LinkedList<AgentPlugin>(util.getPlugins(AgentPlugin.class));
		for(AgentPlugin ap : aPlugins) {
			if(loadedPlugins.contains(ap))
				continue;
			loadedPlugins.add(ap);
			components.addAll(ap.getAgentComponents());
			LOG.info("Agent-Pluign '{}' loaded", ap.getClass().getName());
		}
		
		for(PluginListener pl : listeners) {
			pl.loadingImplementations(this);
		}
	}
	
	/** @return list with all Generate-Options operators */
	public List<Class<? extends BaseGenerateOptionsOperator>> getGenerateOptionsOperators() {
		return Collections.unmodifiableList(generateOptionsOperators);
	}

	/** @return list with all filter operators */
	public List<Class<? extends BaseIntentionUpdateOperator>> getFilterOperators() {
		return Collections.unmodifiableList(filterOperators);
	}

	/** @return list with all update operators */
	public List<Class<? extends BaseUpdateBeliefsOperator>> getUpdateOperators() {
		return Collections.unmodifiableList(updateOperators);
	}

	/** @return list with all policy-control operators */
	public List<Class<? extends BasePolicyControlOperator>> getPolicyControlOperators() {
		return Collections.unmodifiableList(policyControlOperators);
	}

	/** @return list with all violates operators */
	public List<Class<? extends BaseViolatesOperator>> getViolatesOperators() {
		return Collections.unmodifiableList(violatesOperators);
	}

	/** @return list with all planer operators */
	public List<Class<? extends BaseSubgoalGenerationOperator>> getPlaners() {
		return Collections.unmodifiableList(planers);
	}
	
	/** @return list with all belief base operators */
	public List<Class<? extends BaseBeliefbase>> getBeliefbases() {
		return Collections.unmodifiableList(beliefbases);
	}
	
	/** @return list with all Reasoner operators */
	public List<Class<? extends BaseReasoner>> getReasoners() {
		return Collections.unmodifiableList(reasoners);
	}
	
	/** @return list with all Expansion operators */
	public List<Class<? extends BaseExpansion>> getExpansions() {
		return Collections.unmodifiableList(expansions);
	}
	
	/** @return list with all Consolidation operators */
	public List<Class<? extends BaseConsolidation>> getConsolidations() {
		return Collections.unmodifiableList(consolidations);
	}
	
	/** @return list with all Revision operators */
	public List<Class<? extends BaseChangeBeliefs>> getRevisions() {
		return Collections.unmodifiableList(revisions);
	}
	
	public List<Class<? extends AgentComponent>> getComponents() {
		return Collections.unmodifiableList(components);
	}
	
	/**
	 * creates a new instance of a Generate-Options Operator
	 * @param classname	class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseGenerateOptionsOperator createGenerateOptionsOperator(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseGenerateOptionsOperator> c : getGenerateOptionsOperators()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Can't find Generate-Options-Operator with name: " + classname );
	}
	
	/**
	 * creates a new instance of a filter operator.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseIntentionUpdateOperator createFilterOperator(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseIntentionUpdateOperator> c : getFilterOperators()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Can't find Filter-Operator with name: " + classname );
	}
	
	/**
	 * creates a new instance of a planer
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseSubgoalGenerationOperator createPlaner(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseSubgoalGenerationOperator> c : getPlaners()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Can't find Planer type with name: " + classname );
	}
	
	/**
	 * creates a new instance of an update operator.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseUpdateBeliefsOperator createUpdateOperator(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseUpdateBeliefsOperator> c : getUpdateOperators()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}
		
		throw new InstantiationException("Can't find Update-Operator with name: " + classname );
	}
	
	/**
	 * creates a new instance of a policy-control operator.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BasePolicyControlOperator createPolicyControlOperator(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BasePolicyControlOperator> c : getPolicyControlOperators()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}
		
		throw new InstantiationException("Can't find Policy-Control-Operator with name: " + classname );
	}
	
	/**
	 * creates a new instance of a violates operator.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseViolatesOperator createViolatesOperator(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseViolatesOperator> c : getViolatesOperators()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}
		
		throw new InstantiationException("Can't find Violates-Operator with name: " + classname );
	}
	
	/**
	 * creates a new instance of a belief base.
	 * @param config data-structure with configuration parameters for the belief base.
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseBeliefbase createBeliefbase(BeliefbaseConfiguration config) throws InstantiationException, IllegalAccessException {
		if(config == null)
			throw new IllegalArgumentException("Beliefbase configuration must not null");
		
		BaseBeliefbase bb = createBeliefbase(config.getBeliefbaseClassName());
		bb.generateOperators(config);
		
		return bb;
	}
	
	/**
	 * creates a new instance of a belief base.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseBeliefbase createBeliefbase(String classname) throws InstantiationException, IllegalAccessException {
		if(classname == null)
			throw new IllegalArgumentException("The name of the class must not be null.");
		
		for(Class<? extends BaseBeliefbase> c : getBeliefbases()) {
			if(c.getName().compareTo(classname) == 0) {
				BaseBeliefbase bb = c.newInstance();;
				return bb;
			}
		}

		throw new InstantiationException("Can't find Beliefbase type with name: " + classname );
	}
	
	/**
	 * creates a new instance of a reasoner.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseReasoner createReasoner(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseReasoner> c : getReasoners()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Can't find Reasoner with name: " + classname );
	}
	
	/**
	 * creates a new expansion operator instance.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseExpansion createExpansion(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseExpansion> c : getExpansions()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Can't find Expansion with name: " + classname );
	}
	
	/**
	 * creates a new consolidation operator instance.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created consolidation operator instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseConsolidation createConsolidation(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseConsolidation> c : getConsolidations()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Can't find Consolidation with name: " + classname );
	}
	
	/**
	 * creates a new revision operator instance.
	 * @param classname class name of the new created instance (inclusive package)
	 * @return reference to the newly created revision operator instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public BaseChangeBeliefs createRevision(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseChangeBeliefs> c : getRevisions()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Cannot find Revision with name: " + classname );
	}
	
	public AgentComponent createComponent(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends AgentComponent> c : getComponents()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}
		
		throw new InstantiationException("Cannot find Agent-Component with name: " + classname);
	}
}
