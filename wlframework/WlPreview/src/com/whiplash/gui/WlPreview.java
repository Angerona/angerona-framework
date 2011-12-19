package com.whiplash.gui;

/**
 * This class is the abstract ancestor for preview components e.g. PDF previews.
 * @author Matthias Thimm
 */
public abstract class WlPreview extends WlComponent {

    /** For serialization.  */
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#getTitle()
	 */
	@Override
	public String getTitle() {
		// TODO
		return null;
	}

}
