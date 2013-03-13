package angerona.fw;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sf.beenuts.ap.AgentArchitecture;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.asml.CommandSequence;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.internal.AngeronaReporter;
import angerona.fw.internal.Entity;
import angerona.fw.internal.IdGenerator;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.listener.ActionProcessor;
import angerona.fw.listener.AgentListener;
import angerona.fw.listener.SubgoalListener;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.Desires;
import angerona.fw.operators.BaseOperator;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.OperatorStack;
import angerona.fw.parser.ParseException;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextProvider;
import angerona.fw.report.ReportPoster;
import angerona.fw.report.Reporter;
import angerona.fw.serialize.AgentConfig;
import angerona.fw.serialize.AgentInstance;
import angerona.fw.serialize.BeliefbaseConfig;
import angerona.fw.serialize.OperationSetConfig;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * Implementation of an agent in the Angerona Framework.
 * An agent defines it functionality by the used operator instances. The
 * data structure AgentConfiguration is used to dynamically instantiate
 * an agent.
 * The agent defines helper methods to use the operators of the agent.
 * @author Tim Janus
 * @author Daniel Dilger
 */
public class Agent extends AgentArchitecture 
	implements ContextProvider
	, Entity
	, OperatorStack
	, ReportPoster
	, Reporter
	, OperatorCaller
	, ActionProcessor
	, PropertyChangeListener {

	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(Agent.class);
	
	/** the beenuts agent process used for low level communication in the framework */
	private AngeronaAgentProcess agentProcess;
	
	/** The belief base of the agent */
	private Beliefs beliefs;
	
	/** id in the report-attachment hierarchy. */
	private Long id;
	
	private AngeronaReporter reporter;
	
	private List<Long> childrenIds = new LinkedList<Long>();
	
	private List<AgentComponent> customComponents = new LinkedList<AgentComponent>();
	
	private List<SubgoalListener> subgoalListeners = new LinkedList<SubgoalListener>();
	
	private List<AgentListener> listeners = new LinkedList<AgentListener>();
	
	/** The context of the agents used for dynamic code defined in xml files (intentions) */
	private Context context;
	
	/** History of actions performed by the agent */
	private List<Action> actionsHistory = new LinkedList<Action>();
	
	/** a list of capabilities which describe actions the agent can perform*/
	private List<String> capabilities = new LinkedList<>();
	
	private OperatorProvider operators = new OperatorProvider();
	
	/** reference to the current used operator in the cycle process. */
	private Stack<BaseOperator> operatorStack = new Stack<BaseOperator>();
	
	/** the perception received by the last or running cylce call */
	private Perception currentPerception;
	
	/** the last perception used in the updateBeliefs Method */
	private Perception lastUpdateBeliefsPercept;
	
	/** object represents the last action performed by the agent. */
	private Action lastAction;
	
	private CommandSequence asmlCylce;
	
	public OperatorProvider getOperators() {
		return operators;
	}
	
	public Agent(String name) {
		// init beenuts stuff
		agentProcess = new AngeronaAgentProcess(name);
		agentProcess.setAgentArchitecture(this);
		init(agentProcess);
	}
	
	/** @return a list containing all actions peformed by the agent. */
	public List<Action> getActionHistory() {
		return actionsHistory;
	}	
	
	public List<String> getCapabilities() {
		return Collections.unmodifiableList(capabilities);
	}
	
	public boolean hasCapability(String capabilityName) {
		return capabilities.contains(capabilityName);
	}
	
	/**
	 * Sets the belief bases of the agent.
	 * @param world			The world knowledge of this agents
	 * @param views			A map of agent names to belief bases representing the views onto the knowledge of other agents 
	 */
	public void setBeliefs(BaseBeliefbase world, Map<String, BaseBeliefbase> views) {
		// remove old belief entities from the agent
		if(beliefs != null) {
			childrenIds.remove(beliefs.getWorldKnowledge().getGUID());
			beliefs.getWorldKnowledge().removePropertyChangeListener(this);
			for(String name : beliefs.getViewKnowledge().keySet()) {
				BaseBeliefbase act = beliefs.getViewKnowledge().get(name);
				act.removePropertyChangeListener(this);
				childrenIds.remove(act.getGUID());
			}
		}
		
		// create new beliefs and register as entities of the agent:
		beliefs = new Beliefs(world, views);
		childrenIds.add(world.getGUID());
		world.setParent(id);
		world.addPropertyChangeListener(this);
		for(String name : views.keySet()) {
			BaseBeliefbase bb = views.get(name);
			childrenIds.add(bb.getGUID());
			bb.setParent(id);
			bb.addPropertyChangeListener(this);
		}
		regenContext();
	}
	
	/**
	 * creates the belief bases, operators and components of the agent.
	 * @param ai	The configuration of the agent
	 * @throws AgentInstantiationException
	 */
	public void create(AgentInstance ai, SimulationConfiguration config) 
			throws AgentInstantiationException {
		this.id = new Long(IdGenerator.generate(this));
		context = new Context();
		this.reporter = new AngeronaReporter(this, this);
		
		capabilities.addAll(ai.getCapabilities());
		
		// load cylce script and link supported operators 
		asmlCylce = ai.getConfig().getCycleScript();
		for(OperationSetConfig osc : ai.getConfig().getOperations()) {
			if(!operators.addOperationSet(osc)) {
				throw new AgentInstantiationException("Cannot create operation-set: '" + 
						osc.getOperationType() + "'");
			}
		}
		
		createAgentComponents(ai);
		createBeliefbases(ai, config);
		parseBeliefbases(ai);
		
		// add desire component if necessary.
		Desires desires = getDesires();
		if(desires == null && ai.getDesires().size() > 0) {
			LOG.warn("No desire-component added to agent '{}' but desires, auto-add the desire component.", getName());
			desires = new Desires();
			addComponent(desires);
		}

		// init the custom components
		for(AgentComponent ac : customComponents) {
			ac.init(ai.getAdditionalData());
		}
	}

	/**
	 * Helper method: parses the belief base content for all the belief bases of the agent. That
	 * means one world belief base and one view belief base for every other agent is parsed.
	 * @param ai
	 * @throws AgentInstantiationException
	 */
	private void parseBeliefbases(AgentInstance ai)
			throws AgentInstantiationException {
		String errorOutput = null;
		String dir = getEnvironment().getDirectory() + "/";
		
		try {
			// parse the content of the world belief base:
			BaseBeliefbase world = getBeliefs().getWorldKnowledge();
			File worldBBFile = new File(dir + ai.getBeliefbaseName() + "." + world.getFileEnding());
			if(worldBBFile.exists()) {
				world.parse(new BufferedReader(new FileReader(worldBBFile)));
			} else {
				LOG.warn("No world belief base file for '{}'.", ai.getName());
			}
			// parse the content of every view belief base:
			Map<String, BaseBeliefbase> views = getBeliefs().getViewKnowledge();
			for(String key : views.keySet()) {
				BaseBeliefbase actView = views.get(key);
				File viewFile = new File(dir + ai.getBeliefbaseName() + "_" + key + "." + actView.getFileEnding());
				if(viewFile.exists()) {
					actView.parse(new BufferedReader(new FileReader(viewFile)));
				} else {
					LOG.warn("No belief base file for view of '{}'->'{}'.",ai.getName(), key);
				}
			}
		} catch (FileNotFoundException e) {
			errorOutput = "Cannot create agent '" + getName() + "' file not found occured: " + e.getMessage();
			e.printStackTrace();
		} catch (IOException ex) {
			errorOutput = "Cannot create agent '" + getName() + "' IO-Error: " + ex.getMessage();
			ex.printStackTrace();
		} catch (ParseException e) {
			errorOutput = "Cannot create agent '" + getName() + "' parsing error occured: " + e.getMessage();
			e.printStackTrace();
		} finally {
			if(errorOutput != null) {
				throw new AgentInstantiationException(errorOutput);
			}
		}
	}
	
	/**
	 * Helper method: Creates (instantiates) the belief bases of the agent. The belief base config
	 * of the world belief base is given in the agents configuration. The agents configuration might
	 * also contain special belief base configs for the views of the agent onto other agents. If such
	 * a config is not given for one agent, then the view on this agent uses the same belief base config
	 * which is used for the world belief base of the viewed agent.
	 * @param ai
	 * @param config
	 * @throws AgentInstantiationException
	 */
	private void createBeliefbases(AgentInstance ai, SimulationConfiguration config) 
			throws AgentInstantiationException {
		// local variable used to save the output of exceptions...
		String errorOutput = null;		
		PluginInstantiator pi = PluginInstantiator.getInstance();
		BaseBeliefbase world = null;
		Map<String, BaseBeliefbase> views = null;
		try {
			world = pi.createBeliefbase(ai.getBeliefbaseConfig());
			views = new HashMap<String, BaseBeliefbase>();
			
			for(AgentInstance otherInstance : config.getAgents()) {
				// For all agents but myself:
				if(otherInstance == ai)
					continue;
				
				// create a view belief base, first try to get the type of the belief base from
				// the agent-instance part of the agent's configuration who is owner of the view.
				BeliefbaseConfig bbc = ai.getBeliefBaseConfig(otherInstance.getName());
				if(bbc == null) {
					// if this is not available use the same type as the world belief base of the
					// viewed agent.
					bbc = otherInstance.getBeliefbaseConfig();
				}
				BaseBeliefbase actView = pi.createBeliefbase(bbc);
				views.put(otherInstance.getName(), actView);	
			}
			setBeliefs(world, views);
		} catch (InstantiationException e) {
			errorOutput = "Cannot create agent '" + getName() + "' something went wrong during dynamic instantiation: " + e.getMessage();
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			errorOutput = "Cannot create agent '" + getName() + "' something went wrong during dynamic instantiation: " + e.getMessage();
			e.printStackTrace();
		} finally {
			if(errorOutput != null) {
				throw new AgentInstantiationException(errorOutput);
			}
		}
	}

	/**
	 * Helper method: Creates operators and components for the agent using the definitions
	 * in the given parameter.
	 * @param ai	AgentInstance data structure containing information about the operators /
	 * 				components used by the agent.
	 * @throws AgentInstantiationException
	 */
	private void createAgentComponents(AgentInstance ai) 
			throws AgentInstantiationException {
		AgentConfig ac = ai.getConfig();
		PluginInstantiator pi = PluginInstantiator.getInstance();
		try {
			for(String compName : ac.getComponents()) {
				AgentComponent comp = pi.createComponent(compName);
				comp.setParent(id);
				addComponent(comp);
				LOG.info("Add custom Component '{}' to agent '{}'", compName, getName());
			}
		} catch (InstantiationException e) {
			throw new AgentInstantiationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new AgentInstantiationException(e.getMessage());
		}
	}
	
	/**
	 * adds the given component to the agent.
	 * @param component	Reference to the component.
	 * @return	true if the component was successfully added, false if a component of the type already exists.
	 */
	public boolean addComponent(AgentComponent component) {
		if(component == null)
			throw new IllegalArgumentException();
		
		boolean reval = true;
		for(AgentComponent loopEa : customComponents) {
			if(component.getClass().equals(loopEa)) {
				reval = false;
				break;
			}
		}
		
		if(reval) {
			customComponents.add(component);
			component.setParent(id);
		}
		return reval;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AgentComponent> T getComponent(Class<? extends T> cls) {
		for(AgentComponent ea : customComponents) {
			if(ea.getClass().equals(cls))
				return (T)ea;
		}
		return null;
	}
	
	/** @return the last action performed by the agent */
	public Action getLastAction() {
		return lastAction;
	}
	
	/** @return an unmodifiable list of components of this agent */
	public List<AgentComponent> getComponents() {
		return Collections.unmodifiableList(customComponents);
	}
	
	public boolean addListener(AgentListener listener) {
		return listeners.add(listener);
	}
	
	public boolean removeListener(AgentListener listener) {
		return listeners.remove(listener);
	}
	
	public boolean addSubgoalListener(SubgoalListener listener) {
		return subgoalListeners.add(listener);
	}
	
	public boolean removeSubgoalListener(SubgoalListener listener) {
		return subgoalListeners.remove(listener);
	}
	
	public List<SubgoalListener> getSubgoalListeners() {
		return Collections.unmodifiableList(subgoalListeners);
	}
	
	public void onSubgoalFinished(Subgoal sg) {
		for(SubgoalListener sl : subgoalListeners) {
			sl.onSubgoalFinished(sg);
		}
	}
	
	@Override
	public boolean cycle(Object perception) {
		LOG.info("[" + this.getName() + "] Cylce starts: " + perception);
		
		regenContext();
		Context c = getContext();
		c.set("perception", perception);
		currentPerception = perception == null ? null : (Perception)perception;
		
		return asmlCylce.execute(c);
	}

	@Override
	public void shutdown() {
		// no cleanup work yet.
	}

	/** 
	 * informs all the agent listeners of the agent about the update beliefs process
	 * using the given perception
	 * @param perception
	 */
	public void onUpdateBeliefs(Perception perception, Beliefs oldBeliefs) {
		for(AgentListener al : listeners) {
			al.updateBeliefs(perception, oldBeliefs, beliefs);
		}
	}
	
	public Beliefs updateBeliefs(Perception perception) {
		return updateBeliefs(perception, beliefs);
	}
	
	/**
	 * updates the beliefs of the agent, this method searchs for the correct Update operator and calls its process method.
	 * @param perception	the perception causing the update
	 */
	public Beliefs updateBeliefs(Perception perception, Beliefs beliefs) {
		if(perception != null) {
			// save the perception for later use in messaging system.
			lastUpdateBeliefsPercept = perception;
			GenericOperatorParameter param = new GenericOperatorParameter(this);
			param.setParameter("beliefs", beliefs);
			param.setParameter("information", perception);
			BaseUpdateBeliefsOperator bubo = (BaseUpdateBeliefsOperator)operators.getPreferedByType(
					BaseUpdateBeliefsOperator.OPERATION_NAME);
			return bubo.process(param);
		}
		return beliefs;
	}
	
	/**
	 * Reasons the given query on the world knowledge using the default reasoning operator.
	 * @param query		Formula representing the question.
	 * @return			An Angerona Answer containing the result of the reasoning.
	 */
	public AngeronaAnswer reason(FolFormula query) {
		return beliefs.getWorldKnowledge().reason(query);
	}
	
	/** @return the actual belief base of the agent. */
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return a set of formulas representing the desires of the agent. */
	public Desires getDesires() {
		Desires desires = getComponent(Desires.class);
		if(desires == null) {
			LOG.warn("Tried to access the desires of agent '{}' which has no desire-component.", getName());
			return null;
		}
		return desires;
	}
	
	public PlanComponent getPlanComponent() {
		PlanComponent plan = getComponent(PlanComponent.class);
		if(plan == null) {
			LOG.warn("Tried to access the plan-component of agent '{}' which has no plan-component.", getName());
			return null;
		}
		return plan;
	}
	
	/** @return the perception the agent is working on. */
	public Perception getActualPerception() {
		return currentPerception;
	}

	/** @return Reference to the AgentProcess used for communicating with the environment. */
	public AngeronaAgentProcess getAgentProcess() {
		return agentProcess;
	}
	
	public Context getContext() {
		return context;
	}
	
	/**
	 * Helper method: Regenerates the context for the agent. The agent context
	 * is somewhat special because we don't use the beliefs structure but add
	 * world and confidential directly to the agent context. We also add a 
	 * view context which holds all the view belief bases of the agent.
	 */
	private void regenContext() {
		context = ContextFactory.createContext(this);
		context.set("operators", this.operators);
		context.set("plan", this.getComponent(PlanComponent.class));
		if(beliefs != null) {
			context.set("world", beliefs.getWorldKnowledge());
			
			Map<String, BaseBeliefbase> views = beliefs.getViewKnowledge();
			Context vc = new Context();
			context.attachContext("views", vc);
			for(String key : views.keySet()) {
				vc.set(key, views.get(key));
			}
		}
	}
	
	/** @return the environment of the simulation */
	public AngeronaEnvironment getEnvironment() {
		return agentProcess.getEnvironment();
	}
	
	public void performAction(Action act) {
		getAgentProcess().act(act);
		updateBeliefs(act);
		act.onSubgoalFinished(null);
		LOG.info("Action performed: " + act.toString());
		reporter.report("Action: '"+act.toString()+"' performed.");
		Angerona.getInstance().onActionPerformed(this, act);
		actionsHistory.add(act);
	}
	
	@Override 
	public void performAction(Action action, Agent ag, Beliefs beliefs) {
		// ignore beliefs parameter because we really perform the action:
		performAction(action);
	};
	
	/**
	 * Adds a desire to the set of the agents desires.
	 * @param desire	Reference to the desire to add
	 * @return			true if the desire was successfully added (not yet part of the agent
	 * 					desires), false otherwise.
	 */
	public boolean addDesire(Desire desire) {
		Desires desires = getDesires();
		if(desires != null) {
			return getDesires().add(desire);			
		}
		LOG.warn("Tried to add a desire to agent '{}' lacking the desire component.", getName());
		return false;
	}
	
	/**
	 * Adds the given tweety atom as desire
	 * @param desire
	 * @return	true if the desire is successfully added (not yet part of the agent
	 * 			desires), false otherwise.
	 */
	public boolean addDesire(Atom desire) {
		return addDesire(new Desire(desire));
	}
	
	/**
	 * Adds the given set of tweety atoms as desire to the desire component.
	 * @param set	A set of atoms which represent desires.
	 * @return 	true if the desires are successfully added (none of them are part of the agent
	 * 			desires yet), false otherwise.
	 */
	public boolean addDesires(Set<Atom> set) {
		for(Atom a : set) {
			if(!addDesire(a))
				return false;
		}
		return true;
	}
	
	/**
	 * Removes the given desire
	 * @param desire	The reference to the desire to remove.
	 * @return	true if the desire is removed successful
	 */
	public boolean removeDesire(Desire desire) {
		Desires desires = getDesires();
		if(desires != null) {
			return desires.remove(desire);
		}
		return false;
	}
	
	/**
	 * Removes the given desire
	 * @param desire	An atom representing the desire
	 * @return			true if the desire is removed successful.
	 */
	public boolean removeDesire(Atom desire) {
		return removeDesire(new Desire(desire));
	}
	
	@Override
	public Long getGUID() {
		return id;
	}

	@Override
	public Long getParent() {
		// at this moment agents are the highest instance in the hierarchy.
		return null;
	}

	@Override
	public List<Long> getChilds() {
		return childrenIds;
	}
	
	@Override
	public String toString() {
		return this.getName();
	}

	@Override
	public String getPosterName() {
		if(operatorStack.empty())
			return getName();
		else
			return operatorStack.peek().toString();
	}

	@Override
	public void pushOperator(BaseOperator op) {
		operatorStack.push(op);
		reporter.setDefaultPoster(op);
	}

	@Override
	public void popOperator() {
		operatorStack.pop();
		reporter.setDefaultPoster(operatorStack.isEmpty() ? this : operatorStack.peek());
	}
	
	@Override
	public Stack<BaseOperator> getOperatorStack() {
		return operatorStack;
	}

	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		// inform the other components about belief base changes:
		if(ev.getSource() instanceof BaseBeliefbase) {
			BaseBeliefbase bb = (BaseBeliefbase)ev.getSource();
			if(ev.getPropertyName().equals(BaseBeliefbase.BELIEFBASE_CHANGE_PROPERTY_NAME)) {
				onBeliefBaseChange(bb);	
			}
		}
	}
	
	private void onBeliefBaseChange(BaseBeliefbase bb) {
		// TODO: Document the flow of the messaging system for update-beliefs more technically.
		if(bb == beliefs.getWorldKnowledge()) {
			onBBChanged(bb, lastUpdateBeliefsPercept, AgentListener.WORLD);
		} else {
			for(String agName : beliefs.getViewKnowledge().keySet()) {
				BaseBeliefbase act = beliefs.getViewKnowledge().get(agName);
				if(act == bb) {
					onBBChanged(bb, lastUpdateBeliefsPercept, agName);
				}
			}
		}
	}
	
	private void onBBChanged(BaseBeliefbase bb, Perception percept, String space) {
		for(AgentListener l : listeners) {
			l.beliefbaseChanged(bb, percept, space);
		}
	}
	
	/**
	 * Helper method: reports the created belief bases and components to
	 * the report-system.
	 */
	protected void reportCreation() {
		report("Agent: '" + getName()+"' created.");
		
		report("Desires Set of '" + getName() + "' created.", this.getDesires());
		
		Beliefs b = getBeliefs();
		report("World Beliefbase of '" + this.getName()+"' created.", b.getWorldKnowledge() );
		
		Map<String, BaseBeliefbase> views = b.getViewKnowledge();
		for(String name : views.keySet()) {
			BaseBeliefbase actView = views.get(name);
			report("View->'" + name +"' Beliefbase of '" + getName() + "' created.", actView);
		}
		
		for(AgentComponent ac : getComponents()) {
			report("Custom component '" + ac.getClass().getSimpleName() + "' of '" + getName() + "' created.", ac);
		}
	}

	@Override
	public void report(String message) {
		reporter.report(message);
	}

	@Override
	public void report(String message, Entity attachment) {
		reporter.report(message, attachment);
	}

	@Override
	public Reporter getReporter() {
		return this;
	}

	@Override
	public OperatorStack getStack() {
		return this;
	}
}