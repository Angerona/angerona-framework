package com.github.kreaturesfw.knowhow.graph;

import java.util.List;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.github.kreaturesfw.core.legacy.Desire;
import com.github.kreaturesfw.core.legacy.Intention;
import com.github.kreaturesfw.knowhow.graph.parameter.PlanConverter;
import com.github.kreaturesfw.knowhow.penalty.PenaltyFunction;

public interface GraphPlannerStrategy {
	
	/** 
	 * @return 	a new instance of the used penalty function of this planner, they are copies
	 * 			of the penalty function template, such that they all have their own state.			
	 */
	PenaltyFunction createPenaltyFunction();
	
	/**
	 * Sets the template penalty function of the planner, this template is used to create
	 * penalty functions by copying.
	 * @param pf	An reference to the new penalty function if this is null the default penalty
	 * 				function, that returns zero each time is used.
	 */
	void setPenaltyTemplate(PenaltyFunction pf);
	
	/**
	 * @return a {@link PlanConverter} that is used to translate the {@link GraphIntention} into {@link Intention}.
	 */
	PlanConverter getPlanConverter();
	
	void setPlanConverter(PlanConverter converter);
	
	List<WorkingPlan> controlPlan(DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal);

	List<WorkingPlan> controlPlan(DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal, 
			int alternatives);
	
	List<WorkingPlan> controlPlan(DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal, 
			int alternatives, double targetLOD);
	
	void resumePlan(WorkingPlan plan);
	
	void resumePlan(WorkingPlan plan, double targetLOD);
	
	void resumePlan(WorkingPlan plan, int newActionCount);
	
	void resumePlan(WorkingPlan plan, GraphIntention complexIntention, int step);
	
	void resumePlan(WorkingPlan plan, List<GraphIntention> complexIntentions, List<Integer> steps);
}
