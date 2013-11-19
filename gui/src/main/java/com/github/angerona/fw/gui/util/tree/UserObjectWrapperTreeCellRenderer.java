package com.github.angerona.fw.gui.util.tree;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.github.angerona.fw.gui.util.UserObjectWrapper;

/**
 * A specialized rendering for tree-nodes that contain {@link UserObjectWrapper}
 * instances as user-objects. The rendering shows the user-object's icon in front of the
 * tree-node.
 * 
 * This class has no state and therefore uses the singleton pattern, such that users
 * have to invoke UserObjectWrapperTreeCellRenderer.get() to receive the instance
 * of the {@link UserObjectWrapperTreeCellRenderer}
 * 
 * @author Tim Janus
 */
public class UserObjectWrapperTreeCellRenderer extends DefaultTreeCellRenderer {
	
	private static final long serialVersionUID = 7531008502895527598L;

	private static UserObjectWrapperTreeCellRenderer renderer;
	
	private UserObjectWrapperTreeCellRenderer() {}
	
	public static UserObjectWrapperTreeCellRenderer get() {
		if(renderer == null)
			renderer = new UserObjectWrapperTreeCellRenderer();
		return renderer;
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree,
	            Object value, boolean selected, boolean expanded,
	            boolean leaf, int row, boolean hasFocus){
		Component ret = super.getTreeCellRendererComponent(tree, value,
	            selected, expanded, leaf, row, hasFocus);

		/** @todo This looks ugly maybe there is a nicer solution. */
        JLabel label = (JLabel) ret ;
        if(value instanceof DefaultMutableTreeNode) {
        	DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode) value;
        	if(dmtn.getUserObject() instanceof UserObjectWrapper) {
        		UserObjectWrapper w = (UserObjectWrapper)dmtn.getUserObject();
        		label.setIcon( w.getIcon() ) ;
        	}
        }
        
        return label;
	}
}
