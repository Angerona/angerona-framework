package com.github.angerona.knowhow.graph.parameter;

import java.util.List;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Intention;
import com.github.angerona.knowhow.graph.GraphIntention;
import com.github.angerona.knowhow.graph.WorkingPlan;

/**
 * @todo use visitor pattern that uses a list of edges to traverse the bottom up path stored in the plan.
 * 
 * @author Tim Janus
 */
public interface PlanConverter {
	void init(Agent agent);
	
	List<Intention> convert(WorkingPlan plan, GraphIntention gi);
}
