package com.github.kreatures.gui.base;

import javax.swing.JPanel;

import com.github.kreatures.gui.UIPlugin;
import com.github.kreatures.gui.docking.DockableDecorator;

/**
 * Base class for KReatures UI Views.
 * 
 * There are several UI Views already defined in the KReatures GUI Extension library. Nevertheless
 * Plugins have the ability to extends this class and register them to the KReatures GUI Extension as
 * new views.
 * 
 * @see UIPlugin
 * @author Tim Janus
 */
public interface ViewComponent extends DockableDecorator {
	JPanel getPanel();
}
