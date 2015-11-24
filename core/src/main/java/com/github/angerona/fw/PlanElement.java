package com.github.angerona.fw;

import com.github.angerona.fw.listener.ActionProcessor;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.util.Utility;

/**
 * A PlanElement is the data structure storing a step in a plan. It decouples the behavior of the plan
 * from its data. The intention member contains the behavior either as an action or a complex subgoal (plan).
 * 
 * For an atomic action the PlanElement encapsulates the execution. It provides the prepare method which sets the correct
 * data dependencies before running the action. It sets the used beliefs and registers the ActionProcessor which is
 * responsible to perform the actions (might be mental or physical). It also saves a executionData object which
 * contains the input-data for the action. 
 * 
 * The PlanElement also contains a userData object which saves simulation specific data for later use. For example
 * a boolean is stored indicating if the PlanElement represents a lie and give it a higher cost if it is a lie.
 * 
 * @see angerona.fw.listener.ActionProcessor
 * 
 * @todo add behavior of ActionAdapter of Know-how implementation
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
	 * Copy-Ctor: Copies all data of the plan element.
	 * @param other	Reference to the PlanElement which is source of the copy.
	 * @todo Implement deep copy correctly
	 * @todo Proof that exeuctionData and userData are cloneable
	 */
	public PlanElement(PlanElement other) {
		if(other.intention instanceof Action) {
			this.intention = other.intention;
		} else {
			Subgoal sg = (Subgoal)other.intention;
			this.intention = (Intention)sg.clone();
		}
		
		this.executionData = other.executionData;
		this.userData = other.userData;
		this.costs = other.costs;
	}
	
	/** @return	the intention representing the behavior of the PlanElement */
	public Intention getIntention() {
		return intention;
	}
	
	public void setIntention(Intention intention) {
		this.intention = intention;
	}
	
	/**
	 * Prepares the intention for execution. Therefore the intention has to be an Action.
	 * The parameters and the executionData object will be given to the action to prepare it.
	 * @param actionProcessor	The processor used to process the Action
	 * @param usedBeliefs		The Beliefs used during performing of the action.
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
	
	/** @return true if the intention of this PlanElement is an action otherwise false */
	public boolean isAtomic() {
		return this.intention.isAtomic();
	}
	
	@Override
	public boolean equals(Object other) {
		if(! (other instanceof PlanElement)) return false;
		
		PlanElement pe = (PlanElement)other;
		return Utility.equals(intention, pe.intention);
	}

	@Override
	public int hashCode() {
		return intention.hashCode() * 13;
	}
	
	@Override
	public void run() {
		if(prepared) {
			processor.performAction((Action)intention, intention.getAgent(), usedBeliefs);
		} else {
			throw new RuntimeException("Plan-Element was not prepared");
		}
	}
	
	@Override
	public String toString() {
		return intention.toString();
	}
}
