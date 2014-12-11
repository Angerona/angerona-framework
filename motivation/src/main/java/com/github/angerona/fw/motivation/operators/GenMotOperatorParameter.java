package com.github.angerona.fw.motivation.operators;

import javax.management.AttributeNotFoundException;

import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.error.ConversionException;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.dao.impl.BeliefStateImpl;
import com.github.angerona.fw.motivation.dao.impl.GenLevelWeights;
import com.github.angerona.fw.motivation.dao.impl.GenMotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.GenWeightRanges;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dao.impl.MotiveStateImpl;
import com.github.angerona.fw.motivation.reliable.ActionSequenceDao;
import com.github.angerona.fw.motivation.reliable.TimeSlotDao;
import com.github.angerona.fw.motivation.reliable.impl.ActionSequences;
import com.github.angerona.fw.motivation.reliable.impl.TimeSlots;
import com.github.angerona.fw.operators.parameter.GenericOperatorParameter;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public abstract class GenMotOperatorParameter<L extends MotiveLevel> extends GenerateOptionsParameter {

	protected BeliefState beliefState;
	protected MotiveState<L> motiveState;

	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(gop);
		this.beliefState = new BeliefStateImpl(beliefs(), getSequences(), timeSlots());
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

	public ActionSequenceDao getSequences() {
		return getAgent().getComponent(ActionSequences.class);
	}

	protected Beliefs beliefs() {
		return getAgent().getBeliefs();
	}

	protected TimeSlotDao timeSlots() {
		return getAgent().getComponent(TimeSlots.class);
	}

	protected abstract GenMotiveCouplings<L> couplings();

	protected abstract GenWeightRanges<L> ranges();

	protected abstract GenLevelWeights<L> weights();

}
