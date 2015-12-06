package com.github.kreaturesfw.core.operators;

import com.github.kreaturesfw.core.serialize.ContinuousBeliefOperatorFamilyConfig;

/**
 * This class implements a continuous belief-operator-family iteration 
 * strategy, that divides a range like [0, 1] in ten parts (0.0, 0.1, 0.2, ...)
 * 
 * It is used as standard iterator by the {@link ContinuousBeliefOperatorFamily}.
 * 
 * @author Tim Janus
 */
public class StepIteratorStrategy extends
		ContinuousBeliefOperatorFamilyIteratorStrategy {

	/** the size of one step */
	private double step;
	
	@Override
	public void setConfig(ContinuousBeliefOperatorFamilyConfig config) {
		this.config = config;
		step = (config.getMax() - config.getMin()) / 10.0;
	}
	
	@Override
	public OperatorCallWrapper getPredecessor(OperatorCallWrapper current) {
		String setting = current.getSettings().get(config.getParameterName());
		double curPar = Double.parseDouble(setting);
		if(curPar > config.getMin()) {
			curPar -= step;
			OperatorCallWrapper reval = new OperatorCallWrapper(current.getImplementation());
			reval.putSetting(config.getParameterName(), String.valueOf(curPar));
			return reval;
		} else {
			return null;
		}
	}

	@Override
	public OperatorCallWrapper getSuccessor(OperatorCallWrapper current) {
		String setting = current.getSettings().get(config.getParameterName());
		double curPar = Double.parseDouble(setting);
		if(curPar < config.getMax()) {
			curPar += step;
			OperatorCallWrapper reval = new OperatorCallWrapper(current.getImplementation());
			reval.putSetting(config.getParameterName(), String.valueOf(curPar));
			return reval;
		} else {
			return null;
		}
	}
}
