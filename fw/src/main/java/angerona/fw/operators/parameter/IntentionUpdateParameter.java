package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.Action;
import angerona.fw.MasterPlan;

public class IntentionUpdateParameter extends GenericOperatorParameter {
	protected List<Action> forbiddenActions;
	
	protected MasterPlan plan;
	
	public IntentionUpdateParameter(MasterPlan plan, List<Action> forbidden) {
		super(plan.getAgent());
		this.plan= plan;
	}
	
	public List<Action> getForbiddenActions() {
		return forbiddenActions;
	}
	
	public MasterPlan getPlan() {
		return plan;
	}
	
}
