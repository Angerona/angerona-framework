package com.whiplash.config;

import java.awt.event.*;

import javax.swing.*;

import com.whiplash.doc.*;
import com.whiplash.res.*;

/**
 * This class models a configuration option for a charset.
 * @author Matthias Thimm
 */
public class CharsetConfigurationOption extends SerializableObjectConfigurationOption {

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText)
	 */
	public CharsetConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public CharsetConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public CharsetConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public CharsetConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value) {
		if(value instanceof WlCharset)
			return true;
		return false;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#putValue(java.lang.Object)
	 */
	@Override
	public void putValue(Object obj) throws IllegalArgumentException {
		if(!this.isValid(obj))
			throw new IllegalArgumentException("The given value is invalid for this configuration option");
		super.putValue(obj);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getValue()
	 */
	@Override
	public WlCharset getValue() {		
		return (WlCharset)super.getValue();
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getActionComponent()
	 */
	public JComponent getActionComponent(){
		String[] values = new String[WlCharset.values().length];
		int selectedIndex = 0;
		for(int i = 0 ; i < WlCharset.values().length; i++){
			values[i] = WlCharset.values()[i].getName();
			if(this.getValue().equals(WlCharset.values()[i]))
				selectedIndex = i;
		}
		final JComboBox comboBox = new JComboBox(values);
		comboBox.setSelectedIndex(selectedIndex);
		final CharsetConfigurationOption confOption = this;
		comboBox.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				confOption.putValue(WlCharset.forName(((String)comboBox.getSelectedItem())));				
			}
			
		});
		comboBox.setFocusable(false);
		return comboBox;
	}
}
