package angerona.fw.gui.util;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class TreeHelper {
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
