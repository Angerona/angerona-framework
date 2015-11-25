package com.github.kreatures.gui.docking;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import bibliothek.gui.dock.DefaultDockable;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.LocationHint;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

import com.github.kreatures.gui.KReaturesWindow;

/**
 * Decorates a Dockable with a close button.
 * @author Tim Janus
 */
public class CloseButtonDecorator implements DockableDecorator {

	@Override
	public void decorate(final DefaultDockable dockable) {
		DefaultDockActionSource actionSource = new DefaultDockActionSource(
				new LocationHint(LocationHint.DOCKABLE, LocationHint.RIGHT_OF_ALL));
		
		SimpleButtonAction sba = new SimpleButtonAction();
		sba.setText("Close");
		sba.setIcon(KReaturesWindow.get().getIcons().get("close"));
		sba.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dockable.getDockParent().drag(dockable);
			}
		});
		actionSource.add(sba);
		
		dockable.setActionOffers(actionSource);
	}

}
