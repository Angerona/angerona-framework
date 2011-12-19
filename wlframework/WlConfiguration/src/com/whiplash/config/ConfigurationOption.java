package com.whiplash.config;

import java.util.*;

import javax.swing.*;

import com.whiplash.events.*;
import com.whiplash.res.*;

/**
 * This class represents the template for a single configuration option. 
 * @author Matthias Thimm
 */
public abstract class ConfigurationOption extends ConfigurationObject{
	
	/** Static constant for a configuration option that requires a restart. */
	public final static int REQUIRES_RESTART = 0;	
	/** Static constant for a configuration option that requires no restart. */
	public final static int REQUIRES_NO_RESTART = 1;
	
	/** The parent configuration category of this configuration object. */
	private ConfigurationCategory parent;	
	/** The default value for this configuration option. */
	private Object defaultValue;	
	/** Indicates whether this option requires restarting the application after change. */
	private int requiresRestart;
	
	/** A list of configuration listeners. */
	private List<ConfigurationListener> listener = new LinkedList<ConfigurationListener>();
	
	/** Creates a new configuration option with the given
	 * caption, id, and restart flag, that is both visible and editable and has
	 * no parent. 
	 * @param id The id of this configuration object.
	 * @param caption The caption of this configuration object
	 * @param requiresRestart one of ConfigurationOption.REQUIRES_RESTART, ConfigurationOption.REQUIRES_NO_RESTART  
	 */
	public ConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption);
		this.requiresRestart = requiresRestart;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText)
	 */
	public ConfigurationOption(String id, WlText caption){
		this(id,caption,ConfigurationOption.REQUIRES_NO_RESTART);
	}
	
	/**
	 * Creates a new configuration object with the given id,
	 * caption, the given visibility, the given editibility, the given
	 * restart flag. 
	 * @param id The id of this configuration object.
	 * @param caption The caption of this configuration object.
	 * @param visible whether this configuration object is visible (one of ConfigurationObject.IS_VISIBLE, ConfigurationObject.IS_INVISIBLE).
	 * @param editable whether this configuration object is editable  (one of ConfigurationObject.IS_EDITABLE, ConfigurationObject.IS_NOT_EDITABLE).
	 * @param requiresRestart one of ConfigurationOption.REQUIRES_RESTART, ConfigurationOption.REQUIRES_NO_RESTART  
	 */
	public ConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable);
		this.requiresRestart = requiresRestart;
	}
	
	/* (non-Javadoc)
	 * @see edu.cs.ai.kreator.control.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public ConfigurationOption(String id, WlText caption, int visible, int editable){
		this(id,caption,visible,editable,ConfigurationOption.REQUIRES_NO_RESTART);
	}

	/** Adds the given listener to this object's listeners.
	 * @param listener some configuration listener.
	 */
	public void addConfigurationListener(ConfigurationListener listener){
		this.listener.add(listener);
	}
	
	/** Removes the given listener from this object's listeners.
	 * @param listener some configuration listener.
	 */
	public void removeConfigurationListener(ConfigurationListener listener){
		this.listener.remove(listener);
	}
	
	/** Informs this object's listener of a changed event.
	 * @param event some event.
	 */
	protected void fireConfigurationEvent(ConfigurationEvent event){
		for(ConfigurationListener listener: this.listener)
			listener.configurationChanged(event);
	}
	
	/** Sets the parent configuration category of this configuration object.
	 * @param parent a configuration category. */
	protected void setParent(ConfigurationCategory parent){
		this.parent = parent;
	}
	
	/** Returns the parent of this configuration object.
	 * @return the parent of this configuration object.
	 */
	public ConfigurationCategory getParent(){
		return this.parent;
	}
	
	/** Checks whether this configuration option requires restart of the application after change.
	 * @return "true" iff this configuration option requires restart of application after change.
	 */
	public boolean requiresRestart(){
		return this.requiresRestart == ConfigurationOption.REQUIRES_RESTART;
	}
	
	/** Returns the default value of this configuration option.
	 * @return the default value of this configuration option. */
	public Object getDefaultValue(){
		return this.defaultValue;
	}
	
	/** Sets this option's default value
	 * @param defaultValue a default value. */
	public void setDefaultValue(Object defaultValue){
		if(!this.isValid(defaultValue))
			throw new IllegalArgumentException("The default value is invalid for this configuration option.");
		this.defaultValue = defaultValue;
	}
	
	/** Checks whether this configuration option features a default value.
	 * @return "TRUE" if this configuration option features a default value, "FALSE" otherwise. */
	public boolean hasDefaultValue(){
		return this.defaultValue != null;
	}
	
	/** Checks whether the given value object is valid for this option.
	 * @param value a value.
	 * @return "TRUE" if the given value is valid for this option.
	 */
	public abstract boolean isValid(Object value);
	
	/** Saves the new value for this option.
	 * @param obj some object
	 * @throws IllegalArgumentException iff the given object is not valid.
	 */
	public abstract void putValue(Object obj) throws IllegalArgumentException;
	
	/** Gets the value for this option. */
	public abstract Object getValue();
	
	/** Creates a visual component for changing the value of the option.
	 * @return visual component for changing the value of the option.
	 */
	public abstract JComponent getActionComponent();
}
