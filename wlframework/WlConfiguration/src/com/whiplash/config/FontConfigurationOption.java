package com.whiplash.config;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.whiplash.gui.laf.*;
import com.whiplash.res.*;

/** This configuration option stores a font family and font size.
 *  @author Matthias Thimm
 */
public class FontConfigurationOption extends SerializableObjectConfigurationOption {

	/** The possible font sizes. */
	public static final Integer[] FONT_SIZES = {9,10,11,12,13,14,18,24,36,48,64,72,96,144,288};
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,,com.whiplash.res.WlText)
	 */
	public FontConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public FontConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public FontConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public FontConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.SerializableObjectConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value) {
		if(!super.isValid(value))
			return false;
		if(!(value instanceof java.util.List<?>))
			return false;		
		if(!(((java.util.List<?>)value).get(0) instanceof String))
			return false;
		if(!(((java.util.List<?>)value).get(1) instanceof Integer))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.SerializableObjectConfigurationOption#getValue()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public java.util.List<Object> getValue() {
		return (java.util.List<Object>)super.getValue();
	}
	
	/** Returns the font of this configuration option.
	 * @return the font of this configuration option.
	 */
	public Font getFont(){
		java.util.List<Object> value = this.getValue();
		Font theFont = Font.decode(value.get(0) + "-" + value.get(1));		
		return theFont;
	}
	
	/** Sets the font of this option.
	 * @param font some font.
	 */
	public void setFont(Font font){
		java.util.List<Object> value = new java.util.ArrayList<Object>();
		value.add(font.getName());
		value.add(font.getSize());
		this.putValue(value);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.SerializableObjectConfigurationOption#getActionComponent()
	 */
	@Override
	public JComponent getActionComponent() {
		Font currentFont = this.getFont();
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		int selectedIndex = 0;
		for(int i = 0; i < fonts.length; i++)
			if(fonts[i].equals(currentFont.getName())){
				selectedIndex = i;
				break;
			}
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(WlLookAndFeel.COLOR_BACKGROUND_DEFAULT);
		final JComboBox fontBox = new JComboBox(fonts);
		fontBox.setSelectedIndex(selectedIndex);
		fontBox.setFocusable(false);				
		final JComboBox sizeBox = new JComboBox(FontConfigurationOption.FONT_SIZES);
		sizeBox.setSelectedItem(new Integer(currentFont.getSize()));
		sizeBox.setFocusable(false);
		final FontConfigurationOption confOption = this;		
		ActionListener listener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				java.util.List<Object> value = new java.util.ArrayList<Object>();
				value.add((String)fontBox.getSelectedItem());
				value.add((Integer)sizeBox.getSelectedItem());
				confOption.putValue(value);				
			}
			
		};
		fontBox.addActionListener(listener);
		sizeBox.addActionListener(listener);		
		panel.add(fontBox,BorderLayout.CENTER);
		panel.add(sizeBox,BorderLayout.EAST);
		return panel;
	}

}
