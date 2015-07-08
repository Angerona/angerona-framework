package com.github.angerona.fw.motivation.dao.impl;

import java.util.List;
import java.util.Stack;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.Subgoal;
import com.github.angerona.fw.motivation.dao.ActionSequenceDao;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionSequences implements ActionSequenceDao {
	
	protected PlanComponent sequences;

	public ActionSequences(PlanComponent sequences) {
		this.sequences = sequences;
	}
	
	@Override
	public List<Action> getSequence(Desire d) {
		// TODO: search for plan for Desire d
		return null;
	}
	
	@Override
	public void putSequence(Desire d, List<Action> seq) {
		if (seq != null) {
			Subgoal plan = new Subgoal(sequences.getAgent(), d);
			Stack<PlanElement> temp = new Stack<>();
			
			for (Action a : seq) {
				temp.push(new PlanElement(a));
			}

			plan.newStack();
			plan.setStack(0, temp);
			sequences.addPlan(plan);
		}

	}

	@Override
	public void clear() {
		sequences.clear();	
	}
	
}
