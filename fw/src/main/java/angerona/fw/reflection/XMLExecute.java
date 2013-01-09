package angerona.fw.reflection;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import angerona.fw.Agent;
import angerona.fw.PlanElement;
import angerona.fw.error.InvokeException;

@Root(name="execute")
public class XMLExecute extends XMLCommando {

	@Element(name="action", required=true)
	Value action;
	
	@Override
	protected void executeInternal() throws InvokeException {
		Agent ag = null;
		try {
			ag = getParameter("self");
		} catch(InvokeException e) {
			throw InvokeException.scopeError("execute is called in the wrong scope: " +
					"Agent scope needed - inner Error: " + e.getMessage(), getContext());
		}
		PlanElement element = (PlanElement)action.getValue();
		if(element != null) {
			ag.setViolatesResult(element.violates());
			element.prepare(ag, ag.getBeliefs());
			element.run();
		}
	}
	
	@Override
	public void setContext(Context context) {
		super.setContext(context);
		action.setContext(context);
	}
}
