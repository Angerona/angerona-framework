package com.github.kreatures.swarm.basic;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.swarm.components.SwarmActionRef;

/**
 * 
 * @author donfack
 *
 */
public class SwarmAction extends Action {
	/**
	 * actionRef is the reference of the action. This tells us what a agent want to do next.
	 */
	private SwarmActionRef actionRef;
	
	public SwarmAction(Agent sender,SwarmActionRef actionRef ) {
		super(sender.getName());
		this.setAgent(sender);
		this.actionRef=actionRef;
	}
	
	/**
	 * 
	 * @return 
	 */
	public SwarmActionRef getActionRef(){
		return this.actionRef;
	}
	
	@Override
	public String toString() {
		return actionRef + " by " + this.getSenderId() + "" ;
	}
}


