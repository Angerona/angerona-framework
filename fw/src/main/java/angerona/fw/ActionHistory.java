package angerona.fw;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import angerona.fw.logic.Beliefs;

/**
 * This component is responsible to save the actions that were
 * performed in the application and therefore provides a history
 * of actions.
 * 
 * @author Tim Janus
 */
public class ActionHistory extends BaseAgentComponent {
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
	
	public void putAction(String agent, Action action) {
		List<Action> temp = history.get(agent);
		if(temp == null) {
			temp = new LinkedList<Action>();
			history.put(agent, temp);
		}
		temp.add(action);
	}
	
	public boolean didAction(String agent, Action action) {
		List<Action> temp = history.get(agent);
		if(temp != null) {
			return temp.contains(action);
		}
		return false;
	}

	public List<Action> getActionsOf(String agent) {
		List<Action> inner = history.get(agent);
		return inner == null ? new LinkedList<Action>() : Collections.unmodifiableList(inner);
	}
	
	@Override
	public void updateBeliefs(Perception percept, Beliefs oldBeliefs, Beliefs newBeliefs) {
		if(percept instanceof Action) {
			Action act = (Action)percept;
			putAction(act.getSenderId(), act);
		}
	}
	
	@Override
	public ActionHistory clone() {
		return new ActionHistory(this);
	}
}
