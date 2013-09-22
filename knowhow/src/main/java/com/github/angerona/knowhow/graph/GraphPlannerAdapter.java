package com.github.angerona.knowhow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.knowhow.graph.parameter.DefaultPlanConverter;
import com.github.angerona.knowhow.graph.parameter.PlanConverter;
import com.github.angerona.knowhow.penalty.PenaltyFunction;

/**
 * This class implements utility functions of the {@link GraphPlannerStrategy}, it also
 * implements the algorithm 'plan_relevant' of the diploma thesis: 
 * "Resource-bounded Planning of Communication under Confidentiality Constraints for BDI-Agents" of
 * Tim Janus. This algorithm performs one planning step on a plan data structure @link {@link WorkingPlan}.
 * 
 * The Translation between the {@link WorkingPlan} and the {@link Subgoal} structure of Angerona is done by
 * the {@link DefaultPlanConverter} which implements the {@link PlanConverter} interface. The used {@link PlanConverter}
 * can be set using the setPlanConverter() method.
 * 
 * The used penalty function is given by the method setPenaltyTemplate(), it is a template because the penalty function
 * stores it's state, such that it knows the previous tested actions and can add effects on top on previous actions. 
 * The {@link PenaltyFunction} interface can be implemented to give the planner another behavior, to define if an
 * agent wants to act honestly or if keeping it's secrets is it's highest goal.
 * 
 * Subclasses have to implement the controlPlan() method with the most advanced signature to generate a full functional
 * planner, but they can use the planOneStep() which is thread safe because it only works on the data given as parameter,
 * such that multiple threads can call planOneStep on different {@link WorkingPlan} data structures. The method 
 * generateTemporaryGraph() generates a {@link Selector} node that acts as starting point for the planning algorithm. It
 * is the responsibility of the sub class to delete this {@link Selector} node. The algorithm in generateTempoaryGraph()
 * is described in the diploma thesis in Listing "Secrecy Safe Planning Algrithm Start" 
 * 
 * @author Tim Janus
 */
public abstract class GraphPlannerAdapter 
	implements GraphPlannerStrategy {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(GraphPlannerStrategy.class);
	
	private PenaltyFunction template;
	
	private PlanConverter converter;
	
	@Override
	public PenaltyFunction createPenaltyFunction() {
		return template.clone();
	}

	@Override
	public void setPenaltyTemplate(PenaltyFunction template) {
		this.template = template;
	}
	
	@Override
	public List<WorkingPlan> controlPlan(
			DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal) {
		return controlPlan(graph, goal, 0);
	}

	@Override
	public List<WorkingPlan> controlPlan(
			DirectedGraph<GraphNode, DefaultEdge> graph, Desire goal, int alternatives) {
		return controlPlan(graph, goal, alternatives, 1.0);
	}
	
	@Override
	public PlanConverter getPlanConverter() {
		return converter;
	}

	@Override
	public void setPlanConverter(PlanConverter converter) {
		this.converter = converter;
	}
	
	/**
	 * This methods adapts a planning graph, such that it contains a temporary start-node that
	 * can be used by the planning algorithm.
	 * @param graph	The graph that is adapted
	 * @param goal	The desire representing the goal the agent want to reach with the plan
	 * @return		The Selector node representing the start node for the planning algorithm or
	 * 				zero if the temporary selector cannot be connected with a node in the graph.
	 */
	protected Selector generateTemporaryGraph(DirectedGraph<GraphNode, DefaultEdge> graph, 
			Desire goal) {
		Set<Processor> alternatives = new HashSet<>();
			
		BreadthFirstIterator<GraphNode, DefaultEdge> it = new BreadthFirstIterator<>(graph);
		while(it.hasNext()) {
			GraphNode node = it.next();
			if(node instanceof Processor) {
				Processor p = (Processor)node;
				if(p.getName().equals(goal.getFormula().getPredicates().iterator().next().getName())) {
					alternatives.add(p);
				}
			}
		}
		
		if(alternatives.size() == 0)
			return null;
		
		Selector sel = new Selector(goal.toString(), graph);
		graph.addVertex(sel);
		for(Processor p : alternatives) {
			graph.addEdge(sel, p);
		}
		return sel;
	}
	
	protected void planOneStep(WorkingPlan curPlan, List<WorkingPlan> plans,
			DirectedGraph<GraphNode, DefaultEdge> graph, Desire des) {
		GraphNode node = curPlan.getNextNode();
		LOG.info("Working on node: '{}'" + node.toString());
		
		if(node instanceof Selector) {
			List<WorkingPlan> subPlans = new ArrayList<>();
			Selector sel = (Selector)node;
		
			List<Processor> children = sel.getChildren();
			subPlans.add(curPlan);
			
			// copy plans:
			for(int i=1; i<children.size(); ++i) {
				subPlans.add(new WorkingPlan(curPlan));
				throw new NotImplementedException("No deep copy of WorkingPlan yet.");
			}
			
			// adapt pointer to children
			for(int i=0; i<subPlans.size(); ++i) {
				Processor cur = children.get(i);
				subPlans.get(i).setNextNode(cur);
			}
			
			subPlans.remove(curPlan);
			plans.addAll(subPlans);
		} else if(node instanceof Processor) {
			Processor pro = (Processor)node;
			List<Selector> children = pro.getChildren();
			
			// replace the sub intention of the current intention
			// to the intention formed by this processor:
			if(!curPlan.hasVisit(pro)) {
				GraphIntention newIntention = new GraphIntention(pro, (Selector)curPlan.getPredecessor(), 
						curPlan.getCurrentIntention());
				if(curPlan.getWorkingNodeIndex() != -1) {
					curPlan.getCurrentIntention().replaceSubIntention(
							curPlan.getWorkingNodeIndex(), newIntention);
				}
				
				// update the current intention of the plan
				curPlan.setCurrentIntention(newIntention);
				curPlan.setWorkingNodeIndex(-1);
				
				if(children.size() == 0) {
					List<Intention> actionInList = getPlanConverter().convert(newIntention, des.getPerception());
					if(actionInList.size() != 1) {
						throw new IllegalStateException();
					}
					
					if(! (actionInList.get(0) instanceof Action)) {
						throw new IllegalStateException();
					}
					
					curPlan.incrementPenalty((Action)actionInList.get(0));
				}
			}
			
			if(children.size() > 0) {
				// sort children by irrelevance: (if atomic nothing happens anymore
				// TODO Overwork irrelevance concept
				Collections.sort(children, new Comparator<Selector>() {
					@Override
					public int compare(Selector o1, Selector o2) {
						// TODO Auto-generated method stub
						return 0;
					}
				});
				
				// work on the next best relevant selector:
				for(int i=0; i<children.size(); ++i) {
					Selector sel = (Selector)children.get(i);
					if(!curPlan.hasVisit(sel)) {
						curPlan.setNextNode(sel);
						curPlan.setWorkingNodeIndex(i);
						break;
					}
				}
			}
		}
		
		// TODO: Intention update
		curPlan.updateLOD();
		curPlan.visited(node);
		
		if(curPlan.getNextNode() == node) {
			curPlan.moveToPrecessorOfPrecessor();
		}
	}
}
