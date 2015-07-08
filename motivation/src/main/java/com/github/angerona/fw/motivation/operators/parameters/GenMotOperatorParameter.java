package com.github.angerona.fw.motivation.operators.parameters;

import javax.management.AttributeNotFoundException;

import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.error.ConversionException;
import com.github.angerona.fw.island.operators.parameter.IslandPlanParameter;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.motivation.dao.ActionSequenceDao;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.dao.TimeSlotDao;
import com.github.angerona.fw.motivation.dao.impl.ActionSequences;
import com.github.angerona.fw.motivation.dao.impl.BeliefStateImpl;
import com.github.angerona.fw.motivation.dao.impl.GenLevelWeights;
import com.github.angerona.fw.motivation.dao.impl.GenMotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.GenWeightRanges;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dao.impl.MotiveStateImpl;
import com.github.angerona.fw.motivation.dao.impl.TimeSlots;
import com.github.angerona.fw.motivation.data.MotiveLevel;
import com.github.angerona.fw.operators.parameter.GenericOperatorParameter;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public abstract class GenMotOperatorParameter<L extends MotiveLevel> extends IslandPlanParameter {

	protected BeliefState beliefState;
	protected MotiveState<L> motiveState;

	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(gop);
		this.beliefState = new BeliefStateImpl(beliefs(), sequences(), timeSlots(), getDesires());
		this.motiveState = new MotiveStateImpl<L>(couplings(), ranges(), weights());
	}

	public BeliefState getBeliefState() {
		return beliefState;
	}

	public MotiveState<L> getMotiveState() {
		return motiveState;
	}

	public MotStructureDao getStructure() {
		return getAgent().getComponent(MotStructure.class);
	}

	public Desires getDesires() {
		return getAgent().getComponent(Desires.class);
	}

	protected Beliefs beliefs() {
		return getAgent().getBeliefs();
	}
	
	protected ActionSequenceDao sequences() {
		PlanComponent sequences = getActualPlan();
		if (sequences != null) {
			return new ActionSequences(sequences);
		}
		return null;
	}

	protected TimeSlotDao timeSlots() {
		return getAgent().getComponent(TimeSlots.class);
	}

	protected abstract GenMotiveCouplings<L> couplings();

	protected abstract GenWeightRanges<L> ranges();

	protected abstract GenLevelWeights<L> weights();

}
