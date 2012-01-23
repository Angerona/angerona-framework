package angerona.fw.gui;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import angerona.fw.Agent;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.Beliefs;

import com.whiplash.gui.FancyTabbedPane;

public class AgentComponent extends BaseComponent {

	/** kill warning */
	private static final long serialVersionUID = -4199687668546277953L;

	private Agent agent;
	
	public AgentComponent(Agent agent) {
		super("Agent '" + agent.getName() +"'");
		this.agent = agent;
		
		this.setLayout(new BorderLayout());
		
		JLabel agName = new JLabel("Name: " + agent.getName());
		add(agName, BorderLayout.NORTH);
		
		FancyTabbedPane ftp = new FancyTabbedPane(SimulationMonitor.getInstance().getWindow(), false);
		Beliefs b = agent.getBeliefs();
		ftp.addWlComponent(new BeliefbaseComponent("World", b.getWorldKnowledge()));
		for(String viewName : b.getViewKnowledge().keySet()) {
			BaseBeliefbase actView = b.getViewKnowledge().get(viewName);
			ftp.addWlComponent(new BeliefbaseComponent("View->"+viewName, actView));
		}
		ftp.addWlComponent(new BeliefbaseComponent("Confidential", b.getConfidentialKnowledge()));
		
		add(ftp, BorderLayout.CENTER);
	}

	@Override
	public String getComponentTypeName() {
		return "Default Agent-Component";
	}
}
