package com.github.angerona.fw.report;

import com.github.angerona.fw.internal.Entity;
import com.github.angerona.fw.operators.OperatorStack;

/**
 * This interface gives the user of the report mechanism all features. 
 * For a simpler interface to the report mechanism use the Reporter
 * interface.
 * 
 * @see Reporter
 * @author Tim Janus
 */
public interface FullReporter extends Reporter {
	/**
	 * sends a message to the report interface but uses the given
	 * report poster instead the default one.
	 * @param message	The String containing the message.
	 * @param poster	Reference to the report poster used to post this report.
	 */
	void report(String message, ReportPoster poster);
	
	/**
	 * sends a message with the given entity as an attachment to the report interface 
	 * but uses the given report poster instead the default one.
	 * @param message		The String containing the message.
	 * @param attachment	Reference to the entity used as an attachment.
	 * @param poster		Reference to the report poster used to post this report.
	 */
	void report(String message, Entity attachment, ReportPoster poster);
	
	/**
	 * Changes the default report poster.
	 * @param defaultPoster	Reference to the new default report poster.
	 */
	void setDefaultPoster(ReportPoster defaultPoster);
	
	void setOperatorStack(OperatorStack stack);
}
