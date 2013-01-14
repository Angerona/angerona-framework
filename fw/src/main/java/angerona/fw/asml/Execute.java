package angerona.fw.asml;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import angerona.fw.Agent;
import angerona.fw.PlanElement;
import angerona.fw.error.InvokeException;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.Value;

/**
 * The ASML execute command is used to let an agent perform an action. It
 * is a application dependent command and needs the scope to be at the agent
 * level.
 * @author Tim Janus
 */
@Root(name="execute")
public class Execute extends ASMLCommand {

	/** the script value representing the action as a plan element which the agent shall perform */
	@Attribute(name="action", required=true)
	Value action;
	
	/**
	 * Checks if the scope is at agent level. If this is not the case an error is
	 * thrown. Otherwise the plan element is executed.
	 */
	@Override
	protected void executeInternal() throws InvokeException {
		// Test scope:
		Agent ag = null;
		try {
			ag = getParameter("self");
		} catch(InvokeException e) {
			throw InvokeException.scopeError("execute is called in the wrong scope: " +
					"Agent scope needed - inner Error: " + e.getMessage(), getContext());
		}
		
		// receive the action in the plan element and execute it.
		PlanElement element = (PlanElement)action.getValue();
		if(element != null) {
			// TODO: Hack (Use a more general solution for the agent as data holder)
			ag.setViolatesResult(element.violates());
			element.prepare(ag, ag.getBeliefs());
			element.run();
		}
	}
	
	/**
	 * Sets the context the ASML execute command and its value which
	 * represents the action which shall be executed.
	 */
	@Override
	public void setContext(Context context) {
		super.setContext(context);
		action.setContext(context);
	}
}
