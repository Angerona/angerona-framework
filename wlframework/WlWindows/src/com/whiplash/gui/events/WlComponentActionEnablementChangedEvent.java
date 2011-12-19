package com.whiplash.gui.events;

import com.whiplash.gui.*;

/**
 * This event occurs when the enable status of some actions on the component changed.
 * @author Matthias Thimm
 */
public class WlComponentActionEnablementChangedEvent extends WlComponentStatusChangedEvent{
	
	/** Creates a new event triggered by the given component.
	 * @param component a component.
	 */
	public WlComponentActionEnablementChangedEvent(WlComponent component){
		super(component);
	}
}
