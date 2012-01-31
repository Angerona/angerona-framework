package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

public class ResourcenView extends BaseComponent {

	/** kill warning */
	private static final long serialVersionUID = 5711286288337915366L;
	
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
		         int selRow = tree.getRowForLocation(e.getX(), e.getY());
		         TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
		         if(selRow != -1) {
		             if(e.getClickCount() == 2) {
		                 Object o = selPath.getLastPathComponent();
		                 if(o instanceof DefaultMutableTreeNode) {
		                	 DefaultMutableTreeNode n = (DefaultMutableTreeNode)o;
		                	 o = n.getUserObject();
		                	 if(o instanceof TreeController.BBUserObject) {
		                		 TreeController.BBUserObject temp = (TreeController.BBUserObject)o;
		                		 BeliefbaseComponent bc = SimulationMonitor.createBaseComponent(BeliefbaseComponent.class, 
		                				 temp.getBeliefbase());
		                		 SimulationMonitor.getInstance().addComponentToCenter(bc);
		                	 } else if(o instanceof TreeController.AgentUserObject) {
		                		 TreeController.AgentUserObject temp = (TreeController.AgentUserObject)o;
		                		 AgentComponent ac = new AgentComponent();
		                		 ac.setObservationObject(temp.getAgent());
		                		 ac.init();
		                		 SimulationMonitor.getInstance().addComponentToCenter(ac);
		                	 }
		                 }
		             }
		         }
		     }
		 };
		 tree.addMouseListener(ml);
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
