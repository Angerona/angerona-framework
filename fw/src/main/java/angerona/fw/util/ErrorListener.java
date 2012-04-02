package angerona.fw.util;

/**
 * Informs plugins of the Angerona framework about errors.
 * @author Tim Janus
 */
public interface ErrorListener {
	/**
	 * is called if an error occurs.
	 * @param errorTitle	a title for identifying the error.
	 * @param errorMessage	The message describing the error.
	 */
	void onError(String errorTitle, String errorMessage);
}
