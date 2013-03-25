package angerona.fw.gui.controller;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.NavigationUser;
import angerona.fw.gui.view.View;
import angerona.fw.internal.Entity;
import angerona.fw.internal.ViewFactory;
import angerona.fw.listener.SimulationAdapter;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportOutputGenerator;

/**
 * A controller which fills a JTree with the reports of the current 
 * Angerona simulation and adds a default handling when nodes representing
 * an report entry which contains a attachment are clicked --> opens a
 * view for those attachments.
 * @author Tim Janus
 */
public class ReportTreeController extends TreeControllerAdapter implements ReportOutputGenerator<JTree> {

	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(ReportTreeController.class);
	
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
	    		tree.updateUI();
	    	}
	    	
	    	@Override
	    	public void simulationDestroyed(AngeronaEnvironment simulationEnvironment) {
	    		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)model.getRoot();
	    		dmtn.removeAllChildren();
	    		tree.updateUI();
	    	}
		};
		
		// register listeners:
		Angerona.getInstance().addSimulationListener(simulationHandler);
		Angerona.getInstance().addReportListener(this);
    }

	@Override
	public void reportReceived(final ReportEntry entry) {
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
					return AngeronaWindow.getInstance().getIcons().get("report");
				} else {
					return AngeronaWindow.getInstance().getIcons().get("report_attachment");
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
					ViewFactory wnd = ViewFactory.getInstance();
					// find the view
					View view = wnd.getBaseViewObservingEntity(at);
					if(view == null) {
						view = wnd.createViewForEntityComponent(at);
					}
					AngeronaWindow.getInstance().openView(view, "from Report(TODO)");
					
					if(view != null && view instanceof NavigationUser){
						NavigationUser lvc = (NavigationUser)view;
						lvc.setCurrentEntry(entry);
					}
				}
			}
		};
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wrapper);
		if(useAgentNode)
			actAgentNode.add(newNode);
		else
			actTickNode.add(newNode);

		ResourceTreeController.expandAll(tree, true);
		tree.updateUI();
	}

	@Override
	public JTree getOutput() {
		return tree;
	} 
    
	private void updateTickNode(Integer tick) {
		if(	actTickNode == null || 
			!tick.equals(actTickNode.getUserObject())) {
			actTickNode = new DefaultMutableTreeNode(tick);
			rootNode.add(actTickNode);
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
		actTickNode.add(actAgentNode);
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
			return AngeronaWindow.getInstance().getIcons().get("agent");
		}
		
		@Override
    	public String toString() {
    		return (action == null ? "<html>" + agent.getName() + "</html>" : "<html><b>" + action.toString() + "</b></html>");
    	}
	}
}
