package com.github.kreaturesfw.core.legacy;

import java.util.Map;

public interface SettingsStorage {
	/**
	 * Adds the given settings to the settings of the operator
	 * @param settings	A map containing settings as string-string pairs
	 */
	public void addSettings(Map<String, String> settings);
	
	/**
	 * Replaces the current settings with the given settings
	 * @param settings	The new settings.
	 */
	public void setSettings(Map<String, String> settings);
	/**
	 * Receives the settings with the given name, if the setting does not
	 * exists it is created with the value given in def and this value is
	 * returned.
	 * @param name	The name of the setting
	 * @param def	The default value of the setting
	 * @return		The value of the settings if a setting with the given
	 * 				name exists or def if a setting with the given name
	 * 				does not exists.
	 */
	public String getSetting(String name, String def);
	
	public Map<String, String> getSettings();
	
	public String putSetting(String name, String value);
	
	public String removeSetting(String name);
}
