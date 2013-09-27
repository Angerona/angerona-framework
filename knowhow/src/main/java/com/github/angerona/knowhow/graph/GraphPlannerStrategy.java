package com.github.angerona.knowhow.graph;

import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.knowhow.graph.parameter.PlanConverter;
import com.github.angerona.knowhow.penalty.PenaltyFunction;

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
	
	void setKnowledge(Set<FolFormula> knowledge);
	
	Beliefs getBeliefs();
	
	void setBeliefs(Beliefs beliefs);
	
	List<WorkingPlan> controlPlan(DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal);

	List<WorkingPlan> controlPlan(DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal, 
			int alternatives);
	
	List<WorkingPlan> controlPlan(DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal, 
			int alternatives, double targetLOD);
}
