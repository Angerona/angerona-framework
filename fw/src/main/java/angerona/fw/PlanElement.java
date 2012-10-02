package angerona.fw;

import angerona.fw.listener.ActionProcessor;
import angerona.fw.logic.Beliefs;
import angerona.fw.logic.ViolatesResult;

/**
 * A PlanElement is the data-structure saved as step in a plan. It decouples the behavior of the plan
 * from its data. The intention member contains the behavior either as atomic Skill or complex Subgoal.
 * 
 * For a Skill the PlanElement encapsulates the execution. It provides the prepare method which sets the correct
 * data dependencies before running the Skill. It sets the used beliefs and registers the ActionProcessor which is
 * responsible to perform the actions (might be mental or physical). It also saves a executionData object which
 * contains the input-data for the Skill. In the simplest case this object is an Action and the skill does nothing
 * else then send the action to the ActionProcessor.
 * 
 * The PlanElement also contains a userData object which saves simulation specific data for later use. For example
 * could one save a boolean indicating if the PlanElement represents a lie and give it a higher cost if it is a lie.
 * 
 * @see angerona.fw.listener.ActionProcessor
 * @author Tim Janus
 */
public class PlanElement implements AngeronaAtom, Runnable {
	/** the intention containing information about the behavior (logic) of the PlanElement */
	private Intention intention;
	
	/** a complex object containing input data for performing the intention */
	private Object executionData;
	
	/** 
	 * 	The costs to perform the PlanElement. Costs can be given on creation time or calculated on the fly.
	 * 	The intentionUpdateOperator will use this member to select the best PlanElement
	 */
	private double costs;
	
	/**	data-structure containing information about the result of the last violates run */
	private ViolatesResult violates;
	
	/** internal flag used to proof if the prepare method was called before try to run the PlanElement */
	private boolean prepared;
	
	/** use this object to give information about the PlanElement from Generation to
	 * 	selection. For example one could save if the intention represent by this plan
	 * 	is a lie and so on.
	 */
	private Object userData;
	
	private ActionProcessor processor;
	
	private Beliefs usedBeliefs;
	
	/**
	 * CTor: Creates an Initial plan element
	 * @param intention		Reference to the Intention which will be used as behavior for this PlanElement.
	 */
	public PlanElement(Intention intention) {
		this(intention, null, null, 0);
	}
	
	/**
	 * CTor: Creates an Initial plan element
	 * @param intention		Reference to the Intention which will be used as behavior for this PlanElement.
	 * @param executionData	Object containing data needed for the execution of the Intention.
	 */
	public PlanElement(Intention intention, Object executionData) {
		this(intention, executionData, null, 0);
	}
	
	/**
	 * CTor: Creates an Initial plan element
	 * @param intention		Reference to the Intention which will be used as behavior for this PlanElement.
	 * @param executionData	Object containing data needed for the execution of the Intention.
	 * @param userData		Object containing user-specific data for later use.
	 */
	public PlanElement(Intention intention, Object executionData, Object userData) {
		this(intention, executionData, userData, 0);
	}
	
	/**
	 * CTor: Creates an Initial plan element
	 * @param intention		Reference to the Intention which will be used as behavior for this PlanElement.
	 * @param executionData	Object containing data needed for the execution of the Intention.
	 * @param userData		Object containing user-specific data for later use.
	 * @param cost			the cost for performing this plan-element.
	 */
	public PlanElement(Intention intention, Object executionData, Object userData, double cost) {
		this.intention = intention;
		this.executionData = executionData;
		this.costs = cost;
		this.userData = userData;
	}
	
	/**
	 * Copy-Ctor: Copies all data of the plan element, if the intention is a Skill its saved by reference otherwise
	 * it will be copied.
	 * @param other	Reference to the PlanElement which is source of the copy.
	 */
	public PlanElement(PlanElement other) {
		// TODO: Implement deep copy correctly
		if(other.intention instanceof Action) {
			this.intention = other.intention;
		} else {
			Subgoal sg = (Subgoal)other.intention;
			this.intention = (Intention)sg.clone();
		}
		
		// TODO: Proof that exeuctionData and userData are cloneable
		this.executionData = other.executionData;
		this.userData = other.userData;
		this.costs = other.costs;
		this.violates = new ViolatesResult(other.violates);
	}
	
	/** @return	the intention representing the behavior of the PlanElement */
	public Intention getIntention() {
		return intention;
	}
	
	/**
	 * Prepares the intention for execution. Therefore the intention has to be an Action.
	 * The parameters and the executionData object will be given to the skill to prepare it.
	 * @param actionProcessor	The processor used to process the Action
	 * @param usedBeliefs		The Beliefs used to perform the Skill.
	 * @return	true if the preparation was successful, false if the intention is no Action.
	 */
	public boolean prepare(ActionProcessor actionProcessor, Beliefs usedBeliefs) {
		if(!isAtomic())
			return prepared = false;
		
		this.processor = actionProcessor;
		this.usedBeliefs = usedBeliefs;
		return prepared = true;
	}
	
	/** @return the costs associated with the execution of the PlanElement */
	public double getCosts() {
		return costs;
	}
	
	/** changes the costs associated with the execution of the PlanElement */
	public void setCosts(double costs) {
		this.costs = costs;
	}
	
	/** sets the user-data used to save additional data about the PlanElement */
	public void setUserData(Object userData) {
		this.userData = userData;
	}
	
	/** @return the user-data object used to save additional data about the PlanElement */
	public Object getUserData() {
		return userData;
	}

	@Override
	public ViolatesResult violates() {
		return violates;
	}
	
	/**
	 * internally used by the ViolatesOperator to save the results of the last violation check.
	 * @param violates	data-structure containg results of violation check
	 */
	public void setViolatesResult(ViolatesResult violates) {
		this.violates = violates;
	}
	
	/** @return true if the intention of this PlanElement is a Skill otherwise false */
	public boolean isAtomic() {
		return this.intention.isAtomic();
	}
	
	public boolean equals(Object other) {
		if(! (other instanceof PlanElement)) return false;
		
		PlanElement pe = (PlanElement)other;
		return intention.equals(pe.intention);
	}

	@Override
	public void run() {
		if(prepared) {
			processor.performAction((Action)intention, intention.getAgent(), usedBeliefs);
		} else {
			throw new RuntimeException("Plan-Element was not prepared");
		}
	}
}
