package com.whiplash.config;

/**
 * This class provides for some configuration options for the multi-text editor.
 * @author Matthias Thimm
 */
public abstract class WlMteConfigurationOptions {

	/** The configuration option for the default encoding of new files. */
	public static CharsetConfigurationOption CONF_DEFAULT_ENCODING;
	/** The length of the recent files list. */
	public static IntegerConfigurationOption CONF_LENGTH_RECENT_FILES;
	/** The recently opened files. */
	public static FileListQueueConfigurationOption CONF_RECENT_FILES;
	/** The files for the file explorer. */
	public static FileListQueueConfigurationOption CONF_FILE_EXPLORER_FILES;
}
