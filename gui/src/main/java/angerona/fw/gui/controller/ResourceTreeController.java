package angerona.fw.gui.controller;

import java.util.Comparator;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import angerona.fw.Angerona;
import angerona.fw.AngeronaProject;
import angerona.fw.gui.AngeronaGUIDataStorage;
import angerona.fw.gui.SortedTreeNode;
import angerona.fw.listener.FrameworkListener;
import angerona.fw.serialize.SimulationConfiguration;

/**
 * 
 * @author Tim Janus
 */
public class ResourceTreeController extends TreeControllerAdapter implements FrameworkListener {
	
	private DefaultMutableTreeNode root;
	
	private DefaultTreeModel treeModel;
	
	public ResourceTreeController(JTree tree) {
		super(tree);
		this.root = new SortedTreeNode("Root", comp);
		
		treeModel = new DefaultTreeModel(root);
		this.tree.setModel(treeModel);
		
		readConfig();
		
		Angerona.getInstance().addFrameworkListener(this);
	}

	private void readConfig() {
		AngeronaProject configContainer = Angerona.getInstance().getProject();
		
		root.removeAllChildren();

		DefaultMutableTreeNode agent = new DefaultMutableTreeNode("Agent Configs");
		for(String str : configContainer.getAgentConfigurationNames()) {
			agent.add(new DefaultMutableTreeNode(str));
		}
				
		DefaultMutableTreeNode beliefbase = new DefaultMutableTreeNode("Beliefbase Configs");
		for(String str: configContainer.getBeliefbaseConfigurationNames()) {
			beliefbase.add(new DefaultMutableTreeNode(str));
		}
				
		DefaultMutableTreeNode simulationNode = new SortedTreeNode("Simulation Templates", comp);
		for(String str: configContainer.getSimulationConfigurationNames()) {
			DefaultMutableTreeNode actNode = simulationNode;
			SimulationConfiguration sc = configContainer.getSimulationConfiguration(str);
			String [] categories = sc.getCategory().split("/");
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
			actNode.add(new DefaultMutableTreeNode(new DefaultUserObjectWrapper(sc, str) {
					@Override
					public void onActivated() {
						SimulationConfiguration config = (SimulationConfiguration)this.getUserObject();
						AngeronaGUIDataStorage.get().getSimulationControl().setSimulation(config);
					}
				}));
		}
		
		root.add(agent);
		root.add(beliefbase);
		root.add(simulationNode);
		
		expandAll(tree, true);
		tree.updateUI();
	}

	@Override
	public void onBootstrapDone() {
		readConfig();
	}

	@Override
	public void onError(String errorTitle, String errorMessage) {
		// do nothing
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
			
			if(o1.getUserObject() instanceof UserObjectWrapper && o2.getUserObject() instanceof UserObjectWrapper) {
				UserObjectWrapper t1 = (UserObjectWrapper)o1.getUserObject();
				UserObjectWrapper t2 = (UserObjectWrapper)o2.getUserObject();
				return t1.toString().compareTo(t2.toString());
			}
			return 0;
		}
	};
}
