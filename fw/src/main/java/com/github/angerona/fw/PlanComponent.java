package com.github.angerona.fw;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.angerona.fw.listener.SubgoalListener;
import com.github.angerona.fw.logic.Desires;

/**
 * The PlanComponent is data storage for plans.
 * @author Tim Janus
 */
public class PlanComponent extends BaseAgentComponent implements SubgoalListener{

	/** parallel plans of the Plan Component */
	private List<Subgoal> plans = new LinkedList<Subgoal>();
	
	public PlanComponent() {}
	
	public PlanComponent(PlanComponent plan) {
		super(plan);
		for(Subgoal sg : plan.plans) {
			Subgoal copy = new Subgoal(sg);
			plans.add(copy);
		}
	}
	
	/**
	 * Counts how many plans exists to fullfil the given desire.
	 * @param d		References to desires d.
	 * @return		The number representing the count of plans which fulfill the desire d.
	 */
	public int countPlansFor(Desire d) {
		int reval = 0;
		for(Subgoal sg : plans) {
			if(sg.getFulfillsDesire().equals(d))
				reval += 1;
		}
		return reval;
	}
	
	public boolean addPlan(Subgoal plan) {
		if(plan == null || plans.contains(plan))
			return false;
		
		boolean reval = plans.add(plan);
		if(reval) {
			report("New plan for desire '"+plan.getFulfillsDesire()+"' generated.");
		}
		return reval;
	}
	
	public boolean removePlan(Subgoal plan) {
		boolean reval = plans.remove(plan);
		if(reval) {
			report("Removed plan for desire '"+plan.getFulfillsDesire()+"'.");
		}
		return reval;
	}
	
	public List<Subgoal> getPlans() {
		return Collections.unmodifiableList(plans);
	}

	@Override
	public PlanComponent clone() {
		return new PlanComponent(this);
	}

	@Override
	public void init(Map<String, String> additionalData) {
		this.getAgent().addSubgoalListener(this);
	}

	@Override
	public void onSubgoalFinished(Intention subgoal) {
		if(subgoal.getParent() == null) {
			Subgoal sg = (Subgoal) subgoal;
			removePlan(sg);
			if(sg.getFulfillsDesire() != null) {
				getAgent().getComponent(Desires.class).remove(sg.getFulfillsDesire());
			}
		}
	}
	
	@Override
	public String toString() {
		return "Plan Data-Structure";
	}
}
