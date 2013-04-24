package angerona.fw.logic;

import java.util.ArrayList;
import java.util.Collections;

import angerona.fw.BaseAgentComponent;
import angerona.fw.Intention;

public class ScriptingComponent extends BaseAgentComponent {
	/** list of all action */
	private ArrayList<Intention> actions = new ArrayList<Intention>();

	/** default ctor */
	public ScriptingComponent() {
	}

	/** copy ctor, copies the content of other but shares the id */
	public ScriptingComponent(ScriptingComponent other) {
		super(other);
		actions.addAll(other.actions);
	}

	/** adds the given action to the ScriptingComponent of the Agent */
	public boolean add(Intention action) {
		boolean reval = actions.add(action);
		if (reval) {
			report("New action: " + action.toString());
		}
		return reval;
	}

	public boolean remove(Intention action) {
		Intention toRemove = getIntention(action);
		if (toRemove != null) {
			actions.remove(toRemove);
			report("Removed action: " + action.toString());
			return true;
		}
		return false;
	}

	public Intention getIntention(Intention action) {
		for (Intention act : actions) {
			if (action.equals(act))
				return act;
		}
		return null;
	}

	public ArrayList<Intention> getIntentions() {
		return (ArrayList<Intention>) Collections.unmodifiableList(actions);
	}

	@Override
	public Object clone() {
		return new ScriptingComponent(this);
	}
}
