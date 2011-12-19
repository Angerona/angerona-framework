package com.whiplash.config;

import java.io.*;
import java.util.*;

import javax.swing.*;

import com.whiplash.res.*;

/**
 * This class represents a configuration option for a list of files where new
 * files will be appended. If the size of the list exceeds some threshold
 * old file are removed.
 *  @author Matthias Thimm
 */
public class FileListQueueConfigurationOption extends SerializableObjectConfigurationOption {

	/** The length of the file list. */
	private int length = -1;
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,,com.whiplash.res.WlText)
	 */
	public FileListQueueConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public FileListQueueConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public FileListQueueConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public FileListQueueConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}
	
	/** Sets the maximum length of the file list.
	 * @param length some int.
	 */
	public void setLength(int length){
		this.length = length;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.SerializableObjectConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value) {
		if(!super.isValid(value))
			return false;
		if(!(value instanceof List<?>))
			return false;
		List<?> listValue = (List<?>) value;
		if(this.length != -1)
			if(listValue.size() > this.length)
				return false;
		for(int i = 0; i < listValue.size(); i++)
			if(!(listValue.get(i) instanceof File))
				return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.SerializableObjectConfigurationOption#getValue()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<File> getValue() {
		return (List<File>)super.getValue();
	}

	/** Adds the given file to the value of this option.
	 * @param file some file.
	 */
	public void addValue(File file){
		List<File> value = this.getValue();
		value.remove(file);
		value.add(0, file);
		if(this.length != -1)
			while(value.size() > this.length)
				value.remove(value.size()-1);
		this.putValue(value);
	}
	
	/** Removes the given file to the value of this option.
	 * @param file some file.
	 */
	public void removeValue(File file){
		List<File> value = this.getValue();
		value.remove(file);		
		this.putValue(value);
	}
	
	/** Clears the value of this option. */
	public void clear(){
		this.putValue(new LinkedList<File>());
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.SerializableObjectConfigurationOption#getActionComponent()
	 */
	@Override
	public JComponent getActionComponent() {
		// TODO implement me
		return null;
	}

}
