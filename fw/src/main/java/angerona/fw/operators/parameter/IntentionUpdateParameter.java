package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.MasterPlan;
import angerona.fw.Perception;
import angerona.fw.Skill;

public class IntentionUpdateParameter extends GenericOperatorParameter {
	protected List<Skill> skills;
	
	protected MasterPlan plan;
	
	protected Perception perception;
	
	public IntentionUpdateParameter(MasterPlan plan, List<Skill> skills, Perception perception) {
		super(plan.getAgent());
		this.plan= plan;
		this.perception = perception;
	}
	
	public List<Skill> getSkills() {
		return skills;
	}
	
	public MasterPlan getPlan() {
		return plan;
	}
	
	public Perception getPerception() {
		return perception;
	}
}
