package com.whiplash.config;

import javax.swing.*;

import com.whiplash.events.*;
import com.whiplash.res.*;

/**
 * This class represents a string  configuration option, i.e. an option
 * with a single string as value.
 *  @author Matthias Thimm
 */
public class StringConfigurationOption extends ConfigurationOption {
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,,com.whiplash.res.WlText)
	 */
	public StringConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public StringConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public StringConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public StringConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value){
		if(!(value instanceof String)) return false;
		return true;
	}
		
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#putValue(java.lang.Object)
	 */
	@Override
	public void putValue(Object obj) throws IllegalArgumentException{
		if(!this.isValid(obj))
			throw new IllegalArgumentException("The given value is invalid for this configuration option");
		Object oldValue = this.getValue();
		WlConfiguration.put(this.getId(), (String)obj);
		this.fireConfigurationEvent(new ConfigurationEvent(this,oldValue));
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getValue()
	 */
	@Override
	public String getValue(){
		return WlConfiguration.getStringValue(this.getId(), (String)this.getDefaultValue());		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getActionComponent()
	 */
	public JComponent getActionComponent(){
		//TODO implement me
		return null;
	}
}
