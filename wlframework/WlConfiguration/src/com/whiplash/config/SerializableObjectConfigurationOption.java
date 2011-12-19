package com.whiplash.config;

import java.io.*;

import javax.swing.*;

import com.whiplash.events.*;
import com.whiplash.res.*;

/**
 * This class represents a general serializable object configuration option, i.e. an option
 * with a some seralizable object as value.
 *  @author Matthias Thimm
 */
public abstract class SerializableObjectConfigurationOption extends ConfigurationOption {
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText)
	 */
	public SerializableObjectConfigurationOption(String id, WlText caption){
		super(id,caption);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int)
	 */
	public SerializableObjectConfigurationOption(String id, WlText caption, int visible, int editable){
		super(id,caption,visible,editable);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int)
	 */
	public SerializableObjectConfigurationOption(String id, WlText caption, int requiresRestart){
		super(id,caption,requiresRestart);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationObject#ConfigurationObject(java.lang.String,com.whiplash.res.WlText,int,int,int)
	 */
	public SerializableObjectConfigurationOption(String id, WlText caption, int visible, int editable, int requiresRestart){
		super(id,caption,visible,editable,requiresRestart);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#isValid(java.lang.Object)
	 */
	@Override
	public boolean isValid(Object value){
		if(!(value instanceof Serializable)) return false;
		return true;
	}
		
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#putValue(java.lang.Object)
	 */
	@Override
	public void putValue(Object obj) throws IllegalArgumentException{
		if(!this.isValid(obj))
			throw new IllegalArgumentException("The given value is invalid for this configuration option");
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			oos.flush();
		    oos.close();
		    baos.close();
		    Object oldValue = this.getValue();
		    WlConfiguration.put(this.getId(), baos.toByteArray());
		    this.fireConfigurationEvent(new ConfigurationEvent(this,oldValue));
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getValue()
	 */
	@Override
	public Object getValue(){
		byte[] val = WlConfiguration.getByteArrayValue(this.getId(), null);
		if(val == null)
			return this.getDefaultValue();		
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(val);
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}		
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.config.ConfigurationOption#getActionComponent()
	 */
	public abstract JComponent getActionComponent();
}
