package com.github.kreatures.gui.controller;

import java.util.Set;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bibliothek.gui.Dockable;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.KReaturesEnvironment;
import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.gui.KReaturesWindow;
import com.github.kreatures.gui.base.ViewComponent;
import com.github.kreatures.gui.view.BeliefbaseView;
import com.github.kreatures.core.internal.ViewComponentFactory;
import com.github.kreatures.core.listener.SimulationListener;

/**
 * @deprecated
 */
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
		ViewComponent view = ViewComponentFactory.get().createViewForEntityComponent(component);
		if(view != null) {
			Dockable dd = KReaturesWindow.get().openView(view);
			KReaturesWindow.get().registerDockableForCurrentSimulation(dd);
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
		KReaturesWindow.getInstance().addComponentToCenter(ac); */
	}

	/**
	 * Handles the selection of a tree-node which encapsulates a belief base.
	 * @param bb	The base belief base saved in selected tree node.
	 */
	private void handlerBeliefbase(BaseBeliefbase bb) {
		LOG.trace("Handle beliefbase: '{}'", bb.getFileEnding());
		
		Dockable dd = null;
		if(bb.getFileEnding().toLowerCase().equals("asp")) {
			ViewComponent view = ViewComponentFactory.get().createViewForEntityComponent(bb);
			if(view != null) {
				dd = KReaturesWindow.get().openView(view);
			}
		} else {
			BeliefbaseView bc = ViewComponentFactory.get().createEntityView(
					BeliefbaseView.class, bb);
			dd = KReaturesWindow.get().openView(bc);
		}
	
		if(dd != null) {
			KReaturesWindow.get().registerDockableForCurrentSimulation(dd);
		}
	}
	
	@Override
	public void simulationStarted(KReaturesEnvironment simulation) {
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
	public void agentAdded(KReaturesEnvironment simulationEnvironment,
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
	public void agentRemoved(KReaturesEnvironment simulationEnvironment,
			Agent removed) {		
	}

	@Override
	public void tickDone(KReaturesEnvironment simulationEnvironment) {
		// do nothing.
	}

	@Override
	public void simulationDestroyed(
			KReaturesEnvironment simulationEnvironment) {
		DefaultTreeModel tm = (DefaultTreeModel)tree.getModel();
		for(int i=0; i<tm.getChildCount(root); ++i) {
			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)tm.getChild(root, i);
			if(! (dmtn.getUserObject() instanceof UserObjectWrapper)) 
				continue;
			
			UserObjectWrapper tuo = (UserObjectWrapper)dmtn.getUserObject();
			if(! (tuo.getUserObject() instanceof KReaturesEnvironment) )
				continue;
			
			KReaturesEnvironment sim = (KReaturesEnvironment)tuo.getUserObject();
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


	@Override
	public void tickStarting(KReaturesEnvironment simulationEnvironment) {
		// does nothing
	}
}
