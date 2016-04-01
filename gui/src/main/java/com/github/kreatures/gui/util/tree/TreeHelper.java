package com.github.kreatures.gui.util.tree;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * This class contains static methods to change a JTree. It support the
 * expandAll method for example which allows to expand or collapse all
 * tree nodes in a JTree.
 * 
 * @author Tim Janus
 */
public class TreeHelper {
	
	/**
	 * Expands or Collapses all tree nodes in the given JTree. Internally
	 * a recursive algorithm is used.
	 * @param tree		tree the operation is performed on.
	 * @param expand	true means expand the tree, false collapse it.
	 */
	public static void expandAll(JTree tree, boolean expand) {
		expandAll(tree, expand, new TreePath(tree.getModel().getRoot()));
	}
	
	/**
	 * Traverses all the children of the last component of the given tree path and calls
	 * this method recursively on them and also expands or collapse the tree path.
	 * @param tree		The working JTree
	 * @param expand	true means expand the tree path, false collapse it.
	 * @param parent
	 */
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
