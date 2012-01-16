package angerona.fw.gui;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.util.SimulationListener;

public class TreeController implements SimulationListener {
	
	public class BBUserObject {
		private BaseBeliefbase beliefbase;
		private String prefix;
		
		public BBUserObject(String prefix, BaseBeliefbase beliefbase) {
			this.beliefbase = beliefbase;
			this.prefix = prefix;
		}
		
		BaseBeliefbase getBeliefbase() {
			return beliefbase;
		}
		
		@Override
		public String toString() {
			return prefix + " - Knowledge";
		}
	}
	
	public class WorldUserObject extends BBUserObject 
	{public WorldUserObject(BaseBeliefbase bb) {super("World", bb);} }
	
	public class ConfUserObject extends BBUserObject 
	{public ConfUserObject(BaseBeliefbase bb) {super("Confidential", bb);} }
	
	public class ViewUserObject extends BBUserObject 
	{public ViewUserObject(String onAgent, BaseBeliefbase bb) {super("View --> " + onAgent, bb);} }
	
	public class SimulationUserObject {
		private AngeronaEnvironment simulation;
		public SimulationUserObject(AngeronaEnvironment env) {
			simulation = env;
		}
		
		public AngeronaEnvironment getSimulation() {
			return simulation;
		}
		
		@Override
		public String toString() {
			return simulation.getName();
		}
	}
	
	private DefaultMutableTreeNode root;
	
	private DefaultTreeModel treeModel;
	
	private JTree tree;
	
	public TreeController(JTree tree, DefaultMutableTreeNode root) {
		Angerona configContainer = Angerona.getInstance();
		
		this.tree = tree;
		this.root = root;
		
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
			simulation.add(new DefaultMutableTreeNode(str));
		}
		
		root.add(agent);
		root.add(beliefbase);
		root.add(simulation);
		
		treeModel = (DefaultTreeModel) tree.getModel();
		treeModel.setRoot(root);
		expandAll(tree, true);
		configContainer.addSimulationListener(this);
	}
	
	public JTree getTree() {
		return tree;
	}
	
	@Override
	public void simulationStarted(AngeronaEnvironment simulation) {
		simulation.getName();
		DefaultMutableTreeNode simNode = new DefaultMutableTreeNode(new SimulationUserObject(simulation));
		
		for(String agName : simulation.getAgentNames()) {
			agentAddedInt(simNode, simulation.getAgentByName(agName));
		}
		
		treeModel.insertNodeInto(simNode, root, root.getChildCount());
		expandAll(tree, true);
	}

	@Override
	public void agentAdded(AngeronaEnvironment simulationEnvironment,
			Agent added) {
	}
	
	private void agentAddedInt(DefaultMutableTreeNode parent, Agent added) {
		DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(added.getName());
		DefaultMutableTreeNode world = new DefaultMutableTreeNode(new WorldUserObject(added.getBeliefs().getWorldKnowledge()) );
		DefaultMutableTreeNode views = new DefaultMutableTreeNode("Views");
		DefaultMutableTreeNode conf = new DefaultMutableTreeNode(new ConfUserObject(added.getBeliefs().getConfidentialKnowledge()));
		dmt.add(world);
		dmt.add(views);
		dmt.add(conf);
		for(String name : added.getBeliefs().getViewKnowledge().keySet()) {
			BaseBeliefbase bb = added.getBeliefs().getViewKnowledge().get(name);
			DefaultMutableTreeNode actView = new DefaultMutableTreeNode(new ViewUserObject(name, bb));
			views.add(actView);
		}
		
		parent.add(dmt);
		expandAll(tree, true);
	}

	@Override
	public void agentRemoved(AngeronaEnvironment simulationEnvironment,
			Agent removed) {		
	}

	@Override
	public void tickDone(AngeronaEnvironment simulationEnvironment, boolean finished) {
		// do nothing.
	}
	
	// If expand is true, expands all nodes in the tree.
		// Otherwise, collapses all nodes in the tree.
		public static void expandAll(JTree tree, boolean expand) {
		    TreeNode root = (TreeNode)tree.getModel().getRoot();

		    // Traverse tree from root
		    expandAll(tree, new TreePath(root), expand);
		}
		private static void expandAll(JTree tree, TreePath parent, boolean expand) {
		    // Traverse children
		    TreeNode node = (TreeNode)parent.getLastPathComponent();
		    if (node.getChildCount() >= 0) {
		        for (Enumeration<TreeNode> e=node.children(); e.hasMoreElements(); ) {
		            TreeNode n = (TreeNode)e.nextElement();
		            TreePath path = parent.pathByAddingChild(n);
		            expandAll(tree, path, expand);
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
