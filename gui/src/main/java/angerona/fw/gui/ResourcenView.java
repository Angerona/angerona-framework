package angerona.fw.gui;

import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import angerona.fw.Angerona;

public class ResourcenView extends BaseComponent {

	/** kill warning */
	private static final long serialVersionUID = 5711286288337915366L;
	
	private JTree tree;
	
	public ResourcenView() {
		super("Resourcen");
		tree = new JTree();
		DefaultMutableTreeNode ar = new DefaultMutableTreeNode("Angerona Resourcen");
		new TreeController(ar, Angerona.getInstance());
		tree.setModel(new DefaultTreeModel(ar));
		SimulationMonitor.expandAll(tree, true);
		this.add(tree);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return tree.getPreferredSize();
	}
	
}
