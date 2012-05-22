package angerona.fw;

import angerona.fw.internal.Entity;

/**
 * Base class for all operators for agents
 * implements basic functionality for the report mechanism.
 * @author Tim Janus
 */
public class BaseOperator {
	
	/** reference to the agent owning the operator */
	private Agent owner;
	
	/** @return reference to the agent owning the operator */
	public Agent getOwner() {
		return owner;
	}
	
	/**
	 * changes the owner of this operator
	 * @param ag	reference to the new owner.
	 */
	protected void setOwner(Agent ag) {
		this.owner = ag;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Helper method: Allows sub classes to easily use the report mechanisms of Angerona.
	 * @param msg	The message which will be reported.
	 */
	protected void report(String msg) {
		Angerona.getInstance().report(msg, owner);
	}
	
	/**
	 * Helper method: Allows sub classes to easily use the report mechanisms of Angerona.
	 * @param msg			The message which will be reporated
	 * @param attachment	The entity used as attachment for the report.
	 */
	protected void report(String msg, Entity attachment) {
		Angerona.getInstance().report(msg, owner, attachment);
	}
}
