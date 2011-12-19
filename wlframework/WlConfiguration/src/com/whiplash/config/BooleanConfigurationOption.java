package com.whiplash.config;

import java.awt.event.*;

import javax.swing.*;

import com.whiplash.events.*;
import com.whiplash.res.*;

/**
 * This class represents a boolean configuration option, i.e. an option
 * with a single boolean as value.
 *  @author Matthias Thimm
 */
public class BooleanConfigurationOption extends ConfigurationOption {

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,,com.whiplash.res.WlText)
	 */
	public BooleanConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public BooleanConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public BooleanConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public BooleanConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value) {		
		return value instanceof Boolean;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#putValue(java.lang.Object)
	 */
	@Override
	public void putValue(Object obj) throws IllegalArgumentException {
		if(!this.isValid(obj))
			throw new IllegalArgumentException("The given value is invalid for this configuration option");
		Object oldValue = this.getValue();
		WlConfiguration.put(this.getId(), (Boolean)obj);
		this.fireConfigurationEvent(new ConfigurationEvent(this,oldValue));
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getValue()
	 */
	@Override
	public Boolean getValue() {
		return WlConfiguration.getBooleanValue(this.getId(), (Boolean)this.getDefaultValue());
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getActionComponent()
	 */
	@Override
	public JComponent getActionComponent() {
		final JCheckBox checkBox = new JCheckBox();
		checkBox.setSelected(this.getValue());
		final BooleanConfigurationOption confOption = this;
		checkBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				confOption.putValue(checkBox.isSelected());				
			}			
		});
		checkBox.setFocusable(false);
		return checkBox;
	}

}
