package com.whiplash.config;

import com.whiplash.res.*;

/**
 * This abstract class is the common ancestor for configuration category
 * and configuration option and comprises of common methods and attributes.
 * 
 * @author Matthias Thimm
 *
 */
public abstract class ConfigurationObject {
	
	/** Static constant for a configuration object to be visible. */
	public final static int IS_VISIBLE = 0;	
	/** Static constant for a configuration object to be invisible. */
	public final static int IS_INVISIBLE = 1;	
	/** Static constant for a configuration object to be editable. */
	public final static int IS_EDITABLE = 0;	
	/** Static constant for a configuration object to be not editable. */
	public final static int IS_NOT_EDITABLE = 1;
	
	/** The id of this configuration object (machine readable); */
	private String id;	
	/** The caption of this configuration object (human readable). */
	private WlText caption;	
	/** Determines whether this configuration object is visible
	 * in the preferences dialog and the console. */
	private int visible;	
	/** Determines whether this configuration object is editable
	 *  in the preferences dialog. */
	private int editable;	
	
	/** Creates a new configuration object with the given
	 * caption and id, that is both visible and editable. 
	 * @param id The id of this configuration object.
	 * @param caption The caption of this configuration object
	 */
	public ConfigurationObject(String id, WlText caption){
		this(id,caption,IS_VISIBLE,IS_EDITABLE);
	}
	
	/** Creates a new configuration object with the given id,
	 * caption, the given visibility, and the given editibility
	 * @param id The id of this configuration object.
	 * @param caption The caption of this configuration object.
	 * @param visible whether this configuration object is visible (one of ConfigurationObject.IS_VISIBLE, ConfigurationObject.IS_INVISIBLE).
	 * @param editable whether this configuration object is editable  (one of ConfigurationObject.IS_EDITABLE, ConfigurationObject.IS_NOT_EDITABLE).
	 */
	public ConfigurationObject(String id, WlText caption, int visible, int editable){
		this.id = id;
		this.caption = caption;
		this.visible = visible;
		this.editable = editable;		
	}
		
	/** Returns the id of this configuration object.
	 * @return the id of this configuration object. */
	public String getId(){
		return this.id;
	}
	
	/** Returns the localized caption of this configuration object.
	 * @return the localized caption of this configuration object. */
	public String getLocalizedCaption(){
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");		
		return WlResourceManager.getDefaultResourceManager().getLocalizedText(this.caption);
	}
		
	/** Checks whether this configuration object is visible.
	 * @return "TRUE" if this configuration object is visible, "FALSE" otherwise. */
	public boolean isVisible(){
		return this.visible == IS_VISIBLE;
	}
	
	/** Checks whether this configuration object is editable.
	 * @return "TRUE" if this configuration object is editable, "FALSE" otherwise. */
	public boolean isEditable(){
		return this.editable == IS_EDITABLE;
	}
}
