package angerona.fw.operators;

/**
 * This is the base class for every user defined operator.
 * @author Tim Janus
 *
 * @param <IN>		Type of the input parameter
 * @param <OUT>		Type of the output (return value)
 */
public abstract class Operator<IN, OUT> {
	/**
	 * Performs the processing of this operator instance.
	 * @param param 	input parameter to process
	 * @return			the result of the processing
	 */
	public abstract OUT process(IN param);
}
