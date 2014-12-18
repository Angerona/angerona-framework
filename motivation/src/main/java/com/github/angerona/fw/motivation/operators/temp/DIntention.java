package com.github.angerona.fw.motivation.operators.temp;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.Intention;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class DIntention extends Intention {

	private Desire related;

	public DIntention(Agent agent, Desire related) {
		super(agent);

		if (related == null) {
			throw new NullPointerException("related desire must not be null");
		}

		this.related = related;
	}

	public Desire getRelated() {
		return related;
	}

	@Override
	public void onSubgoalFinished(Intention subgoal) {}

	@Override
	public boolean isAtomic() {
		return true;
	}

	@Override
	public boolean isSubPlan() {
		return false;
	}

}
