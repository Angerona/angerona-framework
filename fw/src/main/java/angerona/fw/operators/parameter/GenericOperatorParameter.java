package angerona.fw.operators.parameter;

import angerona.fw.operators.OperatorVisitor;

/**
 * Base class for all operator parameters.
 * @author Tim Janus
 */
public class GenericOperatorParameter {
	public GenericOperatorParameter(OperatorVisitor visitor) {
		this.visitor = visitor;
	}
	
	protected OperatorVisitor getSimulation() {
		return visitor;
	}
	
	private OperatorVisitor visitor;
}
