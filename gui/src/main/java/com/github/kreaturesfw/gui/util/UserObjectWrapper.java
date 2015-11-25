package com.github.kreaturesfw.gui.util;

import javax.swing.Icon;

/**
 * A user object wrapper is used to save user objects in UI elements
 * like JList, JTree or JTable. As the name said it wraps an user
 * object which is saved at a specific position of a UI element (like
 * in a tree node). When the UI element containing the user object is double
 * clicked for example then the UserObjectWrapper onActivated method fires. 
 * A UserObjectWrapper returns an Icon, a name by using the toString method
 * and a reference to the object it is wrapping.
 * 
 * @see CollectionMonitor
 * @author Tim Janus
 */
public interface UserObjectWrapper {
	
	<T> T getUserObjectOfType(Class<T> cls);
	
	/** @return the user object casted to type T, if the caller uses a wrong type T then a ClassCastException is thrown */
	Object getUserObject();
	
	/** @return a string representing the name label in the UI */
	String toString();
	
	/** @return An icon which can be placed in the ui before the user object, this might be null. */
	Icon getIcon();
	
	/** 
	 * Event method called if the ui element containing the user object is activated
	 * by a double click for example.
	 */
	void onActivated();
}
