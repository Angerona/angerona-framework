package com.github.angerona.fw.gui.docking;

import bibliothek.gui.dock.DefaultDockable;

/**
 * Classes implementing this interface can decorate Dockables
 * with specific buttons / titles etc.
 * 
 * @author Tim Janus
 */
public interface DockableDecorator {
	/**
	 * Is called to start the decoration of the given dockable
	 * @param dockable	The dockable which gets decorated.
	 */
	void decorate(DefaultDockable dockable);
}
