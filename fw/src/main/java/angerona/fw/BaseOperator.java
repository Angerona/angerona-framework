package angerona.fw;

import java.util.HashMap;
import java.util.Map;

import angerona.fw.internal.Entity;
import angerona.fw.report.ReportPoster;

/**
 * Base class for all operators for agents
 * implements basic functionality for the report mechanism.
 * @author Tim Janus
 */
public class BaseOperator implements ReportPoster {
	
	/** reference to the agent owning the operator */
	private Agent owner;
	
	protected Map<String, String> parameters = new HashMap<String, String>();
	
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
	 * Change the user-parameters used by the operator. 
	 * @param parameters
	 */
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	
	public Map<String, String> getParameters() {
		return this.parameters;
	}
	
	/**
	 * Helper method: Allows sub classes to easily use the report mechanisms of Angerona.
	 * @param msg	The message which will be reported.
	 */
	protected void report(String msg) {
		Angerona.getInstance().report(msg, this);
	}
	
	/**
	 * Helper method: Allows sub classes to easily use the report mechanisms of Angerona.
	 * @param msg			The message which will be reporated
	 * @param attachment	The entity used as attachment for the report.
	 */
	protected void report(String msg, Entity attachment) {
		Angerona.getInstance().report(msg, this, attachment);
	}

	@Override
	public AngeronaEnvironment getSimulation() {
		return owner.getSimulation();
	}

	@Override
	public String getPosterName() {
		return this.getClass().getSimpleName();
	}
}
