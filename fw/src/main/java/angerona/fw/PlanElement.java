package angerona.fw;

/**
 * Represents a stack element. Skills need to save their context object to perform
 * the correct actions
 * @author Tim Janus
 */
public class PlanElement {
	private Intention intention;
	
	private Object executionData;
	
	private double costs;
	
	/** use this object to give information about the PlanElement from Generation to
	 * 	selection. For example one could save if the intention represent by this plan
	 * 	is a lie and so on.
	 */
	private Object userData;
	
	public PlanElement(PlanElement other) {
		this.intention = (Intention)other.intention.clone();
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
}
