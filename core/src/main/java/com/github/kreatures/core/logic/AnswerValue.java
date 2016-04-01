package com.github.kreatures.core.logic;

/**
 * Enumeration representing the four significant answers used by the KReatures environment.
 * @author Tim Janus
 */
public enum AnswerValue {
	/** answer is true */
	AV_TRUE,
	
	/** answer is false */
	AV_FALSE,
	
	/** answer is not known */
	AV_UNKNOWN,
	
	/** answer is not given */
	AV_REJECT,
	
	/** 
	 * answer is for an open-ended question. Signals the 
	 * program to go through additional reasoning and 
	 * answering processes. 
	 */
	AV_COMPLEX;
	
	public String toString() {
		return name().toString();
	}
}
