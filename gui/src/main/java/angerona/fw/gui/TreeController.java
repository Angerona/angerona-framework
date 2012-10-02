package angerona.fw.gui;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AgentComponent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.BaseBeliefbase;
import angerona.fw.gui.view.ResourcenView;
import angerona.fw.listener.SimulationListener;

/**
 * This class is responsible for keeping track of new Resources added to Angerona and to keep them
 * in sync with the JTrees in ResourcenView.
 * 
 * @see ResourcenView
 * @author Tim Janus
 */
public class TreeController implements SimulationListener {
	
	public class TreeUserObject {
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
			simulation.add(new DefaultMutableTreeNode(new TreeUserObject(str,
					configContainer.getSimulationConfiguration(str))));
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
		// Todo: Output name.
		DefaultMutableTreeNode simNode = new DefaultMutableTreeNode(
				new TreeUserObject(simulation.getName(), simulation));
		
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
		DefaultMutableTreeNode agNode = new DefaultMutableTreeNode(new TreeUserObject(added));
		DefaultMutableTreeNode world = new DefaultMutableTreeNode(
				new TreeUserObject("World", added.getBeliefs().getWorldKnowledge()));
		DefaultMutableTreeNode views = new DefaultMutableTreeNode("Views");
		
		agNode.add(world);
		if(added.getBeliefs().getViewKnowledge().size() > 0) {
			agNode.add(views);
			for(String name : added.getBeliefs().getViewKnowledge().keySet()) {
				BaseBeliefbase bb = added.getBeliefs().getViewKnowledge().get(name);
				DefaultMutableTreeNode actView = new DefaultMutableTreeNode(new TreeUserObject(name, bb));
				views.add(actView);
			}
		}
		
		DefaultMutableTreeNode comps = new DefaultMutableTreeNode("Components");
		for(AgentComponent ac  : added.getComponents()) {
			DefaultMutableTreeNode actComp = new DefaultMutableTreeNode(
				new TreeUserObject(ac.getClass().getSimpleName(), ac));
			comps.add(actComp);
		}
		
		agNode.add(comps);
		parent.add(agNode);
		
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

	@Override
	public void simulationDestroyed(
			AngeronaEnvironment simulationEnvironment) {
		DefaultTreeModel tm = (DefaultTreeModel)tree.getModel();
		for(int i=0; i<tm.getChildCount(root); ++i) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tm.getChild(root, i);
			if(! (dmtn.getUserObject() instanceof TreeUserObject)) 
				continue;
			
			TreeUserObject tuo = (TreeUserObject)dmtn.getUserObject();
			if(! (tuo.getUserObject() instanceof AngeronaEnvironment) )
				continue;
			
			AngeronaEnvironment sim = (AngeronaEnvironment)tuo.getUserObject();
			if(sim == simulationEnvironment) {
				tm.removeNodeFromParent(dmtn);
				break;
			}
		}
	}

	@Override
	public void actionPerformed(Agent agent, Action act) {
		// does nothing
	}
}
