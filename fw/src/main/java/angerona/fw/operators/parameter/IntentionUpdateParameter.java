package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.Intention;
import angerona.fw.Perception;
import angerona.fw.Plan;

public class IntentionUpdateParameter extends GenericOperatorParameter {
	protected List<Intention> skills;
	
	protected Plan plan;
	
	protected Perception perception;
	
	public IntentionUpdateParameter(Plan plan, List<Intention> skills, Perception perception) {
		super(plan.getAgent().getEnvironment());
		if(!plan.isPlan())
			throw new IllegalArgumentException("The given intention must be an high level plan");
		this.plan= plan;
		
		this.perception = perception;
	}
	
	public Plan getPlan() {
		return plan;
	}
	
	public Perception getPerception() {
		return perception;
	}
}
