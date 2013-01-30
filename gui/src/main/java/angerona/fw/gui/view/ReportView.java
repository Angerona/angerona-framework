package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.controller.ResourceTreeController;
import angerona.fw.listener.SimulationAdapter;
import angerona.fw.operators.OperatorVisitor;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

/**
 * shows the reports of the actual simulation in a list-view.
 * @author Tim Janus
 */
public class ReportView extends BaseView implements ReportListener {

	/** kick warning */
	private static final long serialVersionUID = 1L;

	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(ReportView.class);
	
    private JTree tree = new JTree();
 
    private TreeModel model = null;
    
    private DefaultMutableTreeNode rootNode;
    
    private DefaultMutableTreeNode actTickNode;
    
    private DefaultMutableTreeNode actAgentNode;
    
    /**
     * An user object for the Agent nodes of the treeview for reports.
     * This user object can contain the agent and the last used action.
     * @author Tim Janus
     */
    public class AgentNodeUserObject {
    	private Agent agent;
    	
    	private Action action;
    	
    	public AgentNodeUserObject(Agent agent) {
    		this.agent = agent;
    	}
    	
    	public void setAction(Action action) {
    		this.action = action;
    	}
    	
    	@Override
    	public String toString() {
    		return (action == null ? "<html>" + agent.getName() + "</html>" : "<html>" + action.toString() + "</html>");
    	}
    }
    
    public class Leaf {

    	private ReportEntry entry;
    	
    	public Leaf(ReportEntry entry) {
    		this.entry = entry;
    	}
    	
    	@Override
    	public String toString() {
    		return "<html>" + entry.getPosterName() + ": " + entry.getMessage() + "</html>";
    	}
    }
    
    private class NodeActionUpdater extends SimulationAdapter {
    	private ReportView parent;
    	
    	public NodeActionUpdater(ReportView parent) {
    		this.parent = parent;
    	}
    	
    	@Override
    	public void actionPerformed(Agent agent, Action act) {
    		AgentNodeUserObject uo = (AgentNodeUserObject)parent.actAgentNode.getUserObject();
    		uo.setAction(act);
    		tree.updateUI();
    	}
    	
    	@Override
    	public void simulationDestroyed(AngeronaEnvironment simulationEnvironment) {
    		DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)model.getRoot();
    		dmtn.removeAllChildren();
    		tree.updateUI();
    	}
    } 
    
    NodeActionUpdater nodeActionUpdater;
    
    @Override
	public void init() {
		setLayout(new BorderLayout());
		
		rootNode = new DefaultMutableTreeNode("Report");
		model = new DefaultTreeModel(rootNode);
		tree.setModel(model);
		tree.setRootVisible(false);
		JScrollPane pane = new JScrollPane(tree);
        add(pane, BorderLayout.CENTER);
        //setVisible(true);
        
        Angerona.getInstance().addReportListener(this);
        MouseListener ml = new MouseAdapter() {
        	public void mousePressed(MouseEvent e) {
        		onMouseClick(e);
		    }
		};
		tree.addMouseListener(ml);
        nodeActionUpdater = new NodeActionUpdater(this);
        Angerona.getInstance().addSimulationListener(nodeActionUpdater);
	}
    
    @Override
    public void cleanup() {}

    /**
	 * Helper method: called when user clicks on the tree 
	 * @param e structure containing data about the (click)mouse-event.
	 */
	private void onMouseClick(MouseEvent e) {
		int selRow = tree.getRowForLocation(e.getX(), e.getY());
         TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
         if(selRow == -1)
        	 return;
         
         if(e.getClickCount() == 2) {
             selectHandler(selPath);
         }
	}
	
	private void selectHandler(TreePath selPath) {
		if(selPath.getLastPathComponent() instanceof DefaultMutableTreeNode) {
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)selPath.getLastPathComponent();
			Object uo = node.getUserObject();
			if(uo instanceof Leaf) {
				ReportEntry entry = ((Leaf)uo).entry;
				if(entry.getAttachment() == null)
					return;
				
				AngeronaWindow wnd = AngeronaWindow.getInstance();
				View view = wnd.getBaseViewObservingEntity(entry.getAttachment());
				if(view != null) {
					if(view instanceof ReportListener) {
						((ReportListener)view).reportReceived(entry);
					}
					//wnd.addComponentToCenter(view);
				} else {
					
				}
			}
		}
	}
    
	@Override
	public void reportReceived(ReportEntry entry) {
		Integer tick = new Integer(entry.getSimulationTick());
		updateTickNode(tick);
		boolean useAgentNode = updateAgentNode(entry.getScope());
		
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(new Leaf(entry));
		if(useAgentNode)
			actAgentNode.add(newNode);
		else
			actTickNode.add(newNode);

		ResourceTreeController.expandAll(tree, true);
		tree.updateUI();
	}
	
	private void updateTickNode(Integer tick) {
		if(	actTickNode == null || 
			!tick.equals(actTickNode.getUserObject())) {
			actTickNode = new DefaultMutableTreeNode(tick);
			rootNode.add(actTickNode);
		}
	}

	private boolean updateAgentNode(OperatorVisitor scope) {
		if(scope instanceof Agent) {
			Agent ag = (Agent)scope;
			
			for(int i=0; i<actTickNode.getChildCount(); ++i) {
				DefaultMutableTreeNode child = (DefaultMutableTreeNode)actTickNode.getChildAt(i);
				if(! (child.getUserObject() instanceof AgentNodeUserObject))
					continue;
				AgentNodeUserObject uo = (AgentNodeUserObject)child.getUserObject();
				if(uo.agent.equals(ag)) {
					actAgentNode = child;
					return true;
				}
			}
			actAgentNode = new DefaultMutableTreeNode(new AgentNodeUserObject(ag));
			actTickNode.add(actAgentNode);
			return true;
		}
		
		LOG.warn("Add a report without scope. The report-system is not capable of link the report to a specific agent.");
		return false;
	}

	@Override
	public Class<?> getObservedType() {
		return null;
	}

}
