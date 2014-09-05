package com.github.angerona.fw.motivation.operators;

import javax.management.AttributeNotFoundException;

import net.sf.tweety.Formula;

import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.error.ConversionException;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.dao.impl.GenLevelWeights;
import com.github.angerona.fw.motivation.dao.impl.GenMotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.GenWeightRanges;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dao.impl.MotiveStateImpl;
import com.github.angerona.fw.operators.parameter.GenericOperatorParameter;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public abstract class GenMotOperatorParameter<L extends MotiveLevel, F extends Formula> extends GenerateOptionsParameter {

	protected BeliefState<F> beliefState;
	protected MotiveState<L, F> motiveState;

	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(gop);
		this.beliefState = beliefState();
		this.motiveState = new MotiveStateImpl<L, F>(couplings(), ranges(), weights());
	}

	public BeliefState<F> getBeliefState() {
		return beliefState;
	}

	public MotiveState<L, F> getMotiveState() {
		return motiveState;
	}

	public MotStructureDao getStructure() {
		return getAgent().getComponent(MotStructure.class);
	}

	public Desires getDesires() {
		return getAgent().getComponent(Desires.class);
	}

	protected abstract GenMotiveCouplings<L, F> couplings();

	protected abstract GenWeightRanges<L> ranges();

	protected abstract GenLevelWeights<L> weights();

	protected abstract BeliefState<F> beliefState();

}
