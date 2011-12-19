package com.whiplash.config;

import java.util.*;

import com.whiplash.res.*;

/**
 * This class encapsulates a set of configuration options 
 * under a common category. 
 * @author Matthias Thimm
 *
 */
public class ConfigurationCategory extends ConfigurationObject{		
	
	/** The options of this configuration category. */
	private List<ConfigurationOption> options;	
	
	/** The icon for the category*/
	private WlIcon icon;
	
	/**
	 * Creates a new configuration category with the given
	 * caption and id, and empty options that is both visible and editable.
	 * @param id The id of this category.
	 * @param icon The icon of this category.
	 * @param caption The caption of this category
	 */
	public ConfigurationCategory(String id, WlText caption, WlIcon icon){
		super(id,caption);
		this.icon = icon;
		this.options = new ArrayList<ConfigurationOption>();		
	}
		
	/** Creates a new configuration category with the given
	 * caption,the given visibility, and the given editibility,
	 * empty sub-categories.
	 * @param id The id of this configuration category.
	 * @param icon The icon of this category.
	 * @param caption The caption of this configuration category.
	 * @param visible whether this configuration category is visible (one of ConfigurationObject.IS_VISIBLE, ConfigurationObject.IS_INVISIBLE)..
	 * @param editable whether this configuration category is editable (one of ConfigurationObject.IS_EDITABLE, ConfigurationObject.IS_NOT_EDITABLE).
	 */
	public ConfigurationCategory(String id, WlText caption, WlIcon icon, int visible, int editable){
		super(id,caption,visible,editable);
		this.icon = icon;
		this.options = new ArrayList<ConfigurationOption>();		
	}
	
	/** Returns the options of this configuration category.
	 * @return the options of this configuration category. */
	public List<ConfigurationOption> getOptions(){
		return this.options;
	}
	
	/** Returns the icon of this category.
	 * @return the icon of this category.
	 */
	public WlIcon getIcon(){
		return this.icon;
	}
		
	/** Adds the given configuration option to this configuration category.
	 * @param option a configuration option. */
	public void addOption(ConfigurationOption option){
		option.setParent(this);
		this.options.add(option);
	}	
}
