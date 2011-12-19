package com.whiplash.gui.events;

/**
 * Classes implementing this interface respond to status changes of components.
 * @author Matthias Thimm 
 */
public interface WlComponentListener {

	/**
	 * Informs the listener that the title of some component has changed.
	 * @param e the event corresponding to the title change.
	 */
	public void componentChangedTitle(WlComponentTitleChangedEvent e);
	
	/**
	 * Informs the listener that the some component gained focus.
	 * @param e the event corresponding to the focus gain.
	 */
	public void componentGainedFocus(WlComponentFocusGainedEvent e);

	/**
	 * Informs the listener that the some component lost focus.
	 * @param e the event corresponding to the focus loss.
	 */
	public void componentLostFocus(WlComponentFocusLostEvent e);
	
	/**
	 * Informs the listener that the some component has been dragged in the window set.
	 * @param e the event corresponding to the move.
	 */
	public void componentDragged(WlComponentDraggedEvent e);
	
	/**
	 * Informs the listener that the some component has been closed.
	 * @param e the event corresponding to the closure.
	 */
	public void componentClosed(WlComponentClosedEvent e);
	
	/** Informs the listener the the enable status of some action changed-
	 * @param e the event corresponding to the change.
	 */
	public void componentActionEnablementChanged(WlComponentActionEnablementChangedEvent e);
}
