package com.github.angerona.fw.gui.base;

import javax.swing.JComponent;

import com.github.angerona.fw.util.MapObserver;
import com.github.angerona.fw.util.PropertyObserver;

/**
 * Acts as base interface for views in the MVP pattern. It extends
 * the Observer interfaces to allow the monitoring of maps.
 * @author Tim Janus
 */
public interface View extends PropertyObserver, MapObserver{
	/** 
	 * @return 	The component which acts as root for the view, this is useful 
	 * 			if one want to show a message box for example.
	 */
	JComponent getRootComponent();
}
