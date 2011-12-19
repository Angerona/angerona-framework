package com.whiplash.gui.events;

import com.whiplash.gui.*;

/** This event occurs when a component loses focus.
 * @author Matthias Thimm
 *
 */
public class WlComponentFocusLostEvent extends WlComponentStatusChangedEvent {
	
	/** Creates a new event triggered by the given component.
	 * @param component a component.
	 */
	public WlComponentFocusLostEvent(WlComponent component){
		super(component);
	}
}
