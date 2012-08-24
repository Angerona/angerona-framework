package angerona.fw;

import angerona.fw.logic.ViolatesResult;

/**
 * Represents a stack element. Skills need to save their context object to perform
 * the correct actions
 * @author Tim Janus
 */
public class PlanElement implements AngeronaAtom {
	private Intention intention;
	
	private Object executionData;
	
	private double costs;
	
	ViolatesResult res;
	
	/** use this object to give information about the PlanElement from Generation to
	 * 	selection. For example one could save if the intention represent by this plan
	 * 	is a lie and so on.
	 */
	private Object userData;
	
	public PlanElement(PlanElement other) {
		if(other.intention instanceof Skill) {
			this.intention = other.intention;
		} else {
			Subgoal sg = (Subgoal)other.intention;
			this.intention = (Intention)sg.clone();
		}
		this.executionData = other.executionData;
	}
	
	public PlanElement(Intention intention, Object context) {
		this.intention = intention;
		this.executionData = context;
	}
	
	public Intention getIntention() {
		return intention;
	}
	
	public void prepare() {
		intention.setDataObject(executionData);
	}
	
	public double getCosts() {
		return costs;
	}
	
	public void setCosts(double costs) {
		this.costs = costs;
	}
	
	public void setUserData(Object userData) {
		this.userData = userData;
	}
	
	public Object getUserData() {
		return userData;
	}

	@Override
	public ViolatesResult violates() {
		return res;
	}
	
	public void setViolatesResult(ViolatesResult res) {
		this.res = res;
	}
}
