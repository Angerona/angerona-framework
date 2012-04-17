package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.TreeController;

public class ResourcenView extends BaseView {

	/** kill warning */
	private static final long serialVersionUID = 5711286288337915366L;
	
	/** logging facility */
	private static Logger LOG = LoggerFactory.getLogger(ResourcenView.class);
	
	/** tree containing resources */
	private JTree tree = new JTree();
	
	@Override
	public void init() {
		setTitle("Resourcen");
		this.setLayout(new BorderLayout());
		DefaultMutableTreeNode ar = new DefaultMutableTreeNode("Angerona Resourcen");
		new TreeController(tree, ar);
		this.add(new JScrollPane(tree), BorderLayout.CENTER);
		
		MouseListener ml = new MouseAdapter() {
		     public void mousePressed(MouseEvent e) {
		         onMouseClick(e);
		     }
		 };
		 tree.addMouseListener(ml);
	}
	
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
		if (o instanceof TreeController.BBUserObject) {
			handlerBeliefbase((TreeController.BBUserObject) o);
		} else if (o instanceof TreeController.AgentUserObject) {
			handlerAgent((TreeController.AgentUserObject) o);
		} else if (o instanceof TreeController.AgentComponentUserObject) {
			handlerAgentComponent((TreeController.AgentComponentUserObject) o);
		}
	}

	/**
	 * Handles the selection of a tree-node which encapsulates an Agent-Component.
	 * @param uo	The user-object of the tree-node containing further information.
	 */
	private void handlerAgentComponent(TreeController.AgentComponentUserObject uo) {
		String agname = uo.getComponent().getAgent().getName();
		LOG.trace("Handle AgentComponent: '{}' of Agent '{}'.", agname);
		BaseView view = AngeronaWindow.getInstance().createViewForAgentComponent(uo.getComponent());
		if(view != null) {
			AngeronaWindow.getInstance().addComponentToCenter(view);
		}
	}

	/**
	 * Handles the selection of a tree-node which encapsulates an Agent.
	 * @param uo	The user-object of the tree-node containing further information.
	 */
	private void handlerAgent(TreeController.AgentUserObject uo) {
		LOG.trace("Handle Agent '{}'", uo.getAgent().getName());
		AgentView ac = new AgentView();
		ac.setObservationObject(uo.getAgent());
		ac.init();
		AngeronaWindow.getInstance().addComponentToCenter(ac);
	}

	/**
	 * Handles the selection of a tree-node which encapsulates a belief base.
	 * @param uo	The user-object of the tree-node containing further information.
	 */
	private void handlerBeliefbase(TreeController.BBUserObject uo) {
		LOG.trace("Handle beliefbase: '{}'", uo.getBeliefbase().getFileEnding());
		BeliefbaseView bc = AngeronaWindow.createBaseView(
				BeliefbaseView.class, uo.getBeliefbase());
		AngeronaWindow.getInstance().addComponentToCenter(bc);
	}
	
	@Override
	public Dimension getMinimumSize() {
		return tree.getPreferredSize();
	}

	@Override
	public String getComponentTypeName() {
		return "Resourcen View";
	}
	
}
