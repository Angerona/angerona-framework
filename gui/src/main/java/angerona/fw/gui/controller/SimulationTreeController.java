package angerona.fw.gui.controller;

import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AgentComponent;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.BaseBeliefbase;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.view.BeliefbaseView;
import angerona.fw.gui.view.View;
import angerona.fw.listener.SimulationListener;
import bibliothek.gui.Dockable;

public class SimulationTreeController extends TreeControllerAdapter implements SimulationListener {
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(SimulationTreeController.class);

	private DefaultTreeModel treeModel;
	
	private DefaultMutableTreeNode root;

	
	public SimulationTreeController(JTree tree) {
		super(tree);
		root = new DefaultMutableTreeNode("Root");
		treeModel = new DefaultTreeModel(root);
		tree.setModel(treeModel);
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
		
		/** @todo More dynamically... using plugin architecture etc. */
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
		
		// create user object wrapper for agent node:
		UserObjectWrapper agent = new DefaultUserObjectWrapper(added) {
			@Override
			public void onActivated() {
				handlerAgent((Agent)this.getUserObject());
			}
		};
		DefaultMutableTreeNode agNode = new DefaultMutableTreeNode(agent);
		
		// create user object wrapper for world belief base node
		BaseBeliefbase worldBB = added.getBeliefs().getWorldKnowledge();
		UserObjectWrapper worldWrapper = new DefaultUserObjectWrapper(worldBB, "World") {
			@Override
			public void onActivated() {
				handlerBeliefbase((BaseBeliefbase)getUserObject());
			}
		};
		agNode.add(new DefaultMutableTreeNode(worldWrapper));
		
		// create views container node if a view exists.
		DefaultMutableTreeNode views = null;
		if(added.getBeliefs().getViewKnowledge().size() > 0) {
			views = new DefaultMutableTreeNode("Views");
			agNode.add(views);
			
		}
		
		// create nodes for the views and their user wrapper objects
		Set<String> names = added.getBeliefs().getViewKnowledge().keySet();
		for(String name : names) {
			BaseBeliefbase bb = added.getBeliefs().getViewKnowledge().get(name);
			UserObjectWrapper w = new DefaultUserObjectWrapper(bb, name) {
				@Override
				public void onActivated() {
					handlerBeliefbase((BaseBeliefbase)getUserObject());
				}
			};
			views.add(new DefaultMutableTreeNode(w));
		}
		
		// Create tree nodes for the agent components and their user object wrappers.
		DefaultMutableTreeNode comps = new DefaultMutableTreeNode("Components");
		for(AgentComponent ac  : added.getComponents()) {
			UserObjectWrapper w = new DefaultUserObjectWrapper(ac, ac.getClass().getSimpleName()) {
				@Override
				public void onActivated() {
					handlerAgentComponent((AgentComponent)getUserObject());
				}
			};
			comps.add(new DefaultMutableTreeNode(w));
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
			if(! (dmtn.getUserObject() instanceof UserObjectWrapper)) 
				continue;
			
			UserObjectWrapper tuo = (UserObjectWrapper)dmtn.getUserObject();
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
