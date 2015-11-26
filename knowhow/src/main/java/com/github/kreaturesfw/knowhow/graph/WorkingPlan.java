package com.github.kreaturesfw.knowhow.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.kreaturesfw.core.legacy.Action;
import com.github.kreaturesfw.core.legacy.Desire;
import com.github.kreaturesfw.knowhow.penalty.PenaltyFunction;

/**
 * This class stores information of a plan that is work-in-progress. It is used
 * by the graph based planning algorithms. It contains a penalty and a level of
 * detail value.
 *  
 * @author Tim Janus
 */
public class WorkingPlan 
	implements Comparable<WorkingPlan>{
	
	private Desire goal;
	
	/** the root intention that describes the plan */
	private GraphIntention rootIntention;
	
	/** a pointer to the current intention use during the planning algorithm */
	private GraphIntention curIntention;
	
	/** the next node in the graph that will be processed */
	private GraphNode nextNode;
	
	/** the set of already visited graph nodes */
	private Set<GraphNode> visited = new HashSet<>();
	
	/** the history of visited graph nodes as stack */
	private Stack<GraphNode> history = new Stack<>();
	
	/** the penalty stored in this plan where Integer.MAX means infinity */
	private double penalty = 0;
	
	/** the level of detail of this working plan, zero means the plans is empty and one means the plan is complete */
	private double lod = 0;
	
	private double weight = 0;
	
	/** the index of the next child node */
	private int nextIndex = -1;
	
	private PenaltyFunction penaltyFunction;
	
	public WorkingPlan(PenaltyFunction pf, Desire goal) {
		this.penaltyFunction = pf;
		this.goal = goal;
	}
	
	public WorkingPlan(WorkingPlan wp) {
		this.visited = new HashSet<>(wp.visited);
		this.history = new Stack<GraphNode>();
		this.history.addAll(wp.history);
		if(!this.history.equals(wp.history)) {
			throw new IllegalStateException();
		}
		
		this.penalty = wp.penalty;
		this.lod = wp.lod;
		this.penaltyFunction = wp.penaltyFunction.clone();
		this.goal = wp.goal;
		
		this.nextIndex = wp.nextIndex;
		this.nextNode = wp.nextNode;
		
		this.rootIntention = new GraphIntention(wp.rootIntention);
		List<Integer> path = wp.rootIntention.getPathToSubIntention(wp.curIntention);
		this.curIntention = this.rootIntention;
		for(Integer way : path) {
			this.curIntention = curIntention.getSubIntentions().get(way);
		}
	}
	
	public Desire getGoal() {
		return goal;
	}
	
	public void incrementWeight(double weight) {
		this.weight += weight;
	}
	
	boolean isFailed() {
		return penalty == Double.MAX_VALUE;
	}
	
	public GraphIntention getCurrentIntention() {
		return curIntention;
	}
	
	public void setCurrentIntention(GraphIntention intention) {
		this.curIntention = intention;
		if(this.rootIntention == null)
			this.rootIntention = curIntention;
	}
	
	public GraphIntention getRootIntention() {
		return rootIntention;
	}
	
	public GraphNode getPredecessor() {
		return history.peek();
	}
	
	public int getWorkingNodeIndex() {
		return nextIndex;
	}
	
	public void setWorkingNodeIndex(int nextIndex) {
		this.nextIndex = nextIndex;
	}
	
	/** 
	 * Process the a new level of detail value 
	 */
	public void updateLOD() {
		int actions = rootIntention.countActions();
		double complexity = (double)rootIntention.getComplexity();
		lod = actions / (complexity);
	}
	
	/** @return the current level of detail value, use updateLOD() to process the current value */
	public double getLOD() {
		return lod;
	}
	
	/** @return the penalty the plan gets */
	public double getPenalty() {
		return penalty;
	}
	
	public void fail() {
		penalty = Double.MAX_VALUE;
	}
	
	/**
	 * Increments the penalty by mentally performing the action
	 * @param penalty	The amount of increment
	 */
	public void incrementPenalty(Action action) {
		double reval = penaltyFunction.penalty(action);
		penalty = reval == Double.MAX_VALUE ? Double.MAX_VALUE : penalty + reval;
	}
	
	/** 
	 * checks the condition given as parameter and adapt penalty to infinity if
	 * the condition is not valid
	 * @param condition
	 */
	public void checkCondition(FolFormula condition) {
		if(!penaltyFunction.validCondition(condition)) {
			penalty = Double.MAX_VALUE;
		}
	}
	
	void setNextNode(GraphNode nextNode) {
		this.nextNode = nextNode;
		
	}
	
	boolean hasVisit(GraphNode node) {
		return visited.contains(node);
	}
	
	public void moveToPrecessorOfPrecessor() {
		if(history.size() < 3)
			throw new IllegalStateException("Shall not be called yet, history size: '" + history.size() + "'");
		history.pop();
		history.pop();
		setNextNode(history.pop());
		
		curIntention = curIntention.getParent();
		if(curIntention == null) {
			curIntention = rootIntention;
		}
	}
	
	void visited(GraphNode node) {
		this.visited.add(node);
		this.history.push(node);
	}
	
	GraphNode getNextNode() {
		return this.nextNode;
	}

	@Override
	public String toString() {
		String reval = "LOD: '" + lod + "', Penalty: '" + penalty + 
				"' - curNode: '" + getNextNode().toString() + "' - history: '" +
				history + "'";
		if(rootIntention != null) {
			reval += " - rootIntention: '" + rootIntention.toString() + "'";
		}
		return reval;
	}
	
	@Override
	public int compareTo(WorkingPlan o) {
		if(penalty < o.penalty) {
			return -1;
		} else if(penalty > o.penalty) {
			return 1;
		} else {
			if(weight < o.weight) {
				return 1;
			} else if (weight > o.weight) {
				return -1;
			}
		}
		return 0;
	}
}
