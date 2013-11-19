package com.github.angerona.fw.gui.util.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.github.angerona.fw.gui.util.UserObjectWrapper;

/**
 * A general controller for SWING components that contain {@link UserObjectWrapper}.
 * It adds listener code that invokes the user-object's onActivate() method, such
 * that the activation code of the user-object is performed.
 * 
 * @author Tim Janus
 */
public class UserObjectWrapperController extends MouseAdapter {
	private static UserObjectWrapperController controller;
	
	private UserObjectWrapperController() {}
	
	public static UserObjectWrapperController get() {
		if(controller == null) {
			controller = new UserObjectWrapperController();
		}
		return controller;
	}
	
	/**
	 * Helper method: called when user clicks on controls holding user-objects
	 * 
	 * @param e structure containing data about the (click)mouse-event.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() instanceof JTree) {
			mouseInTree(e);
		}
	}

	/**
	 * Checks if a node is under the mouse-location and that is true and
	 * also a double click occurred then the onActivated method of the 
	 * user-object in that node is called.
	 * 
	 * @param e
	 */
	private void mouseInTree(MouseEvent e) {
		JTree srcTree = (JTree) e.getSource();
		int selRow = srcTree.getRowForLocation(e.getX(), e.getY());
		TreePath selPath = srcTree.getPathForLocation(e.getX(), e.getY());
		if (selRow == -1)
			return;

		if (e.getClickCount() == 2) {
			Object obj = ((DefaultMutableTreeNode) selPath
					.getLastPathComponent()).getUserObject();
			if (obj instanceof UserObjectWrapper) {
				((UserObjectWrapper) obj).onActivated();
			}
		}
	}
}
