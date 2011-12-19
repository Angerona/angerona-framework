package com.whiplash.gui.events;

import com.whiplash.gui.*;

/** This event occurs when a component has been closed.
 * @author Matthias Thimm
 *
 */
public class WlComponentClosedEvent extends WlComponentStatusChangedEvent {
	
	/** The window in which a component was closed. The window has to be saved
	 * separately as the component loses a reference to its former window. */
	private WlWindow window;
	
	/** Creates a new event triggered by the given component.
	 * @param component a component.
	 */
	public WlComponentClosedEvent(WlComponent component, WlWindow window){
		super(component);
		this.window = window;
	}
	
	/** Returns the window in which the focus lost occurred.
	 * @return the window in which the focus lost occurred.
	 */
	public WlWindow getWindow(){
		return this.window;
	}	
}
