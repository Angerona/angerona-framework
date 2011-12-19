package com.whiplash.gui.events;

/**
 * Classes implementing this interface respond to status changes of windows.
 * @author Matthias Thimm 
 */
public interface WlWindowListener {
	
	/**
	 * Informs the listener that some window has been closed.
	 * @param e the event corresponding to the closure.
	 */
	public void windowClosed(WlWindowClosedEvent e);
}
