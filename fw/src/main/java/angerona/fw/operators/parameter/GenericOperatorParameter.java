package angerona.fw.operators.parameter;

import angerona.fw.Agent;

/**
 * Base class for all parameter classes.
 * @author Tim Janus
 */
public class GenericOperatorParameter {
	public GenericOperatorParameter(Agent agentContext) {
		this.agentContext = agentContext;
	}
	
	public Agent getAgent() {
		return agentContext;
	}
	
	private Agent agentContext;
}
