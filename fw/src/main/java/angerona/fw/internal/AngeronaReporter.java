package angerona.fw.internal;

import angerona.fw.Angerona;
import angerona.fw.operators.OperatorStack;
import angerona.fw.report.FullReporter;
import angerona.fw.report.ReportPoster;

/**
 * This implementation of the report interface directly delegates the
 * calls to the Angerona report mechanism. 
 * 
 * @author Tim Janus
 */
public class AngeronaReporter implements FullReporter {

	/** reference to object providing a stack of operators */
	private OperatorStack stack;
	
	/** the default poster */
	private ReportPoster defaultPoster;

	/** 
	 * Ctor: 	Creating an unitialized reporter. To use it the setOperatorStack
	 * 			and setDefaultPoster must be invoked.
	 */
	public AngeronaReporter() {
	}
	
	/**
	 * Ctor: Initializes the used operator stack and the default poster.
	 * @param stack
	 * @param defaultPoster
	 */
	public AngeronaReporter(OperatorStack stack, ReportPoster defaultPoster) {
		setOperatorStack(stack);
		setDefaultPoster(defaultPoster);
	}
	
	@Override
	public void setOperatorStack(OperatorStack stack) {
		if(stack == null)
			throw new IllegalArgumentException("The stack most not be null.");
		this.stack = stack;
	}
	
	@Override
	public void setDefaultPoster(ReportPoster defaultPoster) {
		if(defaultPoster == null)
			throw new IllegalArgumentException("The defaultPoster most not be null.");
		this.defaultPoster = defaultPoster;
	}
	
	@Override
	public void report(String message) {
		if(stack != null && defaultPoster != null)
			Angerona.getInstance().report(message, stack, defaultPoster);
	}
	
	@Override
	public void report(String message, Entity attachment) {
		if(stack != null && defaultPoster != null)
			Angerona.getInstance().report(message, attachment, stack, defaultPoster);
	}

	@Override
	public void report(String message, ReportPoster poster) {
		if(stack != null)
			Angerona.getInstance().report(message, stack, poster);
	}
	
	@Override
	public void report(String message, Entity attachment, ReportPoster poster) {
		if(stack != null)
			Angerona.getInstance().report(message, attachment, stack, poster);
	}

}
