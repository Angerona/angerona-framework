package angerona.fw;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import angerona.fw.listener.SubgoalListener;

/**
 * 
 * @author Tim Janus
 */
public class MasterPlan extends BaseAgentComponent implements SubgoalListener{

	/** parallel plans of the Plan Component */
	private List<Subgoal> plans = new LinkedList<Subgoal>();
	
	public MasterPlan() {}
	
	public MasterPlan(MasterPlan plan) {
		super(plan);
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
	public Object clone() {
		return new MasterPlan(this);
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
				getAgent().removeDesire(sg.getFulfillsDesire());
			}
		}
	}
}
