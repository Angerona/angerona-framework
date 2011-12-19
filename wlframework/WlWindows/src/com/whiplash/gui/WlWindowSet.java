package com.whiplash.gui;

import java.awt.*;
import java.util.List;
import java.util.LinkedList;

import javax.swing.*;

import com.explodingpixels.macwidgets.*;
import com.whiplash.control.*;
import com.whiplash.gui.events.*;

/**
 * Controls a set of WlWindows.
 * @author Matthias Thimm
 */
public class WlWindowSet extends LinkedList<WlWindow>{
	
	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** The menu bar of the window set. */
	private JMenuBar menuBar;
	
	/** Listener of component events. */
	private List<WlComponentListener> componentListener; 
	/** Listener of window events. */
	private List<WlWindowListener> windowListener; 
	/** The given controllers are asked for permission when some window or component should be closed. */
	private java.util.List<WlCloserController> closerControllers;
	
	/** Creates a new window set without menu bar. */
	public WlWindowSet(){
		this(null);
	}
	
	/** Creates a new window set with the given menu bar.
	 * @param menuBar a menu bar
	 */
	public WlWindowSet(JMenuBar menuBar){
		super();		
		this.menuBar = menuBar;		
		this.componentListener = new LinkedList<WlComponentListener>();
		this.windowListener = new LinkedList<WlWindowListener>();
		this.closerControllers = new LinkedList<WlCloserController>();
	}
	
	/** Adds the given closer controller.
	 * @param controller a closer controller
	 */
	public void addCloserController(WlCloserController controller){
		this.closerControllers.add(controller);
	}
	
	/** Removes the given closer controller.
	 * @param controller a closer controller.
	 * @return "true" iff the removal was successful.
	 */
	public boolean removeCloserController(WlCloserController controller){
		return this.closerControllers.remove(controller);
	}
	
	/** Activates the given component, i.e. gives it focus.
	 * @param component some component (must be somewhere in this window set)
	 */
	public void activateComponent(WlComponent component){
		component.getParentPane().getWindow().requestFocus();
		component.getParentPane().activateTab(component.getTab());
	}
	
	/** Asks the closer controllers of this window set whether
	 * to allow the given window to be closed.
	 * @param window a window.
	 * @return "true" if the window can be closed.
	 */
	protected boolean requestClosing(WlWindow window){
		for(WlCloserController controller: this.closerControllers)
			if(!controller.requestClosing(window))
				return false;
		return true;
	}
	
	/** Asks the closer controllers of this window set whether
	 * to allow the given component to be closed.
	 * @param component a component.
	 * @return "true" if the component can be closed.
	 */
	protected boolean requestClosing(WlComponent component){
		for(WlCloserController controller: this.closerControllers)
			if(!controller.requestClosing(component))
				return false;
		return true;
	}
	
	/** Adds the given WlComponentListener.
	 * @param listener a WlComponentListener
	 */
	public void addWlComponentListener(WlComponentListener listener){
		this.componentListener.add(listener);
	}
	
	/** Removes the given WlComponentListener.
	 * @param listener a WlComponentListener.
	 * @return "true" iff the removal was successful.
	 */
	public boolean removeWlComponentListener(WlComponentListener listener){
		return this.componentListener.remove(listener);
	}
	
	/** Adds the given WlWindowListener.
	 * @param listener a WlWindowListener
	 */
	public void addWlWindowListener(WlWindowListener listener){
		this.windowListener.add(listener);
	}
	
	/** Removes the given WlWindowListener.
	 * @param listener a WlWindowListener.
	 * @return "true" iff the removal was successful.
	 */
	public boolean removeWlWindowListener(WlWindowListener listener){
		return this.windowListener.remove(listener);
	}
	
	/** Returns the current focused window. 
	 * @return the current focused window.
	 */
	public WlWindow getFocusedWindow(){
		for(WlWindow window: this)
			if(window.isFocused())
				return window;
		return null;
	}
	
	/** Informs the listeners of this object that some component has been dragged.
	 * @param e some event.
	 */
	protected void fireWlComponentDraggedEvent(WlComponentDraggedEvent e){
		for(WlComponentListener listener: this.componentListener)
			listener.componentDragged(e);
	}
	
	/** Informs the listeners of this object that some components title has been changed.
	 * @param e some event.
	 */
	protected void fireWlComponentChangedTitleEvent(WlComponentTitleChangedEvent e){
		for(WlComponentListener listener: this.componentListener)
			listener.componentChangedTitle(e);
		e.getComponent().getTab().refreshTitle();
	}	
	
	/** Informs the listeners of this object that some component gained focus.
	 * @param e some event.
	 */
	protected void fireWlComponentGainedFocusEvent(WlComponentFocusGainedEvent e){
		for(WlComponentListener listener: this.componentListener)
			listener.componentGainedFocus(e);
	}
	
	/** Informs the listeners of this object that some component lost focus.
	 * @param e some event.
	 */
	protected void fireWlComponentFocusLostEvent(WlComponentFocusLostEvent e){
		for(WlComponentListener listener: this.componentListener)
			listener.componentLostFocus(e);
	}

	/** Informs the listeners of this object that some component has been closed.
	 * @param e some event.
	 */
	protected void fireWlComponentClosedEvent(WlComponentClosedEvent e){
		for(WlComponentListener listener: this.componentListener)
			listener.componentClosed(e);
	}

	/** Informs the listeners of this object that some component lost focus.
	 * @param e some event.
	 */
	protected void fireWlComponentActionEnablementEvent(WlComponentActionEnablementChangedEvent e){
		for(WlComponentListener listener: this.componentListener)
			listener.componentActionEnablementChanged(e);
	}
	
	/** Informs the listeners of this object that some window has been closed.
	 * @param e some event.
	 */
	protected void fireWlWindowClosedEvent(WlWindowClosedEvent e){
		for(WlWindowListener listener: this.windowListener)
			listener.windowClosed(e);
	}
	
	/** Creates a new window at the given coordinates.
	 * @param title the title for the new window.
	 * @param coordinates some coordinates.
	 * @return a window.
	 */
	public WlWindow createWindow(String title, Point coordinates){
		WlWindow window = this.createWindow(title);
		window.setLocation(coordinates);
		return window;
	}
	
	/**
	 * Creates a new window and centers it on the screen.
	 * @param title the title for the new window.
	 * @return a window
	 */
	public WlWindow createWindow(String title){
		WlWindow window = new WlWindow(this,title);
		MacUtils.makeWindowLeopardStyle(window.getRootPane());
		window.setJMenuBar(this.menuBar);
		this.add(window);
		window.setLocationRelativeTo(null);
		return window;
	}
		
	/** Checks whether the given point (describing coordinates
	 * on screen) lies outside of every window.
	 * @param p a point
	 * @return "true" iff the given point lies outside of every window.
	 */
	protected boolean isOutsideAnyWindow(Point p){
		for(WlWindow w: this)
			if(WlWindowSet.isInComponent(p, w)) return false;
		return true;
	}
	
	/** Returns the window that contains the given point, or
	 * null, iff there is no such window.
	 * @param p a point
	 * @return a window.
	 */
	protected WlWindow getWindowOfLocation(Point p){
		for(WlWindow w: this)
			if(WlWindowSet.isInComponent(p, w)) return w;
		return null;
	}
	
	/** Under Mac OS X the menu bar has to be reassigned to windows
	 * which gain focus. This is done in this method. 
	 * @param window a window.
	 */
	protected void requestMenuBar(WlWindow window){
		if(!this.menuBar.equals(window.getJMenuBar())){
			window.setJMenuBar(this.menuBar);
			window.validate();
		}
	}
	
	/** Checks whether the given point (describing coordinates
	 * on screen) lies in the given component.
	 * @param p a point
	 * @param comp a component
	 * @return "true" iff the given point lies in the given component.
	 */
	protected static boolean isInComponent(Point p, Component comp){		
		return comp.getLocationOnScreen().x <= p.x 
			&& comp.getLocationOnScreen().y <= p.y
			&& comp.getLocationOnScreen().x + comp.getWidth() >= p.x
			&& comp.getLocationOnScreen().y + comp.getHeight() >= p.y;
	}
}
