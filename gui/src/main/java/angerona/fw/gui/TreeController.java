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

import angerona.fw.Agent;
import angerona.fw.AgentComponent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.view.ResourcenView;
import angerona.fw.listener.SimulationListener;
import angerona.fw.logic.BaseBeliefbase;

/**
 * This class is responsible for keeping track of new Resources added to Angerona and to keep them
 * in sync with the JTrees in ResourcenView.
 * 
 * @see ResourcenView
 * @author Tim Janus
 */
public class TreeController implements SimulationListener {
	
	public class BBUserObject {
		private BaseBeliefbase beliefbase;
		private String prefix;
		
		public BBUserObject(String prefix, BaseBeliefbase beliefbase) {
			this.beliefbase = beliefbase;
			this.prefix = prefix;
		}
		
		public BaseBeliefbase getBeliefbase() {
			return beliefbase;
		}
		
		@Override
		public String toString() {
			return "<" + beliefbase.getGUID() + "> " +prefix + " - Knowledge";
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
			return simulation.getPosterName();
		}
	}
	
	public class AgentComponentUserObject{
		private AgentComponent comp;
		
		public AgentComponentUserObject(AgentComponent comp) {
			this.comp = comp;
		}
		
		public AgentComponent getComponent() {
			return comp;
		}
		
		public String toString() {
			return comp.getClass().getSimpleName();
		}
	}
	
	public class AgentUserObject {
		private Agent agent;
		private boolean init = false;
		
		public AgentUserObject(Agent agent) {
			this.agent = agent;
		}
		
		public Agent getAgent() {
			return agent;
		}
		
		@Override
		public String toString() {
			return "<" + agent.getGUID() + "> " + agent.getName();
		}
		
		boolean getInit() {
			return init;
		}
		
		void setInit(boolean init) {
			this.init = init;
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
		
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			
			@Override
			public void treeWillExpand(TreeExpansionEvent ev)
					throws ExpandVetoException {
				internal(ev);
			}
			
			@Override
			public void treeWillCollapse(TreeExpansionEvent ev)
					throws ExpandVetoException {
				internal(ev);
			}
			
			private void internal(TreeExpansionEvent ev) throws ExpandVetoException {
				// TODO Find a way to determine if a double click or a single click was the basic for this event...
				DefaultMutableTreeNode n = (DefaultMutableTreeNode)ev.getPath().getLastPathComponent();
				if(n.getUserObject() instanceof AgentUserObject) {
					AgentUserObject auo = (AgentUserObject)n.getUserObject();
					if(auo.init) {
						throw new ExpandVetoException(ev);
					} else {
						auo.init = true;
					}
				}
			}
		});
	}
	
	public JTree getTree() {
		return tree;
	}
	
	@Override
	public void simulationStarted(AngeronaEnvironment simulation) {
		simulation.getPosterName();
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
		DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(new AgentUserObject(added));
		DefaultMutableTreeNode world = new DefaultMutableTreeNode(new WorldUserObject(added.getBeliefs().getWorldKnowledge()) );
		DefaultMutableTreeNode views = new DefaultMutableTreeNode("Views");
		
		dmt.add(world);
		if(added.getBeliefs().getViewKnowledge().size() > 0) {
			dmt.add(views);
			for(String name : added.getBeliefs().getViewKnowledge().keySet()) {
				BaseBeliefbase bb = added.getBeliefs().getViewKnowledge().get(name);
				DefaultMutableTreeNode actView = new DefaultMutableTreeNode(new ViewUserObject(name, bb));
				views.add(actView);
			}
		}
		
		DefaultMutableTreeNode comps = new DefaultMutableTreeNode("Components");
		for(AgentComponent ac  : added.getComponents()) {
			DefaultMutableTreeNode actComp = new DefaultMutableTreeNode(
				new AgentComponentUserObject(ac));
			comps.add(actComp);
		}
		// Todo components
		//	DefaultMutableTreeNode conf = new DefaultMutableTreeNode(new ConfUserObject(added.getBeliefs().getConfidentialKnowledge()));
		//	dmt.add(conf);
		dmt.add(comps);
		
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

	@Override
	public void simulationDestroyed(
			AngeronaEnvironment simulationEnvironment) {
		DefaultTreeModel tm = (DefaultTreeModel)tree.getModel();
		for(int i=0; i<tm.getChildCount(root); ++i) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tm.getChild(root, i);
			if(dmtn.getUserObject() instanceof SimulationUserObject) {
				SimulationUserObject suo = (SimulationUserObject)dmtn.getUserObject();
				if(suo.getSimulation() == simulationEnvironment) {
					tm.removeNodeFromParent(dmtn);
					break;
				}
			}
		}
		
	}
}
