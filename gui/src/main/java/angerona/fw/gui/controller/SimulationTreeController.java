package angerona.fw.gui.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AgentComponent;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.BaseBeliefbase;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.SortedTreeNode;
import angerona.fw.gui.view.BeliefbaseView;
import angerona.fw.gui.view.View;
import angerona.fw.listener.SimulationListener;
import bibliothek.gui.Dockable;

public class SimulationTreeController extends TreeControllerAdapter implements SimulationListener {
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(SimulationTreeController.class);
	
	private JTree tree;
	
	private DefaultTreeModel treeModel;
	
	private DefaultMutableTreeNode root;
	
	@Override
	public JTree getTree() {
		return tree;
	}
	
	public SimulationTreeController(JTree tree) {
		this.tree = tree;
		root = new DefaultMutableTreeNode("Root");
		treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         onMouseClick(e);
		     }
		};
		tree.addMouseListener(ml);
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
		
		if (o instanceof BaseBeliefbase) {
			handlerBeliefbase((BaseBeliefbase) o);
		} else if (o instanceof Agent) {
			handlerAgent((Agent) o);
		} else if (o instanceof AgentComponent) {
			handlerAgentComponent((AgentComponent) o);
		}
	}
	
	/**
	 * Handles the selection of a tree-node which encapsulates an Agent-Component.
	 * @param component	The agent component saved in the clicked tree node.
	 */
	private void handlerAgentComponent(AgentComponent component) {
		String agname = component.getAgent().getName();
		LOG.trace("Handle AgentComponent: '{}' of Agent '{}'.", agname);
		View view = AngeronaWindow.getInstance().createViewForEntityComponent(component);
		if(view != null) {
			Dockable dd = AngeronaWindow.getInstance().openView(view, 
					component.getClass().getSimpleName() + " - " + agname);
			AngeronaWindow.getInstance().registerDockableForCurrentSimulation(dd);
		}
	}

	/**
	 * Handles the selection of a tree-node which encapsulates an Agent.
	 * @param agent	The agent saved in the clicked tree node.
	 */
	private void handlerAgent(Agent agent) {
		LOG.trace("Handle Agent '{}'", agent.getName());
		/*AgentView ac = new AgentView();
		ac.setObservationObject(agent);
		ac.init(); 
		AngeronaWindow.getInstance().addComponentToCenter(ac); */
	}

	/**
	 * Handles the selection of a tree-node which encapsulates a belief base.
	 * @param bb	The base belief base saved in selected tree node.
	 */
	private void handlerBeliefbase(BaseBeliefbase bb) {
		LOG.trace("Handle beliefbase: '{}'", bb.getFileEnding());
		
		Agent ag = bb.getAgent();
		String title = ag.getName() + ": ";
		if(bb.equals(ag.getBeliefs().getWorldKnowledge())) {
			title += "World";
		} else {
			for(String view : ag.getBeliefs().getViewKnowledge().keySet()) {
				BaseBeliefbase other = ag.getBeliefs().getViewKnowledge().get(view);
				if(other.equals(bb)) {
					title += "View -> " + view;
					break;
				}
			}
		}
		
		// TODO: More dynamically... using plugin architecture etc.
		Dockable dd = null;
		if(bb.getFileEnding().toLowerCase().equals("asp")) {
			View view = AngeronaWindow.getInstance().createViewForEntityComponent(bb);
			if(view != null) {
				dd = AngeronaWindow.getInstance().openView(view, title);
			}
		} else {
			BeliefbaseView bc = AngeronaWindow.getInstance().createEntityView(
					BeliefbaseView.class, bb);
			dd = AngeronaWindow.getInstance().openView(bc, title);
		}
	if(dd != null) {
		AngeronaWindow.getInstance().registerDockableForCurrentSimulation(dd);
	}
	}
	
	@Override
	public void simulationStarted(AngeronaEnvironment simulation) {
		// TODO: Important the controller is not registered to the simulation listener because
		// the controller is created during a simulationStarted event and the list of listeners
		// must not be changed during this event.
		
		for(String agName : simulation.getAgentNames()) {
			agentAddedInt(root, simulation.getAgentByName(agName));
		}
		
		expandAll(tree, true);
		tree.updateUI();
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
