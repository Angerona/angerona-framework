package angerona.fw.DefendingAgent.comm;

/**
 * Enumeration representing three possible results of a revision request: success, failure and rejection.
 * @author Sebastian Homann, Pia Wierzoch
 *
 */
public enum RevisionAnswerValue {
	/** revision successful */
	AV_SUCCESS,
	
	/** revision failed */
	AV_FAILURE,
	
	/** revision not possible */
	AV_REJECT
	
}
