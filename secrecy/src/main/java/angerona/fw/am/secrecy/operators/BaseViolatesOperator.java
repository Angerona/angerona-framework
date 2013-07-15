package angerona.fw.am.secrecy.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;
import angerona.fw.Cache;
import angerona.fw.Perception;
import angerona.fw.PlanElement;
import angerona.fw.error.NotImplementedException;
import angerona.fw.listener.ActionProcessor;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.operators.Operator;
import angerona.fw.operators.parameter.EvaluateParameter;
import angerona.fw.util.Pair;

/**
 * Base class for violates tests. The base class implementation assumes that there only
 * violates operations for: perceptions/actions (Query, Answer), 
 * actions (which are atomic intentions) and Plans (Subgoals) which are in fact a 
 * collection of actions and other Subgoals.
 * Nevertheless by overriding processInt the subclass can suport more types for violation test.
 * But normally subclasses only implement their version of:
 * - onPerception
 * - onPlan
 * 
 * @author Tim Janus
 */
public abstract class BaseViolatesOperator 
	extends Operator<Agent, EvaluateParameter, ViolatesResult> 
	implements ActionProcessor
	{

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(BaseViolatesOperator.class);
	
	/** the unique operation name of the violates operator */
	public static final String OPERATION_NAME = "Violates";
	
	/** the cache used by the Violates operator */
	private static Cache<EvaluateParameter, ViolatesResult> cache = new Cache<>();
	
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_NAME,
				BaseViolatesOperator.class);
	}
	
	@Override 
	protected EvaluateParameter getEmptyParameter() {
		return new EvaluateParameter();
	}
	
	@Override
	protected ViolatesResult defaultReturnValue() {
		return new ViolatesResult(true);
	}
	
	protected ViolatesResult processInternal(EvaluateParameter param) {
		ViolatesResult reval = null;
		
		/* try to find calculation in cache:
		 * (not working cause of missing hashCode and equals 
		 * methods in some classes
		reval = cache.getCacheValue(param);
		if(reval != null) {
			LOG.debug("Using Cache for: '{}'", param);
			return reval;
		}
		*/
		
		// otherwise calculate the violates result:
		AngeronaAtom atom = param.getAtom();
		if(atom instanceof Perception) {
			Perception p = (Perception)atom;
			reval = onPerception(p, param);
		} else if(atom instanceof PlanElement) {
			reval = onPlan((PlanElement)atom, param);
		} else if(atom == null) {
			reval = onCheck(param.getAgent(), param.getBeliefs());
		}
		
		// return it and save it for later use in a cache:
		if(reval != null) {
			cache.setCacheValue(param, reval);
			return reval;
		}
		throw new NotImplementedException("Violates is not implemeted for Action of type: " + atom.getClass().getSimpleName());
	}
	
	/**
	 * Is called by the processInt method when an perception like Query or Answer was given for violation checking.
	 * @param percept	Casted Reference to the perception
	 * @param param		The rest parameters for the violation invoking.
	 * @return			A ViolatesResult structure containing information about secrecy violation 
	 */
	protected abstract ViolatesResult onPerception(Perception percept, EvaluateParameter param);
	
	/**
	 * Is called by the processInt method when a Plan was given for violation checking.
	 * @param plan		Casted Reference to the plan element
	 * @param param		The rest parameters for the violation invoking.
	 * @return			A ViolatesResult structure containing information about secrecy violation 
	 */
	protected abstract ViolatesResult onPlan(PlanElement plan, EvaluateParameter param);
	
	/**
	 * TODO
	 * @param agent
	 * @param beliefs
	 * @return
	 */
	protected abstract ViolatesResult onCheck(Agent agent, Beliefs beliefs);
	
	/**
	 * Is called if the given agent wants to perform the given action in its mental state.
	 * Subclasses must implement this method to allow the Violates-Operator to be an
	 * ActionProcessor for mentally working through a plan for example.
	 * @param action	The action which shall be performed.
	 * @param agent		The agent performing the action.
	 * @param beliefs	The beliefs used as basis for performing the action.
	 */
	public abstract void performAction(Action action, Agent agent, Beliefs beliefs);
}
