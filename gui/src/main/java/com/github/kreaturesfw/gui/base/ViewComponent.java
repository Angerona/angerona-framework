package com.github.kreaturesfw.gui.base;

import javax.swing.JPanel;

import com.github.kreaturesfw.gui.UIPlugin;
import com.github.kreaturesfw.gui.docking.DockableDecorator;

/**
 * Base class for Angerona UI Views.
 * 
 * There are several UI Views already defined in the Angerona GUI Extension library. Nevertheless
 * Plugins have the ability to extends this class and register them to the Angerona GUI Extension as
 * new views.
 * 
 * @see UIPlugin
 * @author Tim Janus
 */
public interface ViewComponent extends DockableDecorator {
	JPanel getPanel();
}
