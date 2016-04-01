package com.github.kreatures.knowhow.graph.parameter;

import java.util.List;

import com.github.kreatures.core.Agent;
import com.github.kreatures.core.Intention;
import com.github.kreatures.knowhow.graph.GraphIntention;
import com.github.kreatures.knowhow.graph.WorkingPlan;

/**
 * @todo use visitor pattern that uses a list of edges to traverse the bottom up path stored in the plan.
 * 
 * @author Tim Janus
 */
public interface PlanConverter {
	void init(Agent agent);
	
	List<Intention> convert(WorkingPlan plan, GraphIntention gi);
}
