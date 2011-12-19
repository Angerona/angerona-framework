package com.whiplash.config;

import java.util.*;

import com.whiplash.res.*;

/**
 * This class represents a string selection configuration option, i.e. an option
 * with a single string as value, which can only be chosen from a fixed set of
 * strings.
 *  @author Matthias Thimm
 */
public class StringSelectionConfigurationOption extends StringConfigurationOption {
	/** The list of selectable values for this option. */
	protected List<String> selectableValues = new ArrayList<String>();
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText)
	 */
	public StringSelectionConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public StringSelectionConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public StringSelectionConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public StringSelectionConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}
	
	/** Adds a new selectable value to this option.
	 * @param value a string. */
	public void addSelectableValue(String value){
		this.selectableValues.add(value);
	}
	
	/** Returns the list of selectable values for this option.
	 * @return the list of selectable values for this option. */
	public List<String> getSelectableValues(){
		return this.selectableValues;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value){
		if(!super.isValid(value)) return false;
		if(!(this.selectableValues.contains(value))) return false;
		return true;
	}	
}
