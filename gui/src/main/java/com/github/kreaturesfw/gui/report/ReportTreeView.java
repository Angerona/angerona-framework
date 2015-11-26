package com.github.kreaturesfw.gui.report;

import java.awt.BorderLayout;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.github.kreaturesfw.core.Angerona;
import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.core.legacy.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.AngeronaEnvironment;
import com.github.kreaturesfw.core.listener.SimulationListener;
import com.github.kreaturesfw.core.report.ReportEntry;
import com.github.kreaturesfw.gui.AngeronaWindow;
import com.github.kreaturesfw.gui.base.ObservingPanel;
import com.github.kreaturesfw.gui.base.ViewComponent;
import com.github.kreaturesfw.gui.internal.ViewComponentFactory;
import com.github.kreaturesfw.gui.nav.NavigationUser;
import com.github.kreaturesfw.gui.util.DefaultUserObjectWrapper;
import com.github.kreaturesfw.gui.util.UserObjectWrapper;
import com.github.kreaturesfw.gui.util.tree.UserObjectWrapperController;
import com.github.kreaturesfw.gui.util.tree.UserObjectWrapperTreeCellRenderer;

/**
 * The report tree view, uses three hierarchies: On the first level are nodes
 * that represent ticks of the simulation. On the second level are nodes that 
 * represent the agent who currently performs it's cylce and on the third level
 * are nodes that represent report entries.
 * 
 * The TreeView uses the {@link UserObjectWrapper} to adapt rendering and behavior
 * using the {@link UserObjectWrapperController} and {@link UserObjectWrapperTreeCellRenderer}.
 * For this the class generates two specializations of the {@link UserObjectWrapper}. The
 * first represents an agent and displays the agent name or the action of the agent. The
 * second represents an report entry. If this report-entry UserObjectWrapper 
 * is activated it opens a view representing the entry's attachment.
 * at the point of time in the simulation when the report entry has been generated.
 * 
 * @author Tim Janus
 */
public class ReportTreeView 
	extends ObservingPanel 
	implements ReportView, SimulationListener {

	/** serial version id*/
	private static final long serialVersionUID = 6013716486735815871L;
	
	/** Swing tree control that displays the data of the tree-model */
	JTree tree;
	
	/** the data model used for the JTree */
	private DefaultTreeModel viewModel;
	
	/** the root node of the tree-data-model */
	private DefaultMutableTreeNode rootNode;
	
	/** the current node on hierarchy level 1 (tick) */
	private DefaultMutableTreeNode curTickNode;
	
	/** the current node on hierarchy level 2 (agent) */
	private DefaultMutableTreeNode curAgentNode;
	
	/** the name of the agent stores in curAgentNode */
	private String curAgentName = "";
	
	public ReportTreeView() {
		rootNode = new DefaultMutableTreeNode("ROOT");
		viewModel = new DefaultTreeModel(rootNode);
		tree = new JTree(viewModel);
		tree.setRootVisible(false);
		tree.setCellRenderer(UserObjectWrapperTreeCellRenderer.get());
		tree.addMouseListener(UserObjectWrapperController.get());
		
		JScrollPane scrollPane = new JScrollPane(tree);
		
		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		Angerona.getInstance().addSimulationListener(this);
	}

	@Override
	public void reportReceived(final ReportEntry entry) {
		
		// Add a new agent-node if necessary:
		final Agent entryAgent = entry.getScope().getAgent();
		if(	entryAgent != null && 
			!curAgentName.equals(entryAgent.getName())) {
			curAgentName = entryAgent.getName();
			
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					curAgentNode = new DefaultMutableTreeNode(
							new AgentUserObjectWrapper(entryAgent));
					viewModel.insertNodeInto(curAgentNode, 
							curTickNode, curTickNode.getChildCount());
				}
			});
		}
		
		// Add the node representing the report-entry:
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(
						new EntryUserObjectWrapper(entry));
				viewModel.insertNodeInto(newNode, curAgentNode, 
						curAgentNode.getChildCount());
				tree.scrollPathToVisible(new TreePath(newNode.getPath()));
			}
		});
	}

	
	@Override
	public void tickStarting(AngeronaEnvironment simulationEnvironment) {
		invokeTickNodeCreation(simulationEnvironment.getSimulationTick());
		curAgentName = "";
	}
	
	/**
	 * Generates a tick node on the highest hierarchy with the given
	 * number.
	 * @param tick	The number that is linked to the tick node's name
	 */
	private void invokeTickNodeCreation(final Integer tick) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				curTickNode = new DefaultMutableTreeNode("Tick #" + tick);
				viewModel.insertNodeInto(curTickNode, rootNode, rootNode.getChildCount());
			}
		});
	}
	
	@Override
	public void actionPerformed(final Agent agent, final Action act) {
		// update the agent-node by setting the action, which is used for string-representation
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				AgentUserObjectWrapper uo = (AgentUserObjectWrapper)curAgentNode.getUserObject();
				uo.action = act;
				viewModel.reload(curAgentNode);
			}
		});
	}
	
	@Override
	public void simulationStarted(AngeronaEnvironment simulationEnvironment) {
		invokeTickNodeCreation(0);
	}
	
	@Override
	public void simulationDestroyed(AngeronaEnvironment simulationEnvironment) {
		// clear the tree-data-model:
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				viewModel.setRoot(rootNode = new DefaultMutableTreeNode("ROOT"));
				curTickNode = curAgentNode = null;
				curAgentName = "";
			}
		});
	}

	// USER OBJECTS USED IN TREES -----------------------------------------------
	
	/**
	 * Specialized user-object-wrapper for an agent. It override the default icon and
	 * also uses a string representation that depends on the agent or on an action 
	 * that an agent has performed.
	 * 
	 * @author Tim Janus
	 */
	private static class AgentUserObjectWrapper extends DefaultUserObjectWrapper {
		/** An action performed by the agent that is used to change the string representation of this node */
		private Action action;
		
		/** The agent that represents the user-object, whcih is wrapped by this instance */
		private Agent agent;
		
		/** Default-Ctor: Stores agent instance */
		public AgentUserObjectWrapper(Agent userObject) {
			super(userObject);
			this.agent = userObject;
		}
		
		@Override
		public Icon getIcon() {
			return AngeronaWindow.get().getIcons().get("agent");
		}
		
		@Override
    	public String toString() {
    		return (action == null ? "<html>" + agent.getName() + "</html>" : "<html><b>" + action.toString() + "</b></html>");
    	}
		
	}
	
	/**
	 * Specialized user-object-wrapper for an agent. It overrides the default icon
	 * and the string representation to display the report-entry properly. If the
	 * node of this user-object gets activated and the report-entry that acts as user-
	 * object has an attachment than a view is opened for the attachment 
	 * 
	 * @author Tim Janus
	 */
	private static class EntryUserObjectWrapper extends DefaultUserObjectWrapper {
		ReportEntry entry;
		
		public EntryUserObjectWrapper(ReportEntry entry) {
			super(entry);
			this.entry = entry;
		}
		
		@Override
    	public String toString() {
			// use html for futher formatting options */
			return "<html>" + entry.getPosterName() + ": " + entry.getMessage() + "</html>";
    	}
		
		@Override
		public Icon getIcon() {
			// show different icons for reports with and without attachments:
			if(entry.getAttachment() == null) {
				return AngeronaWindow.get().getIcons().get("report");
			} else {
				return AngeronaWindow.get().getIcons().get("report_attachment");
			}
			
		}
		
		@Override
		public void onActivated() {
			// Check for an attachment:
			if(entry.getAttachment() != null) {
				Entity at = entry.getAttachment();
				ViewComponentFactory wnd = ViewComponentFactory.get();
				
				// find a view that can display the attachment:
				ViewComponent view = wnd.getBaseViewObservingEntity(at);
				if(view == null) {
					view = wnd.createViewForEntityComponent(at);
				}
				
				// open that view and move to correct point in time if possible:
				AngeronaWindow.get().openView(view);
				if(view != null && view instanceof NavigationUser){
					NavigationUser lvc = (NavigationUser)view;
					lvc.setCurrentEntry(entry);
				}
			}
		}
	}
	
	// NOT REQUIRED SIMULATIONLISTENER METHODS: ---------------------------------
	
	@Override
	public void tickDone(AngeronaEnvironment simulationEnvironment) {
		// does nothing
	}
	
	@Override
	public void agentAdded(AngeronaEnvironment simulationEnvironment,
			Agent added) {
		// does nothing
	}

	@Override
	public void agentRemoved(AngeronaEnvironment simulationEnvironment,
			Agent removed) {
		// does nothing
	}

}
