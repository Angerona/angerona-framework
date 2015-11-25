package com.github.kreatures.gui.util;

import javax.swing.Icon;

/**
 * The default implementation of the UserObjectWrapper interface uses 
 * the toString method of the wrapped object to determine the name in
 * the UI, has no Icon and does nothing when activated.
 * 
 * @author Tim Janus
 */
public class DefaultUserObjectWrapper implements UserObjectWrapper {

	private Object userObject;
	
	/**
	 * Creates a new user object wrapper wrapping the given user object.
	 * @param userObject	This must not be null.
	 */
	public DefaultUserObjectWrapper(Object userObject) {
		if(userObject == null)
			throw new IllegalArgumentException("userObject must not be null.");
		this.userObject = userObject;
	}
	
	@Override
	public Object getUserObject() {
		return userObject;
	}
	
	@Override
	public String toString() {
		return getUserObject().toString();
	}

	@Override
	public Icon getIcon() {
		return null;
	}

	@Override
	public void onActivated() {
		// does nothing
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getUserObjectOfType(Class<T> cls) {
		if(cls.isAssignableFrom(userObject.getClass())) {
			return (T)userObject;
		}
		return null;
	}
}
