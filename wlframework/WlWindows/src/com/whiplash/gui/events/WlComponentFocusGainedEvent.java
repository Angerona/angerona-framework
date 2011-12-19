package com.whiplash.gui.events;

import com.whiplash.gui.*;

/** This event occurs when a component gains focus.
 * @author Matthias Thimm
 *
 */
public class WlComponentFocusGainedEvent extends WlComponentStatusChangedEvent {
	
	/** Creates a new event triggered by the given component.
	 * @param component a component.
	 */
	public WlComponentFocusGainedEvent(WlComponent component){
		super(component);
	}
}
