package com.github.kreaturesfw.gui.controller;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.github.kreaturesfw.core.KReatures;
import com.github.kreaturesfw.core.KReaturesEnvironment;
import com.github.kreaturesfw.core.basic.Action;
import com.github.kreaturesfw.core.basic.Agent;
import com.github.kreaturesfw.core.internal.Entity;
import com.github.kreaturesfw.core.listener.SimulationAdapter;
import com.github.kreaturesfw.core.report.ReportEntry;
import com.github.kreaturesfw.core.report.ReportOutputGenerator;
import com.github.kreaturesfw.gui.AngeronaWindow;
import com.github.kreaturesfw.gui.base.ViewComponent;
import com.github.kreaturesfw.gui.internal.ViewComponentFactory;
import com.github.kreaturesfw.gui.nav.NavigationUser;

/**
 * A controller which fills a JTree with the reports of the current 
 * Angerona simulation and adds a default handling when nodes representing
 * an report entry which contains a attachment are clicked --> opens a
 * view for those attachments.
 * @author Tim Janus
 * @deprecated
 */
public class ReportTreeController extends TreeControllerAdapter implements ReportOutputGenerator<JTree> {
	
	private DefaultMutableTreeNode rootNode;
	
    private DefaultTreeModel model;
    
    private DefaultMutableTreeNode actTickNode;
    
    private DefaultMutableTreeNode actAgentNode;
    
    private SimulationAdapter simulationHandler;
    
    public ReportTreeController(final JTree tree) {
    	super(tree);
    	rootNode = new DefaultMutableTreeNode("Report");
		model = new DefaultTreeModel(rootNode);
		tree.setModel(model);
		tree.setRootVisible(false);
		
		// override the default render behavior to mark nodes
		// which have attachments and are therefore clickable:
		DefaultTreeCellRenderer dtcm = new DefaultTreeCellRenderer() {
			/** kick warning */
			private static final long serialVersionUID = 2307910999800923976L;

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
		};
		tree.setCellRenderer(dtcm);
		
		// add handling methods for simulation specific events:
		simulationHandler = new SimulationAdapter() {
			@Override
	    	public void actionPerformed(Agent agent, Action act) {
				AgentUserObjectWrapper uo = (AgentUserObjectWrapper)actAgentNode.getUserObject();
	    		uo.action = act;
	    		model.reload(actAgentNode);
	    	}
	    	
	    	@Override
	    	public void simulationDestroyed(KReaturesEnvironment simulationEnvironment) {
	    		rootNode = new DefaultMutableTreeNode("Report");
	    		model = new DefaultTreeModel(rootNode);
	    		tree.setModel(model);
	    		model.reload();
	    		
	    	}
		};
		
		// register listeners:
		KReatures.getInstance().addSimulationListener(simulationHandler);
		KReatures.getInstance().addReportListener(this);
    }

	@Override
	public synchronized void reportReceived(final ReportEntry entry) {
		Integer tick = new Integer(entry.getSimulationTick());
		updateTickNode(tick);
		boolean useAgentNode = updateAgentNode(entry.getScope().getAgent());
		
		// create the user object wrapper for normal report entry tree nodes:
		UserObjectWrapper wrapper = new DefaultUserObjectWrapper(entry) {
			/**
			 * Allow html in the output of the tree node.
			 */
			@Override
	    	public String toString() {
				return "<html>" + entry.getPosterName() + ": " + entry.getMessage() + "</html>";
	    	}
			
			@Override
			public Icon getIcon() {
				if(entry.getAttachment() == null) {
					return AngeronaWindow.get().getIcons().get("report");
				} else {
					return AngeronaWindow.get().getIcons().get("report_attachment");
				}
				
			}
			
			/**
			 * If an attachment is at the report entry and a view exists to show this entry then
			 * open thus view and 
			 */
			@Override
			public void onActivated() {
				if(entry.getAttachment() != null) {
					Entity at = entry.getAttachment();
					ViewComponentFactory wnd = ViewComponentFactory.get();
					// find the view
					ViewComponent view = wnd.getBaseViewObservingEntity(at);
					if(view == null) {
						view = wnd.createViewForEntityComponent(at);
					}
					AngeronaWindow.get().openView(view);
					
					if(view != null && view instanceof NavigationUser){
						NavigationUser lvc = (NavigationUser)view;
						lvc.setCurrentEntry(entry);
					}
				}
			}
		};
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wrapper);
		if(useAgentNode)
			model.insertNodeInto(newNode, actAgentNode, actAgentNode.getChildCount());
		else
		    model.insertNodeInto(newNode, actTickNode, actTickNode.getChildCount());

		TreeControllerAdapter.expandAll(tree, true);
	}

	public void showDetail(boolean details) {
		if(details) {
			expandAll(tree, true);
		} else {
			DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
			expandAll(tree, false);
			
			for(int i=0; i<root.getChildCount(); ++i) {
				DefaultMutableTreeNode tickNode = (DefaultMutableTreeNode)root.getChildAt(i);
				tree.expandPath(new TreePath(tickNode.getPath()));
			}
		}
	}
	
	@Override
	public JTree getOutput() {
		return tree;
	} 
    
	private void updateTickNode(Integer tick) {
		if(	actTickNode == null || 
			!tick.equals(actTickNode.getUserObject())) {
			actTickNode = new DefaultMutableTreeNode(tick);
			model.insertNodeInto(actTickNode, rootNode, rootNode.getChildCount());
		}
	}

	private boolean updateAgentNode(Agent ag) {
		for(int i=0; i<actTickNode.getChildCount(); ++i) {
			DefaultMutableTreeNode child = (DefaultMutableTreeNode)actTickNode.getChildAt(i);
			UserObjectWrapper wrapper = (UserObjectWrapper)child.getUserObject();
			if(!(wrapper.getUserObject() instanceof Agent))
				continue;
			if(wrapper.getUserObject().equals(ag)) {
				actAgentNode = child;
				return true;
			}
		}
		actAgentNode = new DefaultMutableTreeNode(new AgentUserObjectWrapper(ag));
		model.insertNodeInto(actAgentNode, actTickNode, actTickNode.getChildCount());
		return true;
	}
	
	/**
	 * Specialized object wrapper for tree nodes containing an agent container
	 * for the report tree.
	 * @author Tim Janus
	 *
	 */
	protected class AgentUserObjectWrapper extends DefaultUserObjectWrapper {
		public Action action;
		
		private Agent agent;
		
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
}
