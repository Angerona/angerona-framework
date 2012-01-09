package angerona.fw.operators.parameter;

import angerona.fw.AngeronaEnvironment;

public class GenericOperatorParameter {
	public GenericOperatorParameter(AngeronaEnvironment simulation) {
		this.simulation = simulation;
	}
	
	public AngeronaEnvironment getSimulation() {
		return simulation;
	}
	
	private AngeronaEnvironment simulation;
}
