package com.github.kreaturesfw.knowhow.graph.parameter;

import java.util.List;

import com.github.kreaturesfw.core.Agent;
import com.github.kreaturesfw.core.Intention;
import com.github.kreaturesfw.knowhow.graph.GraphIntention;
import com.github.kreaturesfw.knowhow.graph.WorkingPlan;

/**
 * @todo use visitor pattern that uses a list of edges to traverse the bottom up path stored in the plan.
 * 
 * @author Tim Janus
 */
public interface PlanConverter {
	void init(Agent agent);
	
	List<Intention> convert(WorkingPlan plan, GraphIntention gi);
}
