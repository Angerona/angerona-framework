package com.github.angerona.knowhow.graph.parameter;

import java.util.List;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.Perception;
import com.github.angerona.knowhow.graph.GraphIntention;

/**
 * @todo use visitor pattern that uses a list of edges to traverse the bottom up path stored in the plan.
 * 
 * @author Tim Janus
 */
public interface PlanConverter {
	void init(Agent agent);
	
	List<Intention> convert(GraphIntention gi, Perception context);
}
