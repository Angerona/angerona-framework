package com.github.kreaturesfw.gui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.github.kreaturesfw.gui.AngeronaWindow;

/** 
 * Base class for controllers of a JTree. It provides custom user object
 * which wrap an object with an onActivate method which is indicated if
 * the node is double clicked.
 * @author Tim Janus
 * @deprecated
 */
public class TreeControllerAdapter {
	
	protected JTree tree;
	
	public TreeControllerAdapter(JTree tree) {
		if(tree == null)
			throw new IllegalArgumentException("tree must not be null.");
		this.tree = tree;
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         onMouseClick(e);
		     }
		};
		tree.addMouseListener(ml);
	}

	
	public JTree getTree() {
		return tree;
	}

	/**
	 * Helper method: called when user clicks on the tree 
	 * @param e structure containing data about the (click)mouse-event.
	 */
	protected void onMouseClick(MouseEvent e) {
		int selRow = getTree().getRowForLocation(e.getX(), e.getY());
         TreePath selPath = getTree().getPathForLocation(e.getX(), e.getY());
         if(selRow == -1)
        	 return;
         
         if(e.getClickCount() == 2) {
        	 Object obj = ((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject();
             if(obj instanceof UserObjectWrapper) {
            	 ((UserObjectWrapper)obj).onActivated();
             }
         }
	}
	
	/**
	 * Abstract base class which wraps on user object and prvoides an onActivate method which
	 * is invoked if the node containing the user object is double clicked for example. It also
	 * provides a custom toString method to adapt the text of the TreeNode containing the 
	 * user object.
	 * 
	 * @author Tim Janus
	 */
	protected abstract class UserObjectWrapper {
		private Object userObject;
		
		public UserObjectWrapper(Object userObject) {
			if(userObject == null)
				throw new IllegalArgumentException("userObject must not be null.");
			this.userObject = userObject;
		}
		
		public Object getUserObject() {
			return userObject;
		}
		
		@Override
		public abstract String toString();
		
		public abstract Icon getIcon();
		
		public abstract void onActivated();
	}
	
	/**
	 * Default implementation of an user object wrapper. The developer can
	 * change the name shown by the tree node by setting the name of this
	 * object. In the default case the toString method of the provided user
	 * object gives the representation string for the tree node.
	 * @author Tim Janus
	 */
	protected class DefaultUserObjectWrapper extends UserObjectWrapper {

		private String name;
		
		public DefaultUserObjectWrapper(Object userObject) {
			super(userObject);
			name = userObject.toString();
		}
		
		public DefaultUserObjectWrapper(Object userObject, String name) {
			super(userObject);
			setName(name);
		}
		
		public void setName(String name) {
			if(name == null)
				throw new IllegalArgumentException("name must not be null.");
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		@Override 
		public Icon getIcon() {
			return AngeronaWindow.get().getIcons().get("page_white");
		}
		
		@Override
		public String toString() {
			return name;
		}

		@Override
		public void onActivated() {
			// does nothing.
		}
		
	}

	public static void expandAll(JTree tree, boolean expand) {
		expandAll(tree, expand, new TreePath(tree.getModel().getRoot()));
	}
	
	public static void expandAll(JTree tree, boolean expand, TreePath parent) {
	    // Traverse children
	    TreeNode node = (TreeNode)parent.getLastPathComponent();
	    if (node.getChildCount() >= 0) {
	        for (Enumeration<?> e=node.children(); e.hasMoreElements(); ) {
	            TreeNode n = (TreeNode)e.nextElement();
	            TreePath path = parent.pathByAddingChild(n);
	            expandAll(tree, expand, path);
	        }
	    }

	    // Expansion or collapse must be done bottom-up
	    if (expand) {
	    	tree.expandPath(parent);
	    } else {
	    	tree.collapsePath(parent);
		}
	}
}
