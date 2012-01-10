package angerona.fw.gui;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.util.SimulationListener;

public class TreeController implements SimulationListener {
	private DefaultMutableTreeNode rootNode;
	
	private JTree ui;
	
	public TreeController(DefaultMutableTreeNode node, JTree ui) {
		rootNode = node;
		Angerona configContainer = Angerona.getInstance();
		this.ui = ui;
		
		DefaultMutableTreeNode agent = new DefaultMutableTreeNode("Agent Configs");
		for(String str : configContainer.getAgentConfigurationNames()) {
			agent.add(new DefaultMutableTreeNode(str));
		}
				
		DefaultMutableTreeNode beliefbase = new DefaultMutableTreeNode("Beliefbase Configs");
		for(String str: configContainer.getBeliefbaseConfigurationNames()) {
			beliefbase.add(new DefaultMutableTreeNode(str));
		}
				
		DefaultMutableTreeNode simulation = new DefaultMutableTreeNode("Simulation Templates");
		for(String str: configContainer.getSimulationConfigurationNames()) {
			simulation.add(new DefaultMutableTreeNode(str));
		}
		
		node.add(agent);
		node.add(beliefbase);
		node.add(simulation);
		
		configContainer.addSimulationListener(this);
	}
	
	@Override
	public void simulationStarted(AngeronaEnvironment simulationEnvironment) {
		simulationEnvironment.getName();
		DefaultMutableTreeNode simNode = new DefaultMutableTreeNode(simulationEnvironment);
		rootNode.add(simNode);
		ui.setModel(new DefaultTreeModel(rootNode));
		
		for(String agName : simulationEnvironment.getAgentNames()) {
			agentAddedInt(simNode, simulationEnvironment.getAgentByName(agName));
		}
	}

	@Override
	public void agentAdded(AngeronaEnvironment simulationEnvironment,
			Agent added) {
		// TODO:
	}
	
	public class BBUserObject {
		private BaseBeliefbase beliefbase;
		private String prefix;
		
		public BBUserObject(String prefix, BaseBeliefbase beliefbase) {
			this.beliefbase = beliefbase;
			this.prefix = prefix;
		}
		
		BaseBeliefbase getBeliefbase() {
			return beliefbase;
		}
		
		@Override
		public String toString() {
			return prefix + " - Knowledge";
		}
	}
	
	public class WorldUserObject extends BBUserObject 
	{public WorldUserObject(BaseBeliefbase bb) {super("World", bb);} }
	
	public class ConfUserObject extends BBUserObject 
	{public ConfUserObject(BaseBeliefbase bb) {super("Confidential", bb);} }
	
	public class ViewUserObject extends BBUserObject 
	{public ViewUserObject(String onAgent, BaseBeliefbase bb) {super("View --> " + onAgent, bb);} }
	
	
	private void agentAddedInt(DefaultMutableTreeNode parent, Agent added) {
		DefaultMutableTreeNode dmt = new DefaultMutableTreeNode(added.getName());
		DefaultMutableTreeNode world = new DefaultMutableTreeNode(new WorldUserObject(added.getBeliefs().getWorldKnowledge()) );
		DefaultMutableTreeNode views = new DefaultMutableTreeNode("Views");
		DefaultMutableTreeNode conf = new DefaultMutableTreeNode(new ConfUserObject(added.getBeliefs().getConfidentialKnowledge()));
		dmt.add(world);
		dmt.add(views);
		dmt.add(conf);
		for(String name : added.getBeliefs().getViewKnowledge().keySet()) {
			BaseBeliefbase bb = added.getBeliefs().getViewKnowledge().get(name);
			DefaultMutableTreeNode actView = new DefaultMutableTreeNode(new ViewUserObject(name, bb));
			views.add(actView);
		}
		
		parent.add(dmt);
	}

	@Override
	public void agentRemoved(AngeronaEnvironment simulationEnvironment,
			Agent removed) {		
	}

	@Override
	public void tickDone(AngeronaEnvironment simulationEnvironment) {
		// do nothing.
	}
}
