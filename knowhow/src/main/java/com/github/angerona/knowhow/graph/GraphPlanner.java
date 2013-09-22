package com.github.angerona.knowhow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Tim Janus
 */
public class GraphPlanner extends GraphPlannerAdapter {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(GraphPlanner.class);
	
	@Override
	public List<WorkingPlan> controlPlan(
			DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal, 
			final int alternatives, final double targetLOD) {
		
		// generate temporary start node in planning graph:
		Selector startNode = generateTemporaryGraph(graph, goal);
		if(startNode == null) {
			return new ArrayList<>();
		}
		
		// generate initial data structure used during the planning algorithm:
		List<WorkingPlan> plans = new ArrayList<>();
		for(DefaultEdge edge : graph.edgesOf(startNode)) {
			Processor p = (Processor)graph.getEdgeTarget(edge);
			WorkingPlan newPlan = new WorkingPlan(createPenaltyFunction());
			plans.add(newPlan);
			newPlan.setNextNode(p);
			newPlan.visited(startNode);
		}
		
		// iterate the step function until enough plan alternatives are found:
		int complete_plans = 0;
		int iterations = 0;
		while(alternatives == 0 || complete_plans < alternatives) {
			Collections.sort(plans, new Comparator<WorkingPlan>() {
				@Override
				public int compare(WorkingPlan arg0, WorkingPlan arg1) {
					if(arg0.getLOD() >= targetLOD && arg1.getLOD() < targetLOD) {
						return 1;
					} else if(arg0.getLOD() < targetLOD && arg1.getLOD() >= targetLOD) {
						return -1;
					}
					return arg0.compareTo(arg1);
				}
			});
			WorkingPlan current = plans.get(0);
			
			// have all plans a higher value than target LOD? (Break condition for finding all plans)
			if(current.getLOD() >= targetLOD)
				break;
			
			// plan one step
			super.planOneStep(current, plans, graph, goal);
			++iterations;
			LOG.info("The current plans after '{}' iterations:\n{}", iterations, plans);
			
			if(current.getLOD() >= targetLOD) {
				complete_plans += 1;
			}
		}
		
		Collections.sort(plans);
		return alternatives != 0 ? plans.subList(0, alternatives) : plans;
	}
}
