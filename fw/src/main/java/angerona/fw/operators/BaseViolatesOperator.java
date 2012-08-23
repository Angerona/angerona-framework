package angerona.fw.operators;

import angerona.fw.Action;
import angerona.fw.Skill;
import angerona.fw.Subgoal;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.ViolatesResult;
import angerona.fw.operators.parameter.ViolatesParameter;

/**
 * Base class for violates tests. The base class implementation assumes that there only
 * violates operations for: atomic actions (Query, Answer), Skills (which are atomic intentions) and
 * Plans (Subgoals) which are in fact a collection of Skills and other Subgoals.
 * Nevertheless by overriding processInt the subclass can suport more types for violation test.
 * But normally subclasses only implement their version of:
 * - onAction
 * - onSkill
 * - onPlan
 * 
 * @author Tim Janus
 */
public abstract class BaseViolatesOperator extends 
	Operator<ViolatesParameter, ViolatesResult> {
	
	@Override
	protected ViolatesResult processInt(ViolatesParameter param) {
		if(param.getAction() instanceof Action) {
			return onAction((Action)param.getAction(), param);
		} else if(param.getAction() instanceof Skill) {
			return onSkill((Skill)param.getAction(), param);
		} else if(param.getAction() instanceof Subgoal) {
			return onPlan((Subgoal)param.getAction(), param);
		}
		
		throw new NotImplementedException("Violates is not implemnet for Action of type: " + param.getAction().getClass().getSimpleName());
	}
	
	/**
	 * Is called by the processInt method when an action like Query or Answer was given for violation checking.
	 * @param action	Casted Reference to the action
	 * @param param		The rest parameters for the violation invoking.
	 * @return			A ViolatesResult structure containing information about secrecy violation 
	 */
	protected abstract ViolatesResult onAction(Action action, ViolatesParameter param);
	
	/**
	 * Is called by the processInt method when an Skill was given for violation checking.
	 * @param action	Casted Reference to the action
	 * @param param		The rest parameters for the violation invoking.
	 * @return			A ViolatesResult structure containing information about secrecy violation 
	 */
	protected abstract ViolatesResult onSkill(Skill skill, ViolatesParameter param);
	
	/**
	 * Is called by the processInt method when a Plan was given for violation checking..
	 * @param action	Casted Reference to the action
	 * @param param		The rest parameters for the violation invoking.
	 * @return			A ViolatesResult structure containing information about secrecy violation 
	 */
	protected abstract ViolatesResult onPlan(Subgoal plan, ViolatesParameter param);
}
