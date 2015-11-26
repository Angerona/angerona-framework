package com.github.kreaturesfw.core.legacy;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.github.kreaturesfw.core.logic.Beliefs;

/**
 * This component is responsible to save the actions that were
 * performed in the application and therefore provides a history
 * of actions.
 * 
 * The default implementation forbids to repeat an action. Sub-classes
 * might implement versions of an Action-History that allows that an
 * action can be performed multiple times by an agent.
 * 
 * In the communication scenario an agent might answer unknown or reject
 * multiple times to a query.
 * 
 * @author Tim Janus
 */
public class ActionHistory extends BaseAgentComponent {
	/** 
	 * map saving the action history by storing one list of
	 * actions for every agent.
	 */
	private Map<String, List<Action>> history = new HashMap<>();
	
	/** Default Ctor: Used for dynamic creation, creates empty history */
	public ActionHistory() {}
	
	/** Copy Ctor */
	public ActionHistory(ActionHistory other) {
		super(other);
		for(Entry<String, List<Action>> entry : other.history.entrySet()) {
			history.put(entry.getKey(), new LinkedList<Action>(entry.getValue()));
		}
	}
	
	/**
	 * Puts the given action to list of historical action of the given
	 * agent.
	 * @param action	The action that was performed by the agent
	 */
	public void putAction(Action action) {
		List<Action> temp = history.get(action.getSenderId());
		if(temp == null) {
			temp = new LinkedList<Action>();
			history.put(action.getSenderId(), temp);
		}
		temp.add(action);
	}
	
	/**
	 * Checks if the specified agent did the given action.
	 * @param action	The action that might be performed by an agent
	 * @return			True if the action was performed by the agent, 
	 * 					false otherwise
	 */
	public boolean didAction(Action action) {
		List<Action> temp = history.get(action.getSenderId());
		if(temp != null) {
			return temp.contains(action);
		}
		return false;
	}

	/**
	 * Process the list of all actions that the given agent has performed
	 * @param agent	The name of the agent
	 * @return	An unmodifiable list that contains all the actions performed 
	 * 			by the given agent
	 */
	public List<Action> getActionsOf(String agent) {
		List<Action> inner = history.get(agent);
		return inner == null ? new LinkedList<Action>() : Collections.unmodifiableList(inner);
	}
	
	/**
	 * If the perception is an action of another agent, the action is saved in the
	 * history.
	 * @param	percept	The perception that caused the updateBeliefs
	 * @param	oldBeliefs	The beliefs of the agent before the updateBeliefs processing
	 * @param	newBeliefs	The beliefs of the agent after the updateBeliefs processing
	 */
	@Override
	public void updateBeliefs(Perception percept, Beliefs oldBeliefs, Beliefs newBeliefs) {
		if(percept instanceof Action) {
			Action act = (Action)percept;
			putAction(act);
		}
	}
	
	@Override
	public ActionHistory clone() {
		return new ActionHistory(this);
	}
}
