package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.Perception;
import angerona.fw.Plan;
import angerona.fw.Skill;

public class IntentionUpdateParameter extends GenericOperatorParameter {
	protected List<Skill> skills;
	
	protected Plan plan;
	
	protected Perception perception;
	
	public IntentionUpdateParameter(Plan plan, List<Skill> skills, Perception perception) {
		super(plan.getAgent().getEnvironment());
		if(!plan.isPlan())
			throw new IllegalArgumentException("The given intention must be an high level plan");
		this.plan= plan;
		
		this.perception = perception;
	}
	
	public List<Skill> getSkills() {
		return skills;
	}
	
	public Plan getPlan() {
		return plan;
	}
	
	public Perception getPerception() {
		return perception;
	}
}
