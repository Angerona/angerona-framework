package com.github.kreatures.core.internal;

import com.github.kreatures.core.KReatures;
import com.github.kreatures.core.operators.OperatorStack;
import com.github.kreatures.core.report.FullReporter;
import com.github.kreatures.core.report.ReportPoster;

/**
 * This implementation of the report interface directly delegates the
 * calls to the KReatures report mechanism. 
 * 
 * @author Tim Janus
 */
public class KReaturesReporter implements FullReporter {

	/** reference to object providing a stack of operators */
	private OperatorStack stack;
	
	/** the default poster */
	private ReportPoster defaultPoster;

	/** 
	 * Ctor: 	Creating an unitialized reporter. To use it the setOperatorStack
	 * 			and setDefaultPoster must be invoked.
	 */
	public KReaturesReporter() {
	}
	
	/**
	 * Ctor: Initializes the used operator stack and the default poster.
	 * @param stack
	 * @param defaultPoster
	 */
	public KReaturesReporter(OperatorStack stack, ReportPoster defaultPoster) {
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
			KReatures.getInstance().report(message, stack, defaultPoster);
	}
	
	@Override
	public void report(String message, Entity attachment) {
		if(stack != null && defaultPoster != null)
			KReatures.getInstance().report(message, attachment, stack, defaultPoster);
	}

	@Override
	public void report(String message, ReportPoster poster) {
		if(stack != null)
			KReatures.getInstance().report(message, stack, poster);
	}
	
	@Override
	public void report(String message, Entity attachment, ReportPoster poster) {
		if(stack != null)
			KReatures.getInstance().report(message, attachment, stack, poster);
	}

}
