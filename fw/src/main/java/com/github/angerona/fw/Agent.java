package com.github.angerona.fw;

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
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.asml.CommandSequence;
import com.github.angerona.fw.error.AgentInstantiationException;
import com.github.angerona.fw.internal.AngeronaReporter;
import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.internal.IdGenerator;
import com.github.angerona.fw.internal.PluginInstantiator;
import com.github.angerona.fw.listener.ActionProcessor;
import com.github.angerona.fw.listener.AgentListener;
import com.github.angerona.fw.listener.SubgoalListener;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.fw.operators.BaseUpdateBeliefsOperator;
import com.github.angerona.fw.operators.OperatorCallWrapper;
import com.github.angerona.fw.operators.OperatorCaller;
import com.github.angerona.fw.operators.OperatorProvider;
import com.github.angerona.fw.operators.OperatorStack;
import com.github.angerona.fw.operators.parameter.GenericOperatorParameter;
import com.github.angerona.fw.parser.ParseException;
import com.github.angerona.fw.reflection.Context;
import com.github.angerona.fw.reflection.ContextFactory;
import com.github.angerona.fw.reflection.ContextProvider;
import com.github.angerona.fw.report.ReportPoster;
import com.github.angerona.fw.report.Reporter;
import com.github.angerona.fw.serialize.AgentConfig;
import com.github.angerona.fw.serialize.AgentInstance;
import com.github.angerona.fw.serialize.BeliefbaseConfig;
import com.github.angerona.fw.serialize.OperationSetConfig;
import com.github.angerona.fw.serialize.SimulationConfiguration;

/**
 * An agent in the Angerona framework defines itself with the registered
 * instances of AgentComponent and it's used Operator instances. The data
 * structure AgentInstance is used to dynamically instantiate an agent. Every
 * Agent is an Entity in the Angerona Framework and provides its own id, its
 * parentId which is null for an agent and a list of child ids which identify
 * the AgentComponent instances.
 * 
 * An agent also implements the ActionProcessor interface. This implementation
 * updates the agent's own beliefs and sends the performed action to the
 * Environment.
 * 
 * Every agent implements the OperatorCaller interface and implements an
 * operator call stack as described in OperatorStack. The agent also implements
 * the Reporter interface to give its called Operator instances the ability to
 * use the Angerona report system. The call stack implementation also updates
 * the used ReportPoster of the Reporter to the currently active Operator.
 * 
 * @todo handle multiple perceptions
 * @author Tim Janus
 * @author Daniel Dilger
 */
public class Agent implements ContextProvider, Entity, OperatorStack,
		ReportPoster, Reporter, OperatorCaller, ActionProcessor,
		PropertyChangeListener, SubgoalListener {

	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(Agent.class);

	/** The belief base of the agent */
	protected Beliefs beliefs;

	/** id in the report-attachment hierarchy. */
	protected Long id;

	/** the name of the agent, has to be unique in the simulation scope */
	protected String name;

	/**
	 * this instance is responsible to communicate with the Angerona report
	 * system
	 */
	protected AngeronaReporter reporter;

	/**
	 * A list of children Ids which identify the AgentComponent instances linked
	 * to the agent
	 */
	protected List<Long> childrenIds = new LinkedList<Long>();

	protected List<SubgoalListener> subgoalListeners = new LinkedList<SubgoalListener>();

	protected List<AgentListener> listeners = new LinkedList<AgentListener>();

	/**
	 * The context of the agents used for dynamic code defined in xml files
	 * (intentions)
	 */
	protected Context context;

	/** a list of capabilities which describe actions the agent can perform */
	protected List<String> capabilities = new LinkedList<>();
	
	protected List<Perception> perceptions = new LinkedList<>();

	/**
	 * The OperatorProvider instance responsible to manage the different
	 * operators of the agent
	 */
	protected OperatorProvider operators = new OperatorProvider();

	/** The call stack of operators */
	protected Stack<BaseOperator> operatorStack = new Stack<BaseOperator>();

	/** the last perception used in the updateBeliefs Method */
	protected Perception lastUpdateBeliefsPercept;

	protected CommandSequence asmlCylce;
	
	protected AngeronaEnvironment env;

	public OperatorProvider getOperators() {
		return operators;
	}

	public Agent(String name, AngeronaEnvironment env) {
		this.name = name;
		this.env = env;
	}

	public List<String> getCapabilities() {
		return Collections.unmodifiableList(capabilities);
	}

	public boolean hasCapability(String capabilityName) {
		return capabilities.contains(capabilityName);
	}

	public void perceive(Perception percept) {
		perceptions.add(percept);
	}
	
	/**
	 * Sets the belief bases of the agent.
	 * 
	 * @param world
	 *            The world knowledge of this agents
	 * @param views
	 *            A map of agent names to belief bases representing the views
	 *            onto the knowledge of other agents
	 */
	public void setBeliefs(BaseBeliefbase world,
			Map<String, BaseBeliefbase> views) {
		// remove old belief entities from the agent
		if (beliefs != null) {
			childrenIds.remove(beliefs.getWorldKnowledge().getGUID());
			beliefs.getWorldKnowledge().removePropertyChangeListener(this);
			for (String name : beliefs.getViewKnowledge().keySet()) {
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
		for (Entry<String, BaseBeliefbase> ent : views.entrySet()) {
			BaseBeliefbase bb = ent.getValue();
			childrenIds.add(bb.getGUID());
			bb.setParent(id);
			bb.addPropertyChangeListener(this);
		}
		regenContext();
	}
	
	public AngeronaEnvironment getEnvironment() {
		return env;
	}

	/**
	 * Creates the belief bases, operators and components of the agent.
	 * 
	 * @param ai
	 *            The configuration of the agent instance.
	 * @param config
	 *            The configuration of the simulation.
	 * @throws AgentInstantiationException
	 */
	public void create(AgentInstance ai, SimulationConfiguration config)
			throws AgentInstantiationException {
		this.id = Long.valueOf(IdGenerator.generate(this));
		context = new Context();
		this.reporter = new AngeronaReporter(this, this);

		capabilities.addAll(ai.getCapabilities());

		// load cycle script and link supported operators
		asmlCylce = ai.getConfig().getCycleScript();
		for (OperationSetConfig osc : ai.getConfig().getOperations()) {
			try{
				operators.addOperationSet(osc);
			} catch (Exception e) {
				throw new AgentInstantiationException(
						"Cannot create operation-set of type '"
								+ osc.getOperationType() + "': " + e.getMessage());
			}
		}

		createBeliefbases(ai, config);
		createAgentComponents(ai);
		parseBeliefbases(ai, config.getFile().getParentFile());

		// add desire component if necessary.
		Desires desires = getComponent(Desires.class);
		if (desires == null && ai.getDesires().size() > 0) {
			LOG.warn(
					"No desire-component added to agent '{}' but desires, auto-add the desire component.",
					getName());
			desires = new Desires();
			beliefs.addComponent(desires);
		}

		// init the custom components
		for (AgentComponent ac : beliefs.getComponents()) {
			ac.init(ai.getAdditionalData());
			for(AgentComponent listener : beliefs.getComponents()) {
				if(listener != ac) {
					listener.componentInitialized(ac);
				}
			}
		}
	}

	/**
	 * Helper method: parses the belief base content for all the belief bases of
	 * the agent. That means one world belief base and one view belief base for
	 * every other agent is parsed.
	 * 
	 * @param ai
	 * @throws AgentInstantiationException
	 */
	protected void parseBeliefbases(AgentInstance ai, File workingDirectory)
			throws AgentInstantiationException {
		String errorOutput = null;

		String workingPath = workingDirectory.getAbsolutePath() + "/";
		
		try {
			// parse the content of the world belief base:
			BaseBeliefbase world = getBeliefs().getWorldKnowledge();
			File worldBBFile = new File(workingPath + ai.getBeliefbaseName() + "." + world.getFileEnding());
			if (worldBBFile.exists()) {
				world.parse(new BufferedReader(new FileReader(worldBBFile)));
			} else {
				LOG.warn("No world belief base file for '{}'.", ai.getName());
			}
			// parse the content of every view belief base:
			Map<String, BaseBeliefbase> views = getBeliefs().getViewKnowledge();
			for (Entry<String, BaseBeliefbase> ent : views.entrySet()) {
				BaseBeliefbase actView = ent.getValue();
				File viewFile = new File(workingPath
						+ ai.getBeliefbaseName() + "_" + ent.getKey() + "."
						+ actView.getFileEnding());
				if (viewFile.exists()) {
					actView.parse(new BufferedReader(new FileReader(viewFile)));
				} else {
					LOG.warn("No belief base file for view of '{}'->'{}'.",
							ai.getName(), ent.getKey());
				}
			}
		} catch (FileNotFoundException e) {
			errorOutput = "Cannot create agent '" + getName()
					+ "' file not found occured: " + e.getMessage();
			e.printStackTrace();
		} catch (IOException ex) {
			errorOutput = "Cannot create agent '" + getName() + "' IO-Error: "
					+ ex.getMessage();
			ex.printStackTrace();
		} catch (ParseException e) {
			errorOutput = "Cannot create agent '" + getName()
					+ "' parsing error occured: " + e.getMessage();
			e.printStackTrace();
		} finally {
			if (errorOutput != null) {
				throw new AgentInstantiationException(errorOutput);
			}
		}
	}

	/**
	 * Helper method: Creates (instantiates) the belief bases of the agent. The
	 * belief base config of the world belief base is given in the agents
	 * configuration. The agents configuration might also contain special belief
	 * base configs for the views of the agent onto other agents. If such a
	 * config is not given for one agent, then the view on this agent uses the
	 * same belief base config which is used for the world belief base of the
	 * viewed agent.
	 * 
	 * @param ai
	 * @param config
	 * @throws AgentInstantiationException
	 */
	protected void createBeliefbases(AgentInstance ai,
			SimulationConfiguration config) throws AgentInstantiationException {
		// local variable used to save the output of exceptions...
		String errorOutput = null;
		PluginInstantiator pi = PluginInstantiator.getInstance();
		BaseBeliefbase world = null;
		Map<String, BaseBeliefbase> views = null;
		try {
			world = pi.createBeliefbase(ai.getBeliefbaseConfig());
			views = new HashMap<String, BaseBeliefbase>();

			for (AgentInstance otherInstance : config.getAgents()) {
				// For all agents but myself:
				if (otherInstance == ai)
					continue;

				// create a view belief base, first try to get the type of the
				// belief base from
				// the agent-instance part of the agent's configuration who is
				// owner of the view.
				BeliefbaseConfig bbc = ai.getBeliefBaseConfig(otherInstance
						.getName());
				if (bbc == null) {
					// if this is not available use the same type as the world
					// belief base of the
					// viewed agent.
					bbc = otherInstance.getBeliefbaseConfig();
				}
				BaseBeliefbase actView = pi.createBeliefbase(bbc);
				views.put(otherInstance.getName(), actView);
			}
			setBeliefs(world, views);
		} catch (InstantiationException|IllegalAccessException|
				ClassNotFoundException|ParseException e) {
			errorOutput = "Cannot create agent '" + getName()
					+ "' something went wrong during dynamic instantiation: "
					+ e.getMessage();
		} finally {
			if (errorOutput != null) {
				throw new AgentInstantiationException(errorOutput);
			}
		}
	}

	/**
	 * Helper method: Creates operators and components for the agent using the
	 * definitions in the given parameter.
	 * 
	 * @param ai
	 *            AgentInstance data structure containing information about the
	 *            operators / components used by the agent.
	 * @throws AgentInstantiationException
	 */
	protected void createAgentComponents(AgentInstance ai)
			throws AgentInstantiationException {
		AgentConfig ac = ai.getConfig();
		PluginInstantiator pi = PluginInstantiator.getInstance();
		try {
			for (String compName : ac.getComponents()) {
				AgentComponent comp = pi.createComponent(compName);
				comp.setParent(id);
				beliefs.addComponent(comp);
				LOG.info("Add custom Component '{}' to agent '{}'", compName,
						getName());
			}
		} catch (InstantiationException e) {
			throw new AgentInstantiationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new AgentInstantiationException(e.getMessage());
		}
	}

	
	@SuppressWarnings("unchecked")
	public <T extends AgentComponent> T getComponent(Class<? extends T> cls) {
		for (AgentComponent ea : beliefs.getComponents()) {
			if (ea.getClass().equals(cls))
				return (T) ea;
		}
		return null;
	}

	/** @return an unmodifiable list of components of this agent */
	public Set<AgentComponent> getComponents() {
		return Collections.unmodifiableSet(beliefs.getComponents());
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

	@Override
	public void onSubgoalFinished(Intention sg) {
		for (SubgoalListener sl : subgoalListeners) {
			sl.onSubgoalFinished(sg);
		}
	}

	public boolean cycle() {
		Perception percept = perceptions.isEmpty() ? null : perceptions.get(0);
		perceptions.clear();
		LOG.info("[" + this.getName() + "] Cylce starts: " + percept);

		regenContext();
		Context c = getContext();
		c.set("perception", percept);
		
		return asmlCylce.execute(c);
	}
	
	public boolean hasPerceptions() {
		return !perceptions.isEmpty();
	}

	/**
	 * Informs all the agent listeners of the agent about the update beliefs
	 * process using the given perception and the given old beliefs.
	 * 
	 * @param perception
	 *            The perception responsible for the update beliefs process
	 * @param oldBeliefs
	 *            A reference to the beliefs of the agent before the update
	 *            beliefs process was invoked.
	 */
	public void onUpdateBeliefs(Perception perception, Beliefs oldBeliefs) {
		for (AgentListener al : listeners) {
			al.updateBeliefs(perception, oldBeliefs, beliefs);
		}
	}

	public Beliefs updateBeliefs(Perception perception) {
		return updateBeliefs(perception, beliefs);
	}

	/**
	 * Updates the beliefs of the agent. This method searches for the correct
	 * Update operator and calls its process method.
	 * 
	 * @param perception
	 *            The perception causing the update.
	 * @param beliefs
	 *            The Beliefs used as basis for the update process.
	 * @return The updated version of the beliefs.
	 */
	public Beliefs updateBeliefs(Perception perception, Beliefs beliefs) {
		if (perception != null) {
			// save the perception for later use in messaging system.
			lastUpdateBeliefsPercept = perception;
			GenericOperatorParameter param = new GenericOperatorParameter(this);
			param.setParameter("beliefs", beliefs);
			param.setParameter("information", perception);
			OperatorCallWrapper bubo = operators.getPreferedByType(BaseUpdateBeliefsOperator.OPERATION_NAME);
			return (Beliefs) bubo.process(param);
		}
		return beliefs;
	}

	/**
	 * Reasons the given query on the world knowledge using the default
	 * reasoning operator.
	 * 
	 * @param query
	 *            Formula representing the question.
	 * @return An Angerona Answer containing the result of the reasoning.
	 */
	public AngeronaAnswer reason(FolFormula query) {
		return beliefs.getWorldKnowledge().reason(query);
	}

	/** @return the actual belief base of the agent. */
	public Beliefs getBeliefs() {
		return beliefs;
	}

	public PlanComponent getPlanComponent() {
		PlanComponent plan = getComponent(PlanComponent.class);
		if (plan == null) {
			LOG.warn(
					"Tried to access the plan-component of agent '{}' which has no plan-component.",
					getName());
			return null;
		}
		return plan;
	}

	public Context getContext() {
		return context;
	}

	/**
	 * Helper method: Regenerates the context for the agent. The agent context
	 * is somewhat special because we don't use the beliefs structure but add
	 * world and confidential directly to the agent context. We also add a view
	 * context which holds all the view belief bases of the agent.
	 */
	protected void regenContext() {
		context = ContextFactory.createContext(this);
		context.set("operators", this.operators);
		context.set("plan", this.getComponent(PlanComponent.class));
		if (beliefs != null) {
			context.set("world", beliefs.getWorldKnowledge());

			Map<String, BaseBeliefbase> views = beliefs.getViewKnowledge();
			Context vc = new Context();
			context.attachContext("views", vc);
			for (Entry<String, BaseBeliefbase> ent : views.entrySet()) {
				vc.set(ent.getKey(), ent.getValue());
			}
		}
	}

	public void performAction(Action act) {
		updateBeliefs(act);
		act.onSubgoalFinished(null);
		env.sendAction(act.getReceiverId(), act);
		LOG.info("Action performed: " + act.toString());
		reporter.report("Action: '" + act.toString() + "' performed.");
		Angerona.getInstance().onActionPerformed(this, act);
	}

	@Override
	public void performAction(Action action, Agent ag, Beliefs beliefs) {
		// ignore beliefs parameter because we really perform the action:
		performAction(action);
	};

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
		if (operatorStack.empty())
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
		reporter.setDefaultPoster(operatorStack.isEmpty() ? this
				: operatorStack.peek());
	}

	@Override
	public Stack<BaseOperator> getOperatorStack() {
		return operatorStack;
	}

	@Override
	public void propertyChange(PropertyChangeEvent ev) {
		// inform the other components about belief base changes:
		if (ev.getSource() instanceof BaseBeliefbase) {
			BaseBeliefbase bb = (BaseBeliefbase) ev.getSource();
			if (ev.getPropertyName().equals(
					BaseBeliefbase.BELIEFBASE_CHANGE_PROPERTY_NAME)) {
				onBeliefBaseChange(bb);
			}
		}
	}

	protected void onBeliefBaseChange(BaseBeliefbase bb) {
		// TODO: Document the flow of the messaging system for update-beliefs
		// more technically.
		if (bb == beliefs.getWorldKnowledge()) {
			onBBChanged(bb, lastUpdateBeliefsPercept, AgentListener.WORLD);
		} else {
			for (String agName : beliefs.getViewKnowledge().keySet()) {
				BaseBeliefbase act = beliefs.getViewKnowledge().get(agName);
				if (act == bb) {
					onBBChanged(bb, lastUpdateBeliefsPercept, agName);
				}
			}
		}
	}

	protected void onBBChanged(BaseBeliefbase bb, Perception percept, String space) {
		for (AgentListener l : listeners) {
			l.beliefbaseChanged(bb, percept, space);
		}
	}

	/**
	 * Helper method: reports the created belief bases and components to the
	 * report-system.
	 */
	protected void reportCreation() {
		report("Agent: '" + getName() + "' created.");

		Beliefs b = getBeliefs();
		report("World Beliefbase of '" + this.getName() + "' created.",
				b.getWorldKnowledge());

		Map<String, BaseBeliefbase> views = b.getViewKnowledge();
		for (Entry<String, BaseBeliefbase> ent : views.entrySet()) {
			BaseBeliefbase actView = ent.getValue();
			report("View->'" + name + "' Beliefbase of '" + ent.getKey()
					+ "' created.", actView);
		}

		for (AgentComponent ac : getComponents()) {
			report("Custom component '" + ac.getClass().getSimpleName()
					+ "' of '" + getName() + "' created.", ac);
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

	public String getName() {
		return name;
	}
}