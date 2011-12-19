package com.whiplash.control;

import java.util.*;

/**
 * This enum stores properties for GUI appearance.
 * @author Matthias Thimm
 */
public enum GuiProperty {
	APPLICATION_NAME			("NO_NAME"),
	STANDARD_TOOLBAR_BUTTONS	(GuiProperty.standardToolBarButtons()),
	SINGLE_TOOLBAR				(true);
		
	private Object defaultValue;	
	GuiProperty(Object defaultValue){ this.defaultValue = defaultValue; }
	
	/** Returns the value of this property iff present in the given list
	 * or otherwise its default value.
	 * @param properties a hash table for properties.
	 * @return the value of the key if present or otherwise the default value. 
	 */
	public Object value(Hashtable<GuiProperty,Object> properties){
		if(properties.containsKey(this))
			return properties.get(this);			
		return this.defaultValue;
	}
	
	/** Returns the default list of tool bar buttons for the standard tool bar (a null object means separator).
	 * @return the default list of tool bar buttons for the standard tool bar (a null object means separator).
	 */
	private static List<String> standardToolBarButtons(){
		List<String> defaultValues = new LinkedList<String>();
		defaultValues.add(GuiActionCommands.ACTION_NEW_FILE);
		defaultValues.add(GuiActionCommands.ACTION_OPEN_FILE);
		return defaultValues;
	}
}
