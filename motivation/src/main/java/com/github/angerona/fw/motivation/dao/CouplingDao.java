package com.github.angerona.fw.motivation.dao;

import java.util.Collection;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * {@link CouplingDao} is a generic class, that delivers access to the set of {@link MotiveCoupling}s of an {@link Agent}.
 * 
 * @see also {@link MotiveCouplings}
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface CouplingDao<L extends MotiveLevel> {

	/**
	 * @param level
	 * @return a subset of all {@link Motive}s included in the {@link MotiveCoupling}s that are mapped to {@link MotiveLevel} level
	 */
	public Collection<Motive<L>> getMotives(L level);

	/**
	 * @return a set of all {@link Motive}s included in the {@link MotiveCoupling}s
	 */
	public Collection<Motive<L>> getMotives();

	/**
	 * @return a set of all {@link Desire}s included in the {@link MotiveCoupling}s
	 */
	public Collection<Desire> getDesires();

	/**
	 * @param m
	 * @return a subset of all {@link MotiveCoupling}s containing {@link Motive} m
	 */
	public Collection<MotiveCoupling<L>> getCouplings(Motive<L> m);

	/**
	 * @param d
	 * @return a subset of all {@link MotiveCoupling}s containing {@link Desire} d
	 */
	public Collection<MotiveCoupling<L>> getCouplings(Desire d);

	/**
	 * @return a set of all MotiveCouplings of an {@link Agent}
	 */
	public Collection<MotiveCoupling<L>> getCouplings();

	/**
	 * @return the mean coupling-strength of {@link MotiveCoupling}s that are coupled greater than zero
	 */
	public double getMeanCouplingStrength();

}
