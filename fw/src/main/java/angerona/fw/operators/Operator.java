package angerona.fw.operators;

import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.operators.parameter.GenericOperatorParameter;
import angerona.fw.util.ReportPoster;

/**
 * This is the base class for every user defined operator.
 * @author Tim Janus
 *
 * @param <IN>		Type of the input parameter
 * @param <OUT>		Type of the output (return value)
 */
public abstract class Operator<IN extends GenericOperatorParameter, OUT> implements ReportPoster{
	private AngeronaEnvironment environment;
	
	public OUT process(IN param) {
		updateEnvInfo(param);
		return processInt(param);
	}
	
	/**
	 *	Adapter for the processing setting internal variables to make
	 *	a good ReportPoster out of an operator.
	 */
	protected abstract OUT processInt(IN param);
	
	/*
	 * 	Performs the processing of this operator instance.
	 * 	@param param 	input parameter to process
	 * 	@return			the result of the processing
	 */
	private void updateEnvInfo(GenericOperatorParameter gop) {
		environment = gop.getSimulation();
	}
	

	@Override
	public String getPosterName() {
		return this.getClass().getName();
	}

	@Override
	public int getSimTick() {
		return environment.getTick();
	}

	@Override
	public String getSimulationName() {
		return environment.getName();
	}
	
	protected void report(String msg) {
		Angerona.getInstance().report(msg, this);
	}
	
	protected void report(String msg, Object attachment) {
		Angerona.getInstance().report(msg, this, attachment);
	}
}
