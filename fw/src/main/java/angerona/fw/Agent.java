package angerona.fw;
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

import angerona.fw.error.AgentIdException;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.internal.Entity;
import angerona.fw.internal.EntityAtomic;
import angerona.fw.internal.IdGenerator;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.listener.AgentListener;
import angerona.fw.listener.BeliefbaseChangeListener;
import angerona.fw.listener.SubgoalListener;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.Desires;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.OperatorVisitor;
import angerona.fw.operators.parameter.GenerateOptionsParameter;
import angerona.fw.operators.parameter.IntentionUpdateParameter;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;
import angerona.fw.operators.parameter.UpdateBeliefsParameter;
import angerona.fw.operators.parameter.ViolatesParameter;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextProvider;
import angerona.fw.report.ReportPoster;
import angerona.fw.serialize.AgentConfig;
import angerona.fw.serialize.SkillConfig;

/**
 * Implementation of an agent in the Angerona Framework.
 * An agent defines it functionality by the operator instances. The
 * data structure AgentConfiguration is used to dynamically instantiate
 * an agent.
 * The agent defines helper methods to use the operators of the agent.
 * @author Tim Janus
 */
public class Agent extends AgentArchitecture implements ContextProvider, Entity, OperatorVisitor, ReportPoster, BeliefbaseChangeListener {

	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(Agent.class);
	
	/** the beenuts agent process used for low level communication in the framework */
	private AngeronaAgentProcess agentProcess;
	
	/** The belief base of the agent */
	private Beliefs beliefs;
	
	/** id in the report-attachment hierarchy. */
	private Long id;
	
	private List<Long> childrenIds = new LinkedList<Long>();
	
	private List<AgentComponent> customComponents = new LinkedList<AgentComponent>();
	
	private List<SubgoalListener> subgoalListeners = new LinkedList<SubgoalListener>();
	
	private List<AgentListener> listeners = new LinkedList<AgentListener>();
	
	/** The context of the agents used for dynamic code defined in xml files (intentions) */
	private Context context;
	
	/** mapping atomic intentions names to the intention references defining the skills of the agent. */
	private Map<String, Skill> skills = new HashMap<String, Skill>();
	
	/** Reference to the used generate options operator. */
	BaseGenerateOptionsOperator generateOptionsOperator;
	
	/** Reference to the used filter operator. */
	BaseIntentionUpdateOperator intentionUpdateOperator;
	
	/** The operator used to change the knowledge base when receiving perceptions */
	BaseUpdateBeliefsOperator changeOperator;
	
	/** The operator used for violates proofs */
	BaseViolatesOperator violatesOperator;
	
	/** Reference to the used planer */
	BaseSubgoalGenerationOperator subgoalGenerationOperator;
	
	/** reference to the current used operator in the cycle process. */
	Stack<BaseOperator> operatorStack = new Stack<BaseOperator>();
	
	/** the perception received by the last or running cylce call */
	private Perception actualPerception;
	
	private Action lastAction;
	
	/**
	 * Ctor: Used for creating agents with an automatic assigned id.
	 * @param ac	Data structure containing configuration options for the agent behavior.
	 * @throws AgentIdException				Is thrown if automatic and manual id assignment are mixed.
	 * @throws AgentInstantiationException	Is thrown if the dynamic instantiation of a class fails.
	 */
	public Agent(AgentConfig ac, String name) throws AgentIdException, AgentInstantiationException {
		ctor(ac, name);
	}
	
	/**
	 * Sets the belief bases of the agent.
	 * @param world			The world knowledge of this agents
	 * @param views			A map of agent names to belief bases representing the views onto the knowledge of other agents 
	 * @param confidential	A knowledge base representing the confidential rules of this agent.
	 */
	public void setBeliefs(BaseBeliefbase world, Map<String, BaseBeliefbase> views) {
		if(beliefs != null) {
			childrenIds.remove(beliefs.getWorldKnowledge().getGUID());
			beliefs.getWorldKnowledge().removeListener(this);
			for(String name : beliefs.getViewKnowledge().keySet()) {
				BaseBeliefbase act = beliefs.getViewKnowledge().get(name);
				act.removeListener(this);
				childrenIds.remove(act.getGUID());
			}
		}
		beliefs = new Beliefs(world, views);
		childrenIds.add(world.getGUID());
		world.setParent(id);
		world.addListener(this);
		for(String name : views.keySet()) {
			BaseBeliefbase bb = views.get(name);
			childrenIds.add(bb.getGUID());
			bb.setParent(id);
			bb.addListener(this);
		}
		regenContext();
	}
	
	/**
	 * Helper method: is called by every ctor for constructing the object.
	 * @param ac	The configuration of the agent
	 * @throws AgentInstantiationException
	 */
	private void ctor(AgentConfig ac, String name) throws AgentInstantiationException {
		this.id = new Long(IdGenerator.generate(this));
		context = new Context();
		
		agentProcess = new AngeronaAgentProcess(name);
		agentProcess.setAgentArchitecture(this);
		init(agentProcess);
		
		PluginInstantiator pi = PluginInstantiator.getInstance();
		try {
			generateOptionsOperator = pi.createGenerateOptionsOperator(ac.getGenerateOptionsOperatorClass());
			intentionUpdateOperator = pi.createFilterOperator(ac.getIntentionUpdateOperatorClass());
			subgoalGenerationOperator = pi.createPlaner(ac.getSubgoalGenerationClass());
			changeOperator = pi.createUpdateOperator(ac.getUpdateOperatorClass());
			violatesOperator = pi.createViolatesOperator(ac.getViolatesOperatorClass());

			generateOptionsOperator.setOwner(this);
			intentionUpdateOperator.setOwner(this);
			subgoalGenerationOperator.setOwner(this);
			changeOperator.setOwner(this);
			violatesOperator.setOwner(this);
			
			for(String compName : ac.getComponents()) {
				AgentComponent comp = pi.createComponent(compName);
				addComponent(comp);
				LOG.info("Add custom Component '{}' to agent '{}'", compName, name);
			}
		} catch (InstantiationException e) {
			throw new AgentInstantiationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new AgentInstantiationException(e.getMessage());
		}
	}
	
	public void initComponents(Map<String, String> additionalData) {
		for(AgentComponent ac : customComponents) {
			ac.init(additionalData);
		}
	}
	
	public boolean addComponent(AgentComponent component) {
		if(component == null)
			throw new IllegalArgumentException();
		
		boolean reval = true;
		for(EntityAtomic loopEa : customComponents) {
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
		for(EntityAtomic ea : customComponents) {
			if(ea.getClass().equals(cls))
				return (T)ea;
		}
		return null;
	}
	
	public Action getLastAction() {
		return lastAction;
	}
	
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
	
	/**
	 * Adds all the skills saved in the skill configuration list
	 * @param lst	List with all skill configurations which should be loaded
	 * @throws AgentInstantiationException 
	 */
	public void addSkillsFromConfig(List<SkillConfig> lst) throws AgentInstantiationException {
		for(SkillConfig ic : lst) {
			addSkill(ic);
		}
	}

	/**
	 * Adds a skill to the Skill map of the Agent instance. A skill is an atomic action. The skill
	 * is described by a data-structure given per parameter.
	 * @param skillConfig	data-structure containing all informations about the skill.
	 * @throws AgentInstantiationException
	 */
	public void addSkill(SkillConfig skillConfig) throws AgentInstantiationException {
		Skill act = new Skill(this, skillConfig);
		if(skills.containsKey(skillConfig.getName())) {
			throw new AgentInstantiationException("Skill with name: '" + skillConfig.getName() + "' already exists. Names of atomic Intentions / Skills must be unique.");
		} else {
			skills.put(skillConfig.getName(), act);
		}
	}
	
	/**
	 * Gets the intention identified by the given name
	 * @param name	the name of the intention
	 * @return		reference to the intention.
	 */
	public Intention getSkill(String name) {
		return skills.get(name);
	}
	
	/**
	 * @return the skill map of the agent.
	 */
	public Map<String, Skill> getSkills() {
		return Collections.unmodifiableMap(skills);
	}
	
	@Override
	public boolean cycle(Object perception) {
		LOG.info("[" + this.getName() + "] Cylce starts: " + perception);
		
		Intention atomic = null;
		
		if(perception != null && ! (perception instanceof Perception)) {
			LOG.error("object must be of type Perception");
			actualPerception = null;
		} else if(perception instanceof Perception) {
			actualPerception = (Perception)perception;
		}
			
		updateBeliefs(actualPerception);	
		// Deliberation:
		Desires desires = getDesires();
		if(desires != null) {
			Set<Desire> options = generateOptionsOperator.process(new GenerateOptionsParameter(this, actualPerception, skills));
			for(Desire des : options) {
				addDesire(des);
			}
		}
		
		List<Skill> allSkills = new LinkedList<Skill>(skills.values());
		// Means-end-reasoning:
		MasterPlan masterPlan = getComponent(MasterPlan.class);
		if(masterPlan != null) {
			while(atomic == null) {
				atomic = intentionUpdateOperator.process(new IntentionUpdateParameter(masterPlan, allSkills, actualPerception));
				
				if(atomic == null) {
					if(!subgoalGenerationOperator.process(new SubgoalGenerationParameter(masterPlan, allSkills)))
						break;
				}
			}
		} else {
			LOG.error("Cannot perform Agent-cylce: agent missing Plan-Component");
		}
	
		if(atomic != null) {
			atomic.setRealRun(true);
			atomic.run();
		}
		
		LOG.info("[" + this.getName() + "] Cycle ends");
		return false;
	}

	@Override
	public void shutdown() {
		// no cleanup work yet.
	}

	/**
	 * updates the beliefs of the agent, this method searchs for the correct Update operator and calls its process method.
	 * @param perception	the perception causing the update
	 */
	public void updateBeliefs(Perception perception) {
		if(perception != null)
			beliefs = changeOperator.process(new UpdateBeliefsParameter(this, perception));
	}
	
	/**
	 * Performs a thought experiment what happens if the agent applies the following action. It uses the violates operator
	 * registered to the agent.
	 * @param beliefs	The beliefs which should be used for testing violation.
	 * @param action	The action which should be applied before testing for violation.
	 * @return			true if applying the action violates confidential, false otherwise.
	 */
	public boolean performThought(Beliefs beliefs, Action action) {
		return violatesOperator.process(new ViolatesParameter(this, action));
	}
	
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
	
	public MasterPlan getPlanComponent() {
		MasterPlan plan = getComponent(MasterPlan.class);
		if(plan == null) {
			LOG.warn("Tried to access the plan-component of agent '{}' which has no plan-component.", getName());
			return null;
		}
		return plan;
	}
	
	/** @return the perception the agent is working on. */
	public Perception getActualPerception() {
		return actualPerception;
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
	 * view context which holds all the view beliefbases of the agent.
	 */
	private void regenContext() {
		context = ContextFactory.createContext(this);
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
		LOG.info("Action performed: " + act.toString());
		Angerona.getInstance().report("Action: '"+act.toString()+"' performed.", getEnvironment(), this);
		Angerona.getInstance().onActionPerformed(this, act);
	}
	
	public boolean addDesire(Desire desire) {
		Desires desires = getDesires();
		if(desires != null) {
			return getDesires().add(desire);			
		}
		LOG.warn("Tried to add a desire to agent '{}' lacking the desire component.", getName());
		return false;
	}
	
	/**
	 * adds the given tweety atom as desire
	 * @param desire
	 * @return
	 */
	public boolean addDesire(Atom desire) {
		return addDesire(new Desire(desire));
	}
	
	/**
	 * adds the given set of tweety atoms as desire to the desire component.
	 * @param set
	 * @return
	 */
	public boolean addDesires(Set<Atom> set) {
		for(Atom a : set) {
			if(!addDesire(a))
				return false;
		}
		return true;
	}
	
	/**
	 * removes the given desire
	 * @param desire
	 * @return
	 */
	public boolean removeDesire(Desire desire) {
		Desires desires = getDesires();
		if(desires != null) {
			return desires.remove(desire);
		}
		return false;
	}
	
	/**
	 * 
	 * @param desire
	 * @return
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
	public AngeronaEnvironment getSimulation() {
		return getEnvironment();
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
	}

	@Override
	public void popOperator() {
		operatorStack.pop();
	}
	
	@Override
	public Stack<BaseOperator> getStack() {
		return operatorStack;
	}

	@Override
	public void changed(BaseBeliefbase bb) {
		if(bb == beliefs.getWorldKnowledge()) {
			onBBChanged(bb, AgentListener.WORLD);
		} else {
			for(String agName : beliefs.getViewKnowledge().keySet()) {
				BaseBeliefbase act = beliefs.getViewKnowledge().get(agName);
				if(act == bb) {
					onBBChanged(bb, agName);
				}
			}
		}
	}
	
	private void onBBChanged(BaseBeliefbase bb, String space) {
		for(AgentListener l : listeners) {
			l.beliefbaseChanged(bb, space);
		}
	}
}