package com.github.angerona.fw.motivation.dao;

import java.util.Collection;

import net.sf.tweety.Formula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public interface CouplingDao<L extends MotiveLevel, F extends Formula> {

	public Collection<Motive<L>> getMotives(L level);

	public Collection<Motive<L>> getMotives();

	public Collection<Desire> getDesires();

	public Collection<MotiveCoupling<L, F>> getCouplings(Motive<L> m);

	public Collection<MotiveCoupling<L, F>> getCouplings(Desire d);

	public Collection<MotiveCoupling<L, F>> getCouplings();

	public double getMeanCouplingStrength();

}
