package com.github.kreatures.swarm.components;

import com.github.kreatures.core.BaseAgentComponent;
/**
 * This is the default implement of an edge. a object of this class behaves all types of edges. that means visit-, place- and timeEdge.
 * @author donfack
 *
 */
public class DefaultEdge extends SwarmEdgeGeneric {
	
	public DefaultEdge(DefaultEdge edge){
		super(edge);
	}

	public DefaultEdge(int id,int incommingStation, int outcommingStation, int edgeLenght, boolean underected) {
		super(id, incommingStation, outcommingStation, edgeLenght, underected);
		
	}

	@Override
	public BaseAgentComponent clone() {
		return new DefaultEdge(this);
	}

}
