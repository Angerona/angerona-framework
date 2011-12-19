package com.whiplash.gui;

/**
 * Classes implementing this interface are action trigger (eg. tool bars, menu bars,
 * popup menus) for some controller.
 * @author Matthias Thimm
 */
public interface WlActionTrigger {

	/** Sets the enable status of the given action command (ie. enables/disables the corresponding
	 * GUI elements such as menu items or buttons). If there is no GUI element for the given action
	 * command nothing happens.
	 * @param actionCommand some action command.
	 * @param value "true" if the command is to be enabled, "false" if it is to be disabled.
	 * @param obj some object that may contain further information (depending on the action command).
	 */
	public void setEnabled(String actionCommand, boolean value, Object obj);
}
