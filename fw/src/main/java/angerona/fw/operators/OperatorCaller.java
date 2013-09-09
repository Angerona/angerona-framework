package angerona.fw.operators;

import angerona.fw.report.Reporter;

/**
 * This interfaces defines what functionality must be provided by a 
 * caller of an operator. The caller must provide an interface to
 * access the report mechanism and access to the operator call stack.
 * 
 * @author Tim Janus
 */
public interface OperatorCaller {
	/** @return configured interface to the Angerona report mechanism */
	Reporter getReporter();
	
	/** @return the operator call stack */
	OperatorStack getStack();
}
