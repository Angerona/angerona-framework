package angerona.fw.gui.util;

import java.util.Enumeration;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class TreeHelper {
	public static interface UserObjectWrapper {
		
		Object getUserObject();
		
		String toString();
		
		Icon getIcon();
		
		void onActivated();
		
		void onSelected();
	}
	
	public static class DefaultUserObjectWrapper implements UserObjectWrapper {

		private Object userObject;
		
		public DefaultUserObjectWrapper(Object userObject) {
			if(userObject == null)
				throw new IllegalArgumentException("userObject must not be null.");
			this.userObject = userObject;
		}
		
		@Override
		public Object getUserObject() {
			return userObject;
		}
		
		@Override
		public String toString() {
			return getUserObject().toString();
		}

		@Override
		public Icon getIcon() {
			return null;
		}

		@Override
		public void onActivated() {
			// does nothing
		}

		@Override
		public void onSelected() {
			//does nothing
		}
		
	}
	
	public static void expandAll(JTree tree, boolean expand) {
		expandAll(tree, expand, new TreePath(tree.getModel().getRoot()));
	}
	
	private static void expandAll(JTree tree, boolean expand, TreePath parent) {
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
