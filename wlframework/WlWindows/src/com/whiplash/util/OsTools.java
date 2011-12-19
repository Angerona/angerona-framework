package com.whiplash.util;

/**
 * This class provides some auxiliary methods for operating system properties.
 * @author Matthias Thimm
 *
 */
public abstract class OsTools {
	
	/** Returns the operating system name.
	 * @return the operating system name.
	 */
	private static String getOsName() {
		return System.getProperty("os.name");
	}

	/** Whether the current OS is a windows.
	 * @return "true" iff the current OS is a windows.
	 */
	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}

	/** Whether the current OS is a unix.
	 * @return "true" iff the current OS is a unix.
	 */
	public static boolean isUnix() {
		return getOsName().startsWith("Unix");
	}

	/** Whether the current OS is a Mac OS.
	 * @return "true" iff the current OS is a Mac OS.
	 */
	public static boolean isMacOS() {
		return getOsName().startsWith("Mac");
	}
}
