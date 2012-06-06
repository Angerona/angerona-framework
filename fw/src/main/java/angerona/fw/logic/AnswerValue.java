package angerona.fw.logic;

/**
 * Enumeration representing the four significant answers used by the Angerona environment.
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
	
	/** answer is a list of arbitrary elements (a list of one arbitrary element in case of an arbitrary answer) */
	AV_LIST
}
