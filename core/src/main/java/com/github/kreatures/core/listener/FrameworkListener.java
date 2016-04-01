package com.github.kreatures.core.listener;

/**
 * Informs plugins of the KReatures framework about what the framework does.
 * It tells them if an error occurs or if the bootstrapping is done.
 * 
 * @author Tim Janus
 */
public interface FrameworkListener {
	void onBootstrapDone();
	
	/**
	 * is called if an error occurs.
	 * @param errorTitle	a title for identifying the error.
	 * @param errorMessage	The message describing the error.
	 */
	void onError(String errorTitle, String errorMessage);
}
