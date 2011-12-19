package com.whiplash.control;

import com.whiplash.gui.*;

/**
 * Classes implementing this interface control window and
 * component closing.
 * @author Matthias Thimm
 */
public interface WlCloserController {

	/** 
	 * This method is invoked when the given component should be closed in
	 * order to perform some cleanup operations.
	 * @return "true" if the given component can be closed.
	 */
	public boolean requestClosing(WlComponent component);
	
	/** 
	 * This method is invoked when the given window should be closed in
	 * order to perform some cleanup operations.
	 * @return "true" if the given window can be closed.
	 */
	public boolean requestClosing(WlWindow window);
}
