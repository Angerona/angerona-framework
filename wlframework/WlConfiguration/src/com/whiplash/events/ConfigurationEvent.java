package com.whiplash.events;

import com.whiplash.config.*;

/**
 * This event occurs when some configuration option changed its value.
 * @author Matthias Thimm
 */
public class ConfigurationEvent {

	/** The configuration option that triggered the event. */
	private ConfigurationOption option;
	/** The old value of the option. */
	private Object oldValue;
	
	/** Creates a new configuration event.
	 * @param option the configuration option that triggered the event.
	 * @param oldValue the old value of the option.
	 */
	public ConfigurationEvent(ConfigurationOption option, Object oldValue){
		this.option = option;
		this.oldValue = oldValue;
	}
	
	/** Returns the configuration option that triggered the event.
	 * @return the configuration option that triggered the event.
	 */
	public ConfigurationOption getConfigurationOption(){
		return this.option;
	}
	
	/** Returns the old value of the option.
	 * @return the old value of the option.
	 */
	public Object getOldValue(){
		return this.oldValue;
	}
}
