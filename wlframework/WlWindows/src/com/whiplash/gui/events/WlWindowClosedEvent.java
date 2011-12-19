package com.whiplash.gui.events;

import com.whiplash.gui.*;

/**
 * This event occurs when a window has been closed.
 * @author Matthias Thimm
 */
public class WlWindowClosedEvent {
	
	/** The component that triggered this event.  */
	private WlWindow window;
	
	/** Creates a new event triggered by the given window.
	 * @param window a window.
	 */
	public WlWindowClosedEvent(WlWindow window){
		this.window = window;
	}
	
	/** Returns the window that triggered the event.
	 * @return the window that triggered the event.
	 */
	public WlWindow getWindow(){
		return this.window;
	}
}
