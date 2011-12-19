package com.whiplash.gui.events;

import com.whiplash.gui.*;

/**
 * This event occurs when some dragable component (a WlComponent of a
 * WlToolBar) was dragged to a new location. 
 * @author Matthias Thimm
 */
public class WlComponentDraggedEvent extends WlComponentStatusChangedEvent{

	/** The origin window of the move. */
	private WlWindow origin;
	/** The destination window of the move. */
	private WlWindow destination;

	/** Creates a new event with the given parameters.
	 * @param component the component that was moved (either a WlComponent or a WlToolBar).
	 * @param origin the origin window of the move.
	 * @param destination the destination window of the move.
	 */
	public WlComponentDraggedEvent(WlComponent component, WlWindow origin, WlWindow destination){
		super(component);
		this.origin = origin;
		this.destination = destination;
	}

	/** Returns the origin window of the move. 
	 * @return the origin window of the move.
	 */
	public WlWindow getOrigin() {
		return this.origin;
	}
	
	/** Returns the destination window of the move. 
	 * @return the destination window of the move.
	 */
	public WlWindow getDestination() {
		return this.destination;
	}
}
