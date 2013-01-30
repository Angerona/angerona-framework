package angerona.fw.gui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import angerona.fw.Angerona;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.listener.FrameworkListener;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * TODO
 * 
 * @author Tim Janus
 */
public class ResourceTreeController extends TreeControllerAdapter implements FrameworkListener {
	
	private DefaultMutableTreeNode root;
	
	private DefaultTreeModel treeModel;
	
	private JTree tree;
	
	public ResourceTreeController(JTree tree) {
		this.tree = tree;
		this.root = new DefaultMutableTreeNode("Root");
		
		treeModel = new DefaultTreeModel(root);
		this.tree.setModel(treeModel);
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         onMouseClick(e);
		     }
		};
		tree.addMouseListener(ml);
		
		readConfig();
		
		Angerona.getInstance().addFrameworkListener(this);
	}

	private void readConfig() {
		Angerona configContainer = Angerona.getInstance();
		
		root.removeAllChildren();

		DefaultMutableTreeNode agent = new DefaultMutableTreeNode("Agent Configs");
		for(String str : configContainer.getAgentConfigurationNames()) {
			agent.add(new DefaultMutableTreeNode(str));
		}
				
		DefaultMutableTreeNode beliefbase = new DefaultMutableTreeNode("Beliefbase Configs");
		for(String str: configContainer.getBeliefbaseConfigurationNames()) {
			beliefbase.add(new DefaultMutableTreeNode(str));
		}
				
		DefaultMutableTreeNode simulation = new DefaultMutableTreeNode("Simulation Templates");
		for(String str: configContainer.getSimulationConfigurationNames()) {
			simulation.add(new DefaultMutableTreeNode(new TreeUserObject(str,
					configContainer.getSimulationConfiguration(str))));
		}
		
		root.add(agent);
		root.add(beliefbase);
		root.add(simulation);
		expandAll(tree, true);
		tree.updateUI();
	}
	
	/**
	 * Helper method: calls correct tree-node handler
	 * @param selPath path to the selected tree-node.
	 */
	@Override
	protected void selectHandler(TreePath selPath) {
		Object o = selPath.getLastPathComponent();
		if(!(o instanceof DefaultMutableTreeNode))
			return;

		DefaultMutableTreeNode n = (DefaultMutableTreeNode) o;
		o = n.getUserObject();
		
		if(! (o instanceof ResourceTreeController.TreeUserObject))
			return;
		
		o = ((ResourceTreeController.TreeUserObject)o).getUserObject();
		
		if(o instanceof SimulationConfiguration) {
			String path = ((SimulationConfiguration)o).getFilePath();
			AngeronaWindow.getInstance().loadSimulation(path);
		}
	}
	
	public JTree getTree() {
		return tree;
	}

	@Override
	public void onBootstrapDone() {
		readConfig();
	}

	@Override
	public void onError(String errorTitle, String errorMessage) {
		// do nothing
	}
}
