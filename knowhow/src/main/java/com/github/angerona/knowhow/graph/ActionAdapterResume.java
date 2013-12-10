package com.github.angerona.knowhow.graph;

import com.github.angerona.fw.Action;

public class ActionAdapterResume extends Action {
	private WorkingPlan plan;
	
	private GraphIntention parent;
	
	private int indexInIntention;
	
	public ActionAdapterResume(WorkingPlan plan, GraphIntention parent, int indexInIntetion) {
		super("");
		this.plan = plan;
		this.parent = parent;
		this.indexInIntention = indexInIntetion;
	}
	
	public WorkingPlan getWorkingPlan() {
		return plan;
	}
	
	public GraphIntention getParentIntention() {
		return parent;
	}
	
	public int getIndexInIntention() {
		return indexInIntention;
	}
	
	@Override
	public String toString() {
		return "<ActionAdapter(ResumePlanning)>";
	}
}
