package angerona.fw;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.beenuts.ap.AgentArchitecture;
import net.sf.tweety.Formula;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.error.AgentIdException;
import angerona.fw.error.AgentInstantiationException;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.ConfidentialKnowledge;
import angerona.fw.logic.Desires;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.Beliefs;
import angerona.fw.operators.BaseChangeOperator;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BasePolicyControlOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.parameter.GenerateOptionsParameter;
import angerona.fw.operators.parameter.IntentionUpdateParameter;
import angerona.fw.operators.parameter.PolicyControlParameter;
import angerona.fw.operators.parameter.SubgoalGenerationParameter;
import angerona.fw.operators.parameter.UpdateParameter;
import angerona.fw.operators.parameter.ViolatesParameter;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.ContextFactory;
import angerona.fw.reflection.ContextProvider;
import angerona.fw.report.Entity;
import angerona.fw.serialize.AgentConfiguration;
import angerona.fw.serialize.SkillConfiguration;

/**
 * Implementation of an agent in the Angerona Framework.
 * An agent defines it functionality by the operator instances. The
 * data structure AgentConfiguration is used to dynamically instantiate
 * an agent.
 * The agent defines helper methods to use the operators of the agent.
 * @author Tim Janus
 */
public class Agent extends AgentArchitecture implements ContextProvider, Entity {

	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(Agent.class);
	
	/** the beenuts agent process used for low level communication in the framework */
	private AngeronaAgentProcess agentProcess;
	
	/** The desires of the agent */
	private Desires desires;
	
	/** The belief base of the agent */
	private Beliefs beliefs;
	
	/** id in the report-attachment hierarchy. */
	private Long id;
	
	private List<Long> childrenIds = new LinkedList<Long>();
	
	/** The context of the agents used for dynamic code defined in xml files (intentions) */
	private Context context;
	
	/** mapping atomic intentions names to the intention references defining the skills of the agent. */
	private Map<String, Skill> skills = new HashMap<String, Skill>();
	
	/** reference to the actual goal of the agent */
	private MasterPlan masterPlan;
	
	/** Reference to the used generate options operator. */
	BaseGenerateOptionsOperator generateOptionsOperator;
	
	/** Reference to the used filter operator. */
	BaseIntentionUpdateOperator intentionUpdateOperator;
	
	/** The operator used to change the knowledge base when receiving perceptions */
	BaseChangeOperator changeOperator;
	
	/** The operator used for policy control */
	BasePolicyControlOperator policyControlOperator;
	
	/** The operator used for violates proofs */
	BaseViolatesOperator violatesOperator;
	
	/** Reference to the used planer */
	BaseSubgoalGenerationOperator subgoalGenerationOperator;
	
	/** the perception received by the last or running cylce call */
	private Perception actualPerception;
	
	/**
	 * Ctor: Used for creating agents with an automatic assigned id.
	 * @param ac	Data structure containing configuration options for the agent behavior.
	 * @throws AgentIdException				Is thrown if automatic and manual id assignment are mixed.
	 * @throws AgentInstantiationException	Is thrown if the dynamic instantiation of a class fails.
	 */
	public Agent(AgentConfiguration ac, String name) throws AgentIdException, AgentInstantiationException {
		ctor(ac, name);
	}
	
	/**
	 * Sets the belief bases of the agent.
	 * @param world			The world knowledge of this agents
	 * @param views			A map of agent names to belief bases representing the views onto the knowledge of other agents 
	 * @param confidential	A knowledge base representing the confidential rules of this agent.
	 */
	public void setBeliefs(BaseBeliefbase world, Map<String, BaseBeliefbase> views, ConfidentialKnowledge confidential) {
		if(beliefs != null) {
			childrenIds.remove(beliefs.getWorldKnowledge().getGUID());
			childrenIds.remove(beliefs.getConfidentialKnowledge().getGUID());
			for(String name : beliefs.getViewKnowledge().keySet()) {
				BaseBeliefbase act = beliefs.getViewKnowledge().get(name);
				childrenIds.remove(act.getGUID());
			}
		}
		beliefs = new Beliefs(world, views, confidential);
		childrenIds.add(world.getGUID());
		childrenIds.add(confidential.getGUID());
		world.setParent(id);
		confidential.setParent(id);
		for(String name : views.keySet()) {
			BaseBeliefbase bb = views.get(name);
			childrenIds.add(bb.getGUID());
			bb.setParent(id);
		}
		regenContext();
	}
	
	/**
	 * Helper method: is called by every ctor for constructing the object.
	 * @param ac	The configuration of the agent
	 * @throws AgentInstantiationException
	 */
	private void ctor(AgentConfiguration ac, String name) throws AgentInstantiationException {
		this.id = new Long(IdGenerator.generate(this));
		context = new Context();
		
		desires = new Desires(this.id);
		masterPlan = new MasterPlan(this);
		
		agentProcess = new AngeronaAgentProcess(name);
		agentProcess.setAgentArchitecture(this);
		init(agentProcess);
		
		PluginInstantiator pi = PluginInstantiator.getInstance();
		try {
			generateOptionsOperator = pi.createGenerateOptionsOperator(ac.getGenerateOptionsOperatorClass());
			intentionUpdateOperator = pi.createFilterOperator(ac.getFilterOperatorClass());
			subgoalGenerationOperator = pi.createPlaner(ac.getPlanerClass());
			changeOperator = pi.createUpdateOperator(ac.getUpdateOperatorClass());
			policyControlOperator = pi.createPolicyControlOperator(ac.getPolicyControlOperatorClass());
			violatesOperator = pi.createViolatesOperator(ac.getViolatesOperatorClass());
		} catch (InstantiationException e) {
			throw new AgentInstantiationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new AgentInstantiationException(e.getMessage());
		}
	}
	
	/**
	 * Adds all the skills saved in the skill configuration list
	 * @param lst	List with all skill configurations which should be loaded
	 * @throws AgentInstantiationException 
	 */
	public void addSkillsFromConfig(List<SkillConfiguration> lst) throws AgentInstantiationException {
		for(SkillConfiguration ic : lst) {
			addSkill(ic);
		}
	}

	/**
	 * Adds a skill to the Skill map of the Agent instance. A skill is an atomic action. The skill
	 * is described by a data-structure given per parameter.
	 * @param skillConfig	data-structure containing all informations about the skill.
	 * @throws AgentInstantiationException
	 */
	public void addSkill(SkillConfiguration skillConfig) throws AgentInstantiationException {
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
		Set<Formula> options = generateOptionsOperator.process(new GenerateOptionsParameter(this, actualPerception, skills));
		if(!desires.equals(options)) {
			desires.clear();
			desires.addAll(options);
			Angerona.getInstance().report("Desires of Agent '" + getName() + "' updated.", generateOptionsOperator, desires);
		}
		List<Skill> allSkills = new LinkedList<Skill>(skills.values());
		
		// Means-end-reasoning:
		while(atomic == null) {
			atomic = intentionUpdateOperator.process(new IntentionUpdateParameter(masterPlan, allSkills, actualPerception));
			
			if(atomic == null) {
				if(!subgoalGenerationOperator.process(new SubgoalGenerationParameter(masterPlan, allSkills)))
					break;
			}
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
			beliefs = changeOperator.process(new UpdateParameter(this, perception));
	}
	
	/**
	 * Performs the policy control operator.
	 * @param subjectId		The unique name of the subject.
	 * @param answer		The true answer (no policy control yet)
	 * @param question		formula representing the question.
	 * @return	another answer then trueAnswer if policy would be destroyed by trueAnswer, otherwise trueAnswer.
	 */
	public AngeronaAnswer performPolicyControl(String subjectId, AngeronaAnswer answer, Formula question) {
		return policyControlOperator.process(new PolicyControlParameter(this, subjectId, answer, question));
	}
	
	/**
	 * Performs the violates operator.
	 * @param beliefs	The beliefs which should be used for testing violation.
	 * @param action	The action which should be applied before testing for violation.
	 * @return			true if applying the action violates confidential, false otherwise.
	 */
	public boolean performViolates(Beliefs beliefs, Action action) {
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
		return desires;
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
			context.set("confidential", beliefs.getConfidentialKnowledge());
		
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
	}
	
	public boolean addDesire(Formula desire) {
		boolean reval = this.desires.add(desire);
		if(reval) {
			Angerona.getInstance().report("Desires changed.", this.getEnvironment(), this.desires);
		}
		return reval;
	}
	
	public boolean addDesires(List<FolFormula> list) {
		boolean reval = list.addAll(list);
		if(reval) {
			Angerona.getInstance().report("Desires changed.", this.getEnvironment(), this.desires);
		}
		return reval;
	}
	
	public boolean removeDesire(Formula desire) {
		boolean reval = this.desires.remove(desire);
		if(reval) {
			Angerona.getInstance().report("Desires changed.", this.getEnvironment(), this.desires);
		}
		return reval;
	}

	public MasterPlan getPlan() {
		return masterPlan;
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
}
