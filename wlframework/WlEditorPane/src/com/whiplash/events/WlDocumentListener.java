package com.whiplash.events;

import javax.swing.event.*;

/**
 * Classes implementing this interface react on document remove events.
 * @author Matthias Thimm
 */
public interface WlDocumentListener extends DocumentListener {

	/** Informs the listener that some text has been removed.
	 * @param e some event.
	 */
	public void removeUpdate(WlDocumentRemoveEvent e);
}
