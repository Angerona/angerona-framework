package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.MasterPlan;
import angerona.fw.Skill;

public class IntentionUpdateParameter extends GenericOperatorParameter {
	protected List<Skill> skills;
	
	protected MasterPlan plan;
	
	public IntentionUpdateParameter(MasterPlan plan, List<Skill> skills) {
		super(plan.getAgent());
		this.plan= plan;
	}
	
	public List<Skill> getSkills() {
		return skills;
	}
	
	public MasterPlan getPlan() {
		return plan;
	}
	
}
