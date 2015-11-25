package com.github.kreaturesfw.knowhow.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.translators.aspfol.AspFolTranslator;
import net.sf.tweety.lp.asp.syntax.DLPAtom;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.Action;
import com.github.kreaturesfw.core.Desire;
import com.github.kreaturesfw.core.Intention;
import com.github.kreaturesfw.core.Subgoal;
import com.github.kreaturesfw.knowhow.KnowhowStatement;
import com.github.kreaturesfw.knowhow.graph.parameter.DefaultPlanConverter;
import com.github.kreaturesfw.knowhow.graph.parameter.PlanConverter;
import com.github.kreaturesfw.knowhow.penalty.PenaltyFunction;

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
	
	private static AspFolTranslator translator = new AspFolTranslator();
	
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
	public void resumePlan(WorkingPlan plan) {
		resumePlan(plan, 1.0);
	}
	
	@Override
	public void resumePlan(WorkingPlan plan, GraphIntention complexIntention,
			int step) {
		List<GraphIntention> intentions = new ArrayList<>();
		List<Integer> steps = new ArrayList<>();
		
		intentions.add(complexIntention);
		steps.add(step);
		resumePlan(plan, intentions, steps);
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
		
		Selector sel = new Selector(new DLPAtom(goal.toString()), graph);
		graph.addVertex(sel);
		for(Processor p : alternatives) {
			graph.addEdge(sel, p);
		}
		return sel;
	}
	
	protected void planOneStep(WorkingPlan curPlan, List<WorkingPlan> plans) {
		GraphNode node = curPlan.getNextNode();
		LOG.debug("Entering planOneStep(curPlan={}, curNode={})", curPlan.toString(), node);

		Action actionAdded = null;
		
		if(node instanceof Selector) {
			List<WorkingPlan> subPlans = new ArrayList<>();
			Selector sel = (Selector)node;
		
			List<Processor> children = sel.getChildren();
			Collections.sort(children, new Comparator<Processor>() {
				@Override
				public int compare(Processor p1, Processor p2) {
					if(!p1.isAtomic() && !p2.isAtomic()) {
						if(p1.getStatement().getWeight() < p2.getStatement().getWeight())
							return -1;
						else if(p1.getStatement().getWeight() == p2.getStatement().getWeight())
							return -p1.getStatement().toString().compareTo(p2.getStatement().toString());
						else
							return 1;
					} else if(p1.isAtomic() && !p2.isAtomic()) {
						return 1;
					} else if(!p1.isAtomic() && p2.isAtomic()) {
						return -1;
					} else {
						return p1.getName().compareTo(p2.getName());
					}
				}
			});
			subPlans.add(curPlan);
			
			// copy plans:
			for(int i=1; i<children.size(); ++i) {
				subPlans.add(new WorkingPlan(curPlan));
			}
			
			// init after creation
			for(int i=0; i<subPlans.size(); ++i) {
				initPlan(children.get(i), subPlans.get(i));
			}
			
			subPlans.remove(curPlan);
			
			// also update the copied plans with the information that will
			// be processed during this step plan
			for(WorkingPlan wp : subPlans) {
				wp.visited(node);
				curPlan.updateLOD();
			}
			
			plans.addAll(subPlans);
		} else if(node instanceof Processor) {
			final Processor pro = (Processor)node;
			final List<Selector> children = pro.getChildren();
			
			// replace the sub intention of the current intention
			// to the intention formed by this processor:
			if(children.size() == 0 || !curPlan.hasVisit(node)) {
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
					List<Intention> actionInList = getPlanConverter().convert(curPlan, newIntention);
					if(actionInList.size() != 1) {
						throw new IllegalStateException();
					}
					
					if(! (actionInList.get(0) instanceof Action)) {
						throw new IllegalStateException();
					}
					
					actionAdded = (Action)actionInList.get(0);
					curPlan.incrementPenalty(actionAdded);
				}
			}
			
			if(children.size() > 0) {
				// First check if condition is true:
				List<DLPAtom> conditions = pro.getStatement().getConditions();
				for(DLPAtom atom : conditions) {
					FolFormula formula = translator.toFOL(atom);
					curPlan.checkCondition(formula);
				}
				
				List<Integer> indexList = new ArrayList<>();
				for(int i=0; i<children.size(); ++i) {
					indexList.add(i);
				}
				
				// @todo: calculate irrelevance like complexity beforehand and
				// also pre-calculate the index-list
				
				// sort children by irrelevance: (if atomic nothing happens here)
				Collections.sort(indexList, new Comparator<Integer>() {
					@Override
					public int compare(Integer i1, Integer i2) {
						Selector child1 = children.get(i1);
						Selector child2 = children.get(i2);
						
						KnowhowStatement stmt = pro.getStatement();
						int index1 = -1, index2 = -1;
						for(int i=0; i<stmt.getSubTargets().size(); ++i) {
							DLPAtom subtarget = stmt.getSubTargets().get(i);
							if(subtarget.equals(child1.getSubTarget())) {
								index1 = i;
							}
							if(subtarget.equals(child2.getSubTarget())) {
								index2 = i;
							}
						}
						
						if(index1 != -1 && index2 != -1) {
							double irr1 = stmt.getIrrelevance().get(index1);
							double irr2 = stmt.getIrrelevance().get(index2);
							
							if(irr1 == irr2) {
								return index1 - index2;
							} else {
								if(irr1 < irr2) {
									return -1;
								} else if(irr2 < irr1) {
									return 1;
								}
							}
						}
						
						return 0;
					}
				});
				
				// work on the next best relevant selector:
				for(int i=0; i<children.size(); ++i) {
					int index = indexList.get(i);
					Selector sel = (Selector)children.get(index);
					if(!curPlan.hasVisit(sel)) {
						curPlan.setNextNode(sel);
						curPlan.setWorkingNodeIndex(index);
						break;
					}
				}
			}
		}
		
		curPlan.updateLOD();
		curPlan.visited(node);
		
		if(actionAdded != null) {
			LOG.info("Add Action: '{}' to plan, new LOD: '{}'", actionAdded, curPlan.getLOD());
		}
		
		if(curPlan.getNextNode() == node) {
			curPlan.moveToPrecessorOfPrecessor();
		}
		
		LOG.debug("Leaving planOneStep(curPlan={}) = void", curPlan.toString());
	}

	protected void initPlan(Processor cur, WorkingPlan wp) {
		wp.setNextNode(cur);
		if(cur.getStatement() != null) {
			wp.incrementWeight(cur.getStatement().getWeight());
		}
	}
}
