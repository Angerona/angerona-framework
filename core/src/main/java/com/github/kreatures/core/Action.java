package com.github.kreatures.core;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.core.Commit;

import com.github.kreatures.core.reflection.Context;
import com.github.kreatures.core.reflection.ContextFactory;
import com.github.kreatures.core.reflection.ContextProvider;

/**
 * An action that can be performed by an agent to either communicate with other
 * agents or to affect the agent's environment. This class acts as a base class 
 * for different Actions used in the KReatures framework. 
 * 
 * @author Tim Janus
 */
public class Action 
	extends Intention 
	implements KReaturesAtom, ContextProvider {

	/** the unique name of the sender of the action **/
	@Element(name="sender")
	private String sender;
	
	/** Ctor used for deserialization */
	public Action(	@Element(name="sender") String senderId ) {
		super((Agent) null);
		this.sender = senderId;
	}
	
	/**
	 * Ctor: generates a new Action
	 * @param sender	unique name of the sender of the action
	 * @param receiver	unique name of the receiver of the action, static member ALL means everyone receives this action
	 */
	public Action(Agent sender) {
		super(sender);
		this.sender = sender.getName();
	}
	
	/**	@return the unique name of the sender */
	public String getSenderId() {
		return sender;
	}
	
	@Override
	public String toString() {
		return "Action '" + this.getClass().getSimpleName() + "' by '" + sender + "'" ;
	}
	
	@Override
	public Context getContext() {
		return ContextFactory.createContext(this);
	}
	
	@Override
	public void onSubgoalFinished(Intention subgoal) {
		if(super.parent != null) {
			super.parent.onSubgoalFinished(this);
		}
	}

	@Override
	public boolean isAtomic() {
		return true;
	}

	@Override
	public boolean isSubPlan() {
		return false;
	}
	
	@Commit
	public void onDeserialization() {
		KReaturesEnvironment sim = KReatures.getInstance().getActualSimulation();
		if(sim != null) {
			setAgent(sim.getAgentByName(sender));
		}
	}
}
