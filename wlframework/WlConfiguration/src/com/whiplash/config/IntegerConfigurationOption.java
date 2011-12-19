package com.whiplash.config;

import javax.swing.*;

import com.whiplash.events.*;
import com.whiplash.res.*;

/**
 * This class represents an integer configuration option, i.e. an option
 * with a single integer as value.
 *  @author Matthias Thimm
 */
public class IntegerConfigurationOption extends ConfigurationOption {

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,,com.whiplash.res.WlText)
	 */
	public IntegerConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public IntegerConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public IntegerConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public IntegerConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value) {
		return value instanceof Integer;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#putValue(java.lang.Object)
	 */
	@Override
	public void putValue(Object obj) throws IllegalArgumentException {
		if(!this.isValid(obj))
			throw new IllegalArgumentException("The given value is invalid for this configuration option");
		Object oldValue = this.getValue();
		WlConfiguration.put(this.getId(), (Integer) obj);
		this.fireConfigurationEvent(new ConfigurationEvent(this,oldValue));
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getValue()
	 */
	@Override
	public Integer getValue() {
		return WlConfiguration.getIntegerValue(this.getId(), (Integer) this.getDefaultValue());
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getActionComponent()
	 */
	@Override
	public JComponent getActionComponent() {
		// TODO implement me
		return null;
	}

}
