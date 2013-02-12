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
	 * Ctor: Initializes the used operator stack and the default poster.
	 * @param stack
	 * @param defaultPoster
	 */
	public AngeronaReporter(OperatorStack stack, ReportPoster defaultPoster) {
		if(stack == null)
			throw new IllegalArgumentException("The stack most not be null.");
		
		this.stack = stack;
		setDefaultPoster(defaultPoster);
	}
	
	@Override
	public void setDefaultPoster(ReportPoster defaultPoster) {
		if(defaultPoster == null)
			throw new IllegalArgumentException("The defaultPoster most not be null.");
		this.defaultPoster = defaultPoster;
	}
	
	@Override
	public void report(String message) {
		Angerona.getInstance().report(message, stack, defaultPoster);
	}
	
	@Override
	public void report(String message, Entity attachment) {
		Angerona.getInstance().report(message, attachment, stack, defaultPoster);
	}

	@Override
	public void report(String message, ReportPoster poster) {
		Angerona.getInstance().report(message, stack, poster);
	}
	
	@Override
	public void report(String message, Entity attachment, ReportPoster poster) {
		Angerona.getInstance().report(message, attachment, stack, poster);
	}

}
