package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.AgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.TreeController;
import angerona.fw.serialize.SimulationConfiguration;

public class ResourcenView extends BaseView {

	/** kick warning */
	private static final long serialVersionUID = -8021405489946274962L;

	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(ResourcenView.class);
	
	/** tree containing resources */
	private JTree tree = new JTree();
	
	@Override
	public void init() {
		this.setLayout(new BorderLayout());
		DefaultMutableTreeNode ar = new DefaultMutableTreeNode("Angerona Resourcen");
		new TreeController(tree, ar);
		this.add(new JScrollPane(tree), BorderLayout.CENTER);
		tree.setRootVisible(false);
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         onMouseClick(e);
		     }
		 };
		 tree.addMouseListener(ml);
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

	/**
	 * Helper method: calls correct tree-node handler
	 * @param selPath path to the selected tree-node.
	 */
	private void selectHandler(TreePath selPath) {
		Object o = selPath.getLastPathComponent();
		if(!(o instanceof DefaultMutableTreeNode))
			return;

		DefaultMutableTreeNode n = (DefaultMutableTreeNode) o;
		o = n.getUserObject();
		
		if(! (o instanceof TreeController.TreeUserObject))
			return;
		
		o = ((TreeController.TreeUserObject)o).getUserObject();
		
		if (o instanceof BaseBeliefbase) {
			handlerBeliefbase((BaseBeliefbase) o);
		} else if (o instanceof Agent) {
			handlerAgent((Agent) o);
		} else if (o instanceof AgentComponent) {
			handlerAgentComponent((AgentComponent) o);
		} else if(o instanceof SimulationConfiguration) {
			handlerSimulationConfiguration((SimulationConfiguration)o);
		}
	}
	

	private void handlerSimulationConfiguration(SimulationConfiguration config) {
		AngeronaWindow.getInstance().loadSimulation(config.getFilePath());
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
			AngeronaWindow.getInstance().openView(view, component.getClass().getSimpleName() + " - " + agname);
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
		if(bb.getFileEnding().toLowerCase().equals("asp")) {
			View view = AngeronaWindow.getInstance().createViewForEntityComponent(bb);
			if(view != null) {
				AngeronaWindow.getInstance().openView(view, title);
			}
		} else {
			BeliefbaseView bc = AngeronaWindow.getInstance().createEntityView(
					BeliefbaseView.class, bb);
			AngeronaWindow.getInstance().openView(bc, title);
		}
	}

	@Override
	public Class<?> getObservedType() {
		return null;
	}
}
