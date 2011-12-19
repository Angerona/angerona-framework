package angerona.fw.gui;

import javax.swing.tree.DefaultMutableTreeNode;

import angerona.fw.Angerona;

public class TreeController {
	public TreeController(DefaultMutableTreeNode node, Angerona configContainer) {
		
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
	}
}
