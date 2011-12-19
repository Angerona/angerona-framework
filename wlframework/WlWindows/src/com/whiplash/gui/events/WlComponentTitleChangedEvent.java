package com.whiplash.gui.events;

import com.whiplash.gui.*;

/**
 * This event occurs when the title of a component changes.
 * @author Matthias Thimm
 */
public class WlComponentTitleChangedEvent extends WlComponentStatusChangedEvent {
	
	/** Creates a new event triggered by the given component.
	 * @param component a component.
	 */
	public WlComponentTitleChangedEvent(WlComponent component){
		super(component);
	}
}
