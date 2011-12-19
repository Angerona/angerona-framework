package com.whiplash.config;

import java.util.*;
import java.util.prefs.*;

/**
 * This class provides access to a configuration.
 * @author Matthias Thimm
 */
public abstract class WlConfiguration {

	/** An id referring to the application. */
	private static String applicationId;
	/** A handle to the actual preferences object. */
	private static Preferences preferences;
	
	/** The configuration categories. */
	private static List<ConfigurationCategory> configurationCategories;
	
	/** Inits the configuration. */
	public static void init(String applicationId){
		WlConfiguration.applicationId = applicationId;
		WlConfiguration.preferences = java.util.prefs.Preferences.userRoot();		
		WlConfiguration.configurationCategories = new LinkedList<ConfigurationCategory>();
	}
	
	/** Saves the given key/value combination.
	 * @param key a key.
	 * @param value some value. */
	protected static void put(String key, String value){
		WlConfiguration.preferences.put(WlConfiguration.applicationId + "." + key, value);
		try {
			WlConfiguration.preferences.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}

	/** Saves the given key/value combination.
	 * @param key a key.
	 * @param value some value. */
	protected static void put(String key, byte[] value){
		WlConfiguration.preferences.putByteArray(WlConfiguration.applicationId + "." + key, value);
		try {
			WlConfiguration.preferences.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Saves the given key/value combination.
	 * @param key a key.
	 * @param value some value. */
	protected static void put(String key, Integer value){
		WlConfiguration.preferences.putInt(WlConfiguration.applicationId + "." + key, value);		
		try {
			WlConfiguration.preferences.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Saves the given key/value combination.
	 * @param key a key.
	 * @param value some value. */
	protected static void put(String key, Boolean value){
		WlConfiguration.preferences.putBoolean(WlConfiguration.applicationId + "." + key, value);		
		try {
			WlConfiguration.preferences.flush();
		} catch (BackingStoreException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** Returns the value of the given key.
	 * @param key some key.
	 * @param defaultValue a default value that will be returned if the key is not set.
	 * @return the value of the key
	 */
	protected static byte[] getByteArrayValue(String key, byte[] defaultValue){
		return WlConfiguration.preferences.getByteArray(WlConfiguration.applicationId + "." + key, defaultValue);
	}
	
	/** Returns the value of the given key.
	 * @param key some key.
	 * @param defaultValue a default value that will be returned if the key is not set.
	 * @return the value of the key
	 */
	protected static String getStringValue(String key, String defaultValue){		
		return WlConfiguration.preferences.get(WlConfiguration.applicationId + "." + key, defaultValue);
	}
	
	/** Returns the value of the given key.
	 * @param key some key.
	 * @param defaultValue a default value that will be returned if the key is not set.
	 * @return the value of the key
	 */
	protected static Boolean getBooleanValue(String key, Boolean defaultValue){		
		return WlConfiguration.preferences.getBoolean(WlConfiguration.applicationId + "." + key, defaultValue);
	}
	
	/** Returns the value of the given key.
	 * @param key some key.
	 * @param defaultValue a default value that will be returned if the key is not set.
	 * @return the value of the key
	 */
	protected static Integer getIntegerValue(String key, Integer defaultValue){		
		return WlConfiguration.preferences.getInt(WlConfiguration.applicationId + "." + key, defaultValue);
	}
	
	/** Adds the given configuration category.
	 * @param category some configuration category. */
	public static void addConfigurationCategory(ConfigurationCategory category){
		WlConfiguration.configurationCategories.add(category);
	}
	
	/** Returns the configuration categories of this configuration.
	 * @return the configuration categories of this configuration. */
	public static List<ConfigurationCategory> getConfigurationCategories(){
		return WlConfiguration.configurationCategories;
	}
	
}
