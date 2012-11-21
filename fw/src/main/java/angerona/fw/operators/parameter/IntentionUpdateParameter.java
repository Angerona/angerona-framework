package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.Action;
import angerona.fw.PlanComponent;

public class IntentionUpdateParameter extends GenericOperatorParameter {
	protected List<Action> forbiddenActions;
	
	protected PlanComponent plan;
	
	public IntentionUpdateParameter(PlanComponent plan, List<Action> forbidden) {
		super(plan.getAgent());
		this.plan= plan;
	}
	
	public List<Action> getForbiddenActions() {
		return forbiddenActions;
	}
	
	public PlanComponent getPlan() {
		return plan;
	}
	
}
