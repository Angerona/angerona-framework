package angerona.fw.operators;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.Perception;
import angerona.fw.PlanElement;
import angerona.fw.error.NotImplementedException;
import angerona.fw.listener.ActionProcessor;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.operators.parameter.ViolatesParameter;

/**
 * Base class for violates tests. The base class implementation assumes that there only
 * violates operations for: perceptions/actions (Query, Answer), 
 * Skills (which are atomic intentions) and Plans (Subgoals) which are in fact a 
 * collection of Skills and other Subgoals.
 * Nevertheless by overriding processInt the subclass can suport more types for violation test.
 * But normally subclasses only implement their version of:
 * - onAction
 * - onSkill
 * - onPlan
 * 
 * @author Tim Janus
 */
public abstract class BaseViolatesOperator extends 
	Operator<ViolatesParameter, ViolatesResult> 
	implements ActionProcessor
	{

	@Override
	protected ViolatesResult processInt(ViolatesParameter param) {
		ViolatesResult reval = null;
		if(param.getAtom() instanceof Perception) {
			Perception p = (Perception)param.getAtom();
			reval = onPerception(p, param);
			p.setViolates(reval);
		} else if(param.getAtom() instanceof PlanElement) {
			reval = onPlan((PlanElement)param.getAtom(), param);
		}
		
		if(reval != null)
			return reval;
		throw new NotImplementedException("Violates is not implemnet for Action of type: " + param.getAtom().getClass().getSimpleName());
	}
	
	/**
	 * Is called by the processInt method when an perception like Query or Answer was given for violation checking.
	 * @param percept	Casted Reference to the perception
	 * @param param		The rest parameters for the violation invoking.
	 * @return			A ViolatesResult structure containing information about secrecy violation 
	 */
	protected abstract ViolatesResult onPerception(Perception percept, ViolatesParameter param);
	
	/**
	 * Is called by the processInt method when a Plan was given for violation checking..
	 * @param plan		Casted Reference to the plan element
	 * @param param		The rest parameters for the violation invoking.
	 * @return			A ViolatesResult structure containing information about secrecy violation 
	 */
	protected abstract ViolatesResult onPlan(PlanElement plan, ViolatesParameter param);
	
	/**
	 * Is called if the given agent wants to peform the given action in its mental state.
	 * Subclasses must implement this method to allow the Violates-Operator to be an
	 * Action-Processor for working through a plan for example.y
	 */
	public abstract void performAction(Action action, Agent agent, Beliefs beliefs);
}
