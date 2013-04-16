package angerona.fw.gui.project;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import angerona.fw.AngeronaProject;
import angerona.fw.gui.SortedTreeNode;
import angerona.fw.gui.base.ObservingPanel;
import angerona.fw.gui.util.TreeHelper;
import angerona.fw.gui.util.TreeHelper.UserObjectWrapper;
import angerona.fw.serialize.Resource;
import angerona.fw.serialize.SimulationConfiguration;

public class ProjectTreeView extends ObservingPanel implements ProjectView {
	/** kick warning */
	private static final long serialVersionUID = -8021405489946274962L;
	
	private UserObjectFactory factory = new DefaultUserObjectFactory();
	
	private JTree tree;
	
	private JPopupMenu leafContextMenu;
	
	private JPopupMenu categoryContextMenu;
	
	private JMenuItem miRemove;
	
	private JMenuItem miAdd;
	
	private DefaultTreeModel model;
	
	private DefaultMutableTreeNode root;
	
	private Map<Object, DefaultMutableTreeNode> leafMap = new HashMap<>();
	
	private Map<String, DefaultMutableTreeNode> typeNodeMap = new HashMap<>();
	
	public ProjectTreeView() {
		this.setLayout(new BorderLayout());
		
		tree = new JTree();
		this.add(new JScrollPane(tree), BorderLayout.CENTER);
		tree.setRootVisible(false);
		root = new DefaultMutableTreeNode("root");
		model = new DefaultTreeModel(root);
		tree.setModel(model);
		
		tree.updateUI();
		
		categoryContextMenu = new JPopupMenu();
		miAdd = new JMenuItem("Add...");
		categoryContextMenu.add(miAdd);
		
		leafContextMenu = new JPopupMenu();
		miRemove = new JMenuItem("Remove");
		leafContextMenu.add(miRemove);
		
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				
				TreePath path = tree.getPathForLocation(x,y);
				if(SwingUtilities.isLeftMouseButton(e)) {
					if(e.getClickCount() == 2) {
						DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)path.getLastPathComponent();
						if(dmtn != null) {
							UserObjectWrapper uo = (UserObjectWrapper)dmtn.getUserObject();
							uo.onActivated();
						}
					}
				} else if(SwingUtilities.isRightMouseButton(e)) {
					if(path == null) {
						categoryContextMenu.show(tree, x, y);
					} else {
						tree.setSelectionPath(path);
						leafContextMenu.show(tree, x, y);
					}
				} 
			}
		});
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent ev) {
				DefaultMutableTreeNode dmtn= (DefaultMutableTreeNode)ev.getPath().getLastPathComponent();
				if(dmtn != null) {
					((UserObjectWrapper)dmtn.getUserObject()).onSelected();
				}
			}
		});
	}
	
	@Override
	public <K, V> void onPut(String mapName, Map<K, V> changes) {
		for(K key : changes.keySet()) {
			onResourceAdded((Resource)changes.get(key));
		}
	}
	
	@Override 
	public <K> void onRemove(String mapName, K key) {
		remove((String)key);
	}

	@Override
	public void onProjectChange(AngeronaProject project) {
		for(MutableTreeNode mtn : typeNodeMap.values()) {
			mtn.removeFromParent();
		}
		typeNodeMap.clear();

		if(project != null) {
			for(Resource res : project.getResources().values()) {
				onResourceAdded(res);
			}
		}
		
		tree.updateUI();
		TreeHelper.expandAll(tree, true);
	}

	private void onResourceAdded(Resource res) {
		DefaultMutableTreeNode mtn = new DefaultMutableTreeNode(factory.createUserObject(res));
		DefaultMutableTreeNode actNode = typeNodeMap.get(res.getResourceType());
		if(actNode==null) {
			actNode = new SortedTreeNode(res.getResourceType(), comp);
			root.add(actNode);
			typeNodeMap.put(res.getResourceType(), actNode);
		}
		
		String [] categories = res.getCategory().split("/");
		for(String category : categories) {
			if(category.isEmpty())
				continue;
			
			boolean found = false;
			for(int i=0; i< actNode.getChildCount(); ++i) {
				DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)actNode.getChildAt(i);
				if(dmtn.getUserObject() instanceof String) {
					if(((String)dmtn.getUserObject()).compareTo(category) == 0) {
						actNode = dmtn;
						found = true;
						break;
					}
				}
			}
			if(!found) {
				DefaultMutableTreeNode newNode = new SortedTreeNode(category, comp);
				actNode.add(newNode);
				actNode = newNode;
			}
		}
		
		actNode.add(mtn);
		leafMap.put(res.getName(), mtn);
		
		tree.updateUI();
		TreeHelper.expandAll(tree, true);
	}
	
	private boolean remove(String res) {
		DefaultMutableTreeNode node = leafMap.get(res);
		if(node != null) {
			UserObjectWrapper uo = (UserObjectWrapper)node.getUserObject();
			MutableTreeNode parent = (MutableTreeNode)node.getParent();
			model.removeNodeFromParent(node);
			leafMap.remove(res);
			if(parent.getChildCount() == 0) {
				model.removeNodeFromParent(parent);
				typeNodeMap.remove(((Resource)(uo.getUserObject())).getResourceType());
			}
			tree.updateUI();
			return true;
		}
		return false;
	}


	protected Comparator<DefaultMutableTreeNode> comp = new Comparator<DefaultMutableTreeNode>() {
	
		@Override
		public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
			if(o1.getUserObject() instanceof String && o2.getUserObject() instanceof String) {
				return o1.getUserObject().toString().compareTo(o2.getUserObject().toString());
			}
			
			if(o1.getUserObject() instanceof String) {
				return -1;
			} else if(o2.getUserObject() instanceof String) {
				return 1;
			}
			
			if(o1.getUserObject() instanceof SimulationConfiguration && 
					o2.getUserObject() instanceof SimulationConfiguration) {
				SimulationConfiguration t1 = (SimulationConfiguration)o1.getUserObject();
				SimulationConfiguration t2 = (SimulationConfiguration)o2.getUserObject();
				return t1.toString().compareTo(t2.toString());
			}
			return 0;
		}
	};

	@Override
	public AbstractButton getRemoveButton() {
		return miRemove;
	}

	@Override
	public AbstractButton getAddButton() {
		return miAdd;
	}

	@Override
	public void setUserObjectFactory(UserObjectFactory factory) {
		if(factory != null) {
			this.factory = factory;
		}
	}
}
