package com.github.kreatures.gui.util.tree;

import java.util.Collections;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

/**
 * A node in a JTree which sorts its children. The default behavior is an
 * alphabetically ordering but the developer can provide an instance of
 * Comparator<DefaultMutableTreeNode> to override this behavior.
 * 
 * @author Tim Janus
 */
public class SortedTreeNode extends DefaultMutableTreeNode {
	/** kick warning */
	private static final long serialVersionUID = 3865433262834779109L;
	
	/** the comparator used for ordering of the childs */
	private Comparator<DefaultMutableTreeNode> comparator = defaultComp;
	
	public SortedTreeNode(Object userObject) {
		super(userObject);
	}
	
	public SortedTreeNode(Object userObject, Comparator<DefaultMutableTreeNode> comparator) {
		super(userObject);
		this.comparator = comparator;
	}
	
	public void setComparator(Comparator<DefaultMutableTreeNode> comparator) {
		if(comparator == null)
			throw new IllegalArgumentException("Comparator must not be null.");
		this.comparator = comparator;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void insert(MutableTreeNode newChild, int childIndex) {
		super.insert(newChild, childIndex);
		Collections.sort(this.children, comparator);
	}
	
	@Override
	public void add(MutableTreeNode newChild) {
		insert(newChild, getChildCount());
	}
	
	/**
	 * The default comparator uses the user object's toString method
	 * and sorts the nodes by this strings alphabetically.
	 */
	static final public Comparator<DefaultMutableTreeNode> defaultComp = new Comparator<DefaultMutableTreeNode>() {
		@Override
		public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
			if(o1.getUserObject() == null)
				return -1;
			if(o2.getUserObject() == null)
				return 1;
			return o1.toString().compareTo(o2.toString());
		}
		
	};
}
