package angerona.fw.gui.controller;

import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public abstract class TreeControllerAdapter implements TreeController {
	
	protected class TreeUserObject {
		private String name;
		
		private Object userObject;
		
		public TreeUserObject(Object userObject) {
			name = userObject.toString();
			this.userObject = userObject;
		}
		
		public TreeUserObject(String name, Object userObject) {
			this.name = name;
			this.userObject = userObject;
		}
		
		public Object getUserObject() {
			return userObject;
		}
		
		@Override
		public String toString() {
			return name;
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
             selectHandler(selPath);
         }
	}
	
	protected abstract void selectHandler(TreePath selPath);
	
	@Override
	public abstract JTree getTree();

}
