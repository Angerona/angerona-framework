package angerona.fw;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.PluginManager;
import net.xeoh.plugins.base.impl.PluginManagerFactory;
import net.xeoh.plugins.base.util.PluginManagerUtil;

import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseConsolidation;
import angerona.fw.logic.base.BaseExpansion;
import angerona.fw.logic.base.BaseReasoner;
import angerona.fw.logic.base.BaseRevision;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BasePolicyControlOperator;
import angerona.fw.operators.BaseChangeOperator;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.serialize.BeliefbaseConfiguration;
import angerona.fw.serialize.GlobalConfiguration;

/**
 * This class collects all the implementation of the plugins and gives the user the ability to dynamically
 * instantiate them.
 * @author Tim Janus
 */
public class PluginInstantiator {
	/** reference to the plugin manager used by jspf. */
	private static PluginManager pm;
	
	/** list of all classes implementing the generate options operator */
	private static List<Class<? extends BaseGenerateOptionsOperator>> generateOptionsOperators = new LinkedList<Class<? extends BaseGenerateOptionsOperator>>();

	/** list of all classes implementing the filter operator */
	private static List<Class<? extends BaseIntentionUpdateOperator>> filterOperators = new LinkedList<Class<? extends BaseIntentionUpdateOperator>>();

	/** list of all classes implementing the update operator */
	private static List<Class<? extends BaseChangeOperator>> updateOperators = new LinkedList<Class<? extends BaseChangeOperator>>();

	/** list of all classes implementing the policy-control operator */
	private static List<Class<? extends BasePolicyControlOperator>> policyControlOperators = new LinkedList<Class<? extends BasePolicyControlOperator>>();

	/** list of all classes implementing the violates operator */
	private static List<Class<? extends BaseViolatesOperator>> violatesOperators = new LinkedList<Class<? extends BaseViolatesOperator>>();

	/** list of all classes implementing a planer */
	private static List<Class<? extends BaseSubgoalGenerationOperator>> planers = new LinkedList<Class<? extends BaseSubgoalGenerationOperator>>();
	
	/** list of all classes implementing a beliefbase */
	private static List<Class<? extends BaseBeliefbase>> beliefbases = new LinkedList<Class<? extends BaseBeliefbase>>();
	
	/** list of all classes implementing a reasoner for a belief base */
	private static List<Class<? extends BaseReasoner>> reasoners = new LinkedList<Class<? extends BaseReasoner>>();
	
	/** list of all classes implementing a expansion for a belief base */
	private static List<Class<? extends BaseExpansion>> expansions = new LinkedList<Class<? extends BaseExpansion>>();
	
	/** list of all classes implementing a consolidation for a belief base */
	private static List<Class<? extends BaseConsolidation>> consolidations = new LinkedList<Class<? extends BaseConsolidation>>();
	
	/** list of all classes implementing a revision for a belief base */
	private static List<Class<? extends BaseRevision>> revisions = new LinkedList<Class<? extends BaseRevision>>();
	
	/**
	 * Helper method: Initializes the plugins
	 */
	private static void initPlugins() {
		pm = PluginManagerFactory.createPluginManager();

		GlobalConfiguration config = AngeronaMain.getConfig();
		for(String pluginPath : config.getPluginPaths()) {
			pm.addPluginsFrom(new File(pluginPath).toURI());
		}
		
		PluginManagerUtil pluginManagerUtil = new PluginManagerUtil(pm);
		Collection<OperatorPlugin> opPlugins = new LinkedList<OperatorPlugin>(pluginManagerUtil.getPlugins(OperatorPlugin.class));
		
		for(OperatorPlugin ap : opPlugins) {
			generateOptionsOperators.addAll(ap.getSupportedGenerateOptionsOperators());
			filterOperators.addAll(ap.getSupportedFilterOperators());
			updateOperators.addAll(ap.getSupportedChangeOperators());
			policyControlOperators.addAll(ap.getSupportedPolicyControlOperators());
			violatesOperators.addAll(ap.getSupportedViolatesOperators());
			planers.addAll(ap.getSupportedPlaners());
		}
		
		Collection<BeliefbasePlugin> bbPlugins = new LinkedList<BeliefbasePlugin>(pluginManagerUtil.getPlugins(BeliefbasePlugin.class));
		for(BeliefbasePlugin bp : bbPlugins) {
			beliefbases.addAll(bp.getSupportedBeliefbases());
			reasoners.addAll(bp.getSupportedReasoners());
			expansions.addAll(bp.getSupportedExpansionOperations());
			consolidations.addAll(bp.getSupportedConsolidationOperations());
			revisions.addAll(bp.getSupportedRevisionOperations());
		}
	}
	
	/** @return list with all Generate-Options operators */
	public static List<Class<? extends BaseGenerateOptionsOperator>> getGenerateOptionsOperators() {
		if(pm == null) initPlugins();
		return generateOptionsOperators;
	}

	/** @return list with all filter operators */
	public static List<Class<? extends BaseIntentionUpdateOperator>> getFilterOperators() {
		if(pm == null) initPlugins();
		return filterOperators;
	}

	/** @return list with all update operators */
	public static List<Class<? extends BaseChangeOperator>> getUpdateOperators() {
		if(pm == null) initPlugins();
		return updateOperators;
	}

	/** @return list with all policy-control operators */
	public static List<Class<? extends BasePolicyControlOperator>> getPolicyControlOperators() {
		if(pm == null) initPlugins();
		return policyControlOperators;
	}

	/** @return list with all violates operators */
	public static List<Class<? extends BaseViolatesOperator>> getViolatesOperators() {
		if(pm == null) initPlugins();
		return violatesOperators;
	}

	/** @return list with all planer operators */
	public static List<Class<? extends BaseSubgoalGenerationOperator>> getPlaners() {
		if(pm == null) initPlugins();
		return planers;
	}
	
	/** @return list with all belief base operators */
	public static List<Class<? extends BaseBeliefbase>> getBeliefbases() {
		if(pm == null) initPlugins();
		return beliefbases;
	}
	
	/** @return list with all Reasoner operators */
	public static List<Class<? extends BaseReasoner>> getReasoners() {
		if(pm == null) initPlugins();
		return reasoners;
	}
	
	/** @return list with all Expansion operators */
	public static List<Class<? extends BaseExpansion>> getExpansions() {
		if(pm == null) initPlugins();
		return expansions;
	}
	
	/** @return list with all Consolidation operators */
	public static List<Class<? extends BaseConsolidation>> getConsolidations() {
		if(pm == null) initPlugins();
		return consolidations;
	}
	
	/** @return list with all Revision operators */
	public static List<Class<? extends BaseRevision>> getRevisions() {
		if(pm == null) initPlugins();
		return revisions;
	}
	
	/**
	 * creates a new instance of a Generate-Options Operator
	 * @param classname	class name of the new created instance (inclusive package)
	 * @return reference to the newly created instance.
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static BaseGenerateOptionsOperator createGenerateOptionsOperator(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseIntentionUpdateOperator createFilterOperator(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseSubgoalGenerationOperator createPlaner(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseChangeOperator createUpdateOperator(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseChangeOperator> c : getUpdateOperators()) {
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
	public static BasePolicyControlOperator createPolicyControlOperator(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseViolatesOperator createViolatesOperator(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseBeliefbase createBeliefbase(BeliefbaseConfiguration config) throws InstantiationException, IllegalAccessException {
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
	public static BaseBeliefbase createBeliefbase(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseReasoner createReasoner(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseExpansion createExpansion(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseConsolidation createConsolidation(String classname) throws InstantiationException, IllegalAccessException {
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
	public static BaseRevision createRevision(String classname) throws InstantiationException, IllegalAccessException {
		for(Class<? extends BaseRevision> c : getRevisions()) {
			if(c.getName().compareTo(classname) == 0) {
				return c.newInstance();
			}
		}

		throw new InstantiationException("Can't find Revision with name: " + classname );
	}
}
