package angerona.fw.operators;

import angerona.fw.BaseOperator;



/**
 * This is the base class for every user defined operator.
 * @author Tim Janus
 *
 * @param <IN>		Type of the input parameter
 * @param <OUT>		Type of the output (return value)
 */
public abstract class Operator<IN, OUT> extends BaseOperator {
	public OUT process(IN param) {
		getOwner().pushOperator(this);
		OUT reval = processInt(param);
		getOwner().popOperator();
		return reval;
	}
	
	/**
	 *	Adapter for the processing setting internal variables to make
	 *	a good ReportPoster out of an operator.
	 */
	protected abstract OUT processInt(IN param);
}
