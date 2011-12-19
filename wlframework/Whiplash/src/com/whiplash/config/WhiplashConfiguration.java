package com.whiplash.config;

import java.io.*;
import java.util.*;
import java.util.List;

import com.whiplash.doc.*;
import com.whiplash.res.*;

/**
 * This class encapsulates the initialization of the configuration.
 * @author Matthias Thimm
 */
public abstract class WhiplashConfiguration {
	
	/**
	 * @return the "general" configuration category. 
	 */
	private static ConfigurationCategory getGeneralConfig(){
		ConfigurationCategory cat = new ConfigurationCategory("general",WlText.GENERAL,WlIcon.PREFERENCES_GENERAL);
		// default encoding
		WlMteConfigurationOptions.CONF_DEFAULT_ENCODING = new CharsetConfigurationOption("default_encoding",WlText.PREF_TITLE_DEFAULT_ENCODING);
		WlMteConfigurationOptions.CONF_DEFAULT_ENCODING.setDefaultValue(WlCharset.WESTERN_MACOSROMAN);
		cat.addOption(WlMteConfigurationOptions.CONF_DEFAULT_ENCODING);
		// default line wrap setting
		WlEditorPaneConfigurationOptions.CONF_LINE_WRAP = new BooleanConfigurationOption("default_linewrap",WlText.PREF_LINE_WRAP);
		WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.setDefaultValue(true);
		cat.addOption(WlEditorPaneConfigurationOptions.CONF_LINE_WRAP);
		// length of recent files list
		WlMteConfigurationOptions.CONF_LENGTH_RECENT_FILES = new IntegerConfigurationOption("length_recent_file_list",WlText.PREF_LENGTH_RECENT_FILES,ConfigurationOption.IS_INVISIBLE,ConfigurationOption.IS_EDITABLE);
		WlMteConfigurationOptions.CONF_LENGTH_RECENT_FILES.setDefaultValue(10);
		cat.addOption(WlMteConfigurationOptions.CONF_LENGTH_RECENT_FILES);
		// recent files list
		WlMteConfigurationOptions.CONF_RECENT_FILES = new FileListQueueConfigurationOption("recent_files",WlText.PREF_RECENT_FILES,ConfigurationOption.IS_INVISIBLE,ConfigurationOption.IS_EDITABLE);
		WlMteConfigurationOptions.CONF_RECENT_FILES.setLength(WlMteConfigurationOptions.CONF_LENGTH_RECENT_FILES.getValue());
		WlMteConfigurationOptions.CONF_RECENT_FILES.setDefaultValue(new LinkedList<File>());
		cat.addOption(WlMteConfigurationOptions.CONF_RECENT_FILES);
		// file explorer files list
		WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES = new FileListQueueConfigurationOption("explorer_files",WlText.PREF_RECENT_FILES,ConfigurationOption.IS_INVISIBLE,ConfigurationOption.IS_EDITABLE);
		WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES.setDefaultValue(new LinkedList<File>());
		cat.addOption(WlMteConfigurationOptions.CONF_FILE_EXPLORER_FILES);
		return cat;
	}
	
	/**
	 * @return the "type setting" configuration category. 
	 */
	private static ConfigurationCategory getTypesettingConfig(){
		return new ConfigurationCategory("typesetting",WlText.TYPESETTING,WlIcon.PREFERENCES_TYPESETTING);
	}	
	
	/**
	 * @return the "Fonts & Colors" configuration category. 
	 */
	private static ConfigurationCategory getFontsAndColorsConfig(){
		ConfigurationCategory cat = new ConfigurationCategory("fontsandcolors",WlText.FONTS_AND_COLORS,WlIcon.PREFERENCES_FONTS_AND_COLORS); 
		// default font
		WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT = new FontConfigurationOption("display_font",WlText.PREF_DISPLAY_FONT,ConfigurationOption.IS_VISIBLE,ConfigurationOption.IS_EDITABLE);
		List<Object> defaultValue = new ArrayList<Object>();
		defaultValue.add("Helvetica");
		defaultValue.add(new Integer(12));
		WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT.setDefaultValue(defaultValue);
		cat.addOption(WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT);
		return cat;
	}
	
	/** Performs initialization of the configuration. */
	public static void fillConfiguration(){
		// add configuration categories		
		WlConfiguration.addConfigurationCategory(WhiplashConfiguration.getGeneralConfig());
		WlConfiguration.addConfigurationCategory(WhiplashConfiguration.getFontsAndColorsConfig());
		WlConfiguration.addConfigurationCategory(WhiplashConfiguration.getTypesettingConfig());
	}
}
