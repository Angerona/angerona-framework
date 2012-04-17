package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

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
	
	private static Logger LOG = LoggerFactory.getLogger(ResourcenView.class);
	
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
		                		 BeliefbaseView bc = AngeronaWindow.createBaseComponent(BeliefbaseView.class, 
		                				 temp.getBeliefbase());
		                		 AngeronaWindow.getInstance().addComponentToCenter(bc);
		                	 } else if(o instanceof TreeController.AgentUserObject) {
		                		 TreeController.AgentUserObject temp = (TreeController.AgentUserObject)o;
		                		 AgentView ac = new AgentView();
		                		 ac.setObservationObject(temp.getAgent());
		                		 ac.init();
		                		 AngeronaWindow.getInstance().addComponentToCenter(ac);
		                	 } else if(o instanceof TreeController.AgentComponentUserObject) {
		                		 boolean uiViewFound = false;
		                		 TreeController.AgentComponentUserObject uo = (TreeController.AgentComponentUserObject)o;
		                		 Collection<Class<? extends BaseView>> uiViews = AngeronaWindow.getInstance().getUIComponentMap().values();
		                		 for(Class<? extends BaseView> cls : uiViews) {
		                			 try {
										if(uo.getComponent().getClass().equals(cls.newInstance().getObservationObjectType())) {
											 BaseView newly = AngeronaWindow.createBaseComponent(cls, uo.getComponent());
											 AngeronaWindow.getInstance().addComponentToCenter(newly);
											 uiViewFound = true;
										 }
									} catch (InstantiationException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (IllegalAccessException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
		                		 }
		                		 
		                		 if(!uiViewFound) {
		                			 LOG.warn("Cannot find UI-View for Agent-Component '{}' of agent '{}'", 
		                				uo, uo.getComponent().getAgent().getName());
		                		 }
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
