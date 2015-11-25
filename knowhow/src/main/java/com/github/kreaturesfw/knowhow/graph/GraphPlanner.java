package com.github.kreaturesfw.knowhow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.Desire;
import com.github.kreaturesfw.core.error.NotImplementedException;

/**
 * This class implements a sequential planning algorithm on
 * a planning graph. It is described in the Diploma Thesis
 * "Resource-bounded Planning of Communication under Confidentiality 
 * Constraints for BDI-Agents" as 'plan_control'
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
		LOG.debug("Entering controlPlan(desire={}, alternatives={}, targetLod="+targetLOD+")", goal.toString(), alternatives);
		
		List<WorkingPlan> plans = initalizePlanning(graph, goal);
		if(plans.isEmpty()) {
			return plans;
		}
		int iterations = iterativePlanning(alternatives, targetLOD, plans, new BreakCondition() {
			@Override
			public boolean breakCondition() {
				// TODO Auto-generated method stub
				return false;
			}
		});
		plans = filterPlans(alternatives, plans);
		
		LOG.debug("Leaving controlPlan() used '{}' iterations" + iterations);
		return plans;
	}

	private List<WorkingPlan> filterPlans(final int alternatives,
			List<WorkingPlan> plans) {
		// filter plans at the end
		Collections.sort(plans);
		plans = alternatives != 0 ? plans.subList(0, alternatives) : plans;
		List<WorkingPlan> toDel = new ArrayList<>();
		for(WorkingPlan wp : plans) {
			if(wp.isFailed()) {
				toDel.add(wp);
			}
		}
		plans.removeAll(toDel);
		return plans;
	}

	private int iterativePlanning(final int alternatives, final double targetLOD,
			List<WorkingPlan> plans, BreakCondition breakCondition) {
		// iterate the step function until enough plan alternatives are found:
		int complete_plans = 0;
		int iterations = 0;
		while((alternatives == 0 || complete_plans < alternatives) && !breakCondition.breakCondition()) {
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
			super.planOneStep(current, plans);
			++iterations;
			//LOG.info("The current plans after '{}' iterations:\n{}", iterations, plans);
			
			if(current.getLOD() >= targetLOD || current.isFailed()) {
				complete_plans += 1;
			}
		}
		return iterations;
	}

	/**
	 * Initalizes the planning by creating a list of working plans for a given
	 * graph and a given goal.
	 * @param graph
	 * @param goal
	 * @return	An non-empty list of plan alternatives that need further planning if no
	 * 			error occurred or an empty list of plan alternatives if the given graph cannot
	 * 			fulfill the given goal.
	 */
	private List<WorkingPlan> initalizePlanning(
			DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal) {
		// generate temporary start node in planning graph:
		Selector startNode = generateTemporaryGraph(graph, goal);
		if(startNode == null) {
			return new ArrayList<>();
		}
		
		// generate initial data structure used during the planning algorithm:
		List<WorkingPlan> plans = new ArrayList<>();
		for(DefaultEdge edge : graph.edgesOf(startNode)) {
			Processor p = (Processor)graph.getEdgeTarget(edge);
			WorkingPlan newPlan = new WorkingPlan(createPenaltyFunction(), goal);
			plans.add(newPlan);
			initPlan(p, newPlan);
			
			newPlan.visited(startNode);
			
		}
		return plans;
	}

	private void implResume(WorkingPlan plan, double targetLOD, BreakCondition breakCondition) {
		List<WorkingPlan> plans = new ArrayList<>();
		plans.add(plan);
		iterativePlanning(1, targetLOD, plans, breakCondition);
	}
	
	@Override
	public void resumePlan(WorkingPlan plan, double targetLOD) {
		implResume(plan, targetLOD, new BreakCondition() {
			@Override
			public boolean breakCondition() {
				return false;
			}
		});
	}
	
	@Override
	public void resumePlan(WorkingPlan plan, int newActionCount) {
		implResume(plan, 1, new BreakWhenXNewActions(plan));
	}

	@Override
	public void resumePlan(WorkingPlan plan, List<GraphIntention> complexIntentions, List<Integer> steps) {
		throw new NotImplementedException();
	}

	public static interface BreakCondition {
		boolean breakCondition();
	}
	
	public static class BreakWhenXNewActions implements BreakCondition {
		int neededActions;
		
		WorkingPlan wp;
		
		public BreakWhenXNewActions(WorkingPlan wp) {
			this(wp, 1);
		}
		
		public BreakWhenXNewActions(WorkingPlan wp, int actions) {
			this.wp = wp;
			this.neededActions = wp.getRootIntention().countActions() + actions;
		}
		
		@Override
		public boolean breakCondition() {
			return wp.getRootIntention().countActions() >= neededActions;
		}
		
	}
}
