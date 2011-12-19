package com.whiplash.gui.events;

import com.whiplash.gui.*;

/**
 * This class is the abstract ancestor for component status
 * changed events.
 * @author Matthias Thimm
 */
public abstract class WlComponentStatusChangedEvent {
	
	/** The component that triggered this event.  */
	private WlComponent component;
	
	/** Creates a new event triggered by the given component.
	 * @param component a component.
	 */
	public WlComponentStatusChangedEvent(WlComponent component){
		this.component = component;
	}
	
	/** Returns the component that triggered the event.
	 * @return the component that triggered the event.
	 */
	public WlComponent getComponent(){
		return this.component;
	}
}
