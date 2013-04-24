package angerona.fw.gui.util.tree;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import angerona.fw.gui.util.CollectionMonitor;
import angerona.fw.gui.util.UserObjectWrapper;

/**
 * A tree collection monitor uses the UserObjectWrapper and ControllerListener to fire
 * events. This means a listener is informed if one UserObjectWrapper in
 * the tree got activated or if the user input invoked a remove command using
 * DELETE key of the keyboard etc. It also returns the list of selected nodes as
 * user objects which are saved in the UserObjectWrapper.
 * 
 * @author Tim Janus
 */
public class TreeCollectionMonitor extends CollectionMonitor<JTree> {
	
	/** the mouse listener registered to the tree monitored by this instance. */
	private MouseListener mouseListener;
	
	/** the key listener registered to the tree monitored by this instance. */
	private KeyListener keyListener;
	
	/**
	 * Helper method: called when user clicks on the tree, it checks
	 * if the a node is hit and if this node has a UserObjectWrapper
	 * as userObject and invokes the onActivated() method on the 
	 * UserObjectWrapper implementation.
	 * @param e structure containing data about the (click)mouse-event.
	 */
	protected void onMouseEvent(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		TreePath path = component.getPathForLocation(x,y);
		if(path != null && SwingUtilities.isLeftMouseButton(e)) {
			if(e.getClickCount() == 2) {
				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
				if(dmtn != null && dmtn.getUserObject() instanceof UserObjectWrapper) {
					UserObjectWrapper uo = (UserObjectWrapper)dmtn.getUserObject();
					uo.onActivated();
				}
			}
		}
	}
	/**
	 * Helper method: called when the user press a key when the tree has the focus.
	 * It fires a remove event if the DELETE key is pressed or an activation event if 
	 * the enter key is pressed.
	 * @param e data structure containing information about the key event.
	 */
	protected void onKeyEvent(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_DELETE) {
			fireRemoveEvent();
		} else if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(component.getSelectionPath() == null)
				return;
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)component.getSelectionPath().getLastPathComponent();
			if(dmtn != null && dmtn.getUserObject() instanceof UserObjectWrapper) {
				((UserObjectWrapper)dmtn.getUserObject()).onActivated();
			}
		}
	}

	@Override
	protected void wireEvents() {
		mouseListener = new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				onMouseEvent(e);
			}
		};
		component.addMouseListener(mouseListener);
		
		
		keyListener = new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				onKeyEvent(e);
			}
		};
		component.addKeyListener(keyListener);
	}

	@Override
	protected void unwireEvents() {
		component.removeMouseListener(mouseListener);
		component.removeKeyListener(keyListener);
	}

	@Override
	public List<Object> getSelectedUserObjects() {
		List<Object> userObjects = new LinkedList<>();
		for(TreePath tp : component.getSelectionPaths()) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tp.getLastPathComponent();
			if(dmtn != null && dmtn.getUserObject() instanceof UserObjectWrapper) {
				UserObjectWrapper userObjectWrapper = (UserObjectWrapper)dmtn.getUserObject();
				userObjects.add(userObjectWrapper.getUserObject());
			}
		}
		return userObjects;
	}
}
