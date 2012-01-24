package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.Beliefs;
import angerona.fw.report.ReportAttachment;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

import com.whiplash.gui.FancyTabbedPane;

public class AgentComponent extends BaseComponent implements NavigationUser, ReportListener {

	/** kill warning */
	private static final long serialVersionUID = -4199687668546277953L;

	private Agent agent;
	
	private ReportEntry currentEntry;
	
	private List<BaseComponent> components = new LinkedList<BaseComponent>();
	
	private FancyTabbedPane ftp;
	
	public AgentComponent(Agent agent) {
		super("Agent '" + agent.getName() +"'");
		this.agent = agent;
		
		this.setLayout(new BorderLayout());
		
		JLabel agName = new JLabel("Name: " + agent.getName());
		add(agName, BorderLayout.NORTH);
		
		ftp = new FancyTabbedPane(SimulationMonitor.getInstance().getWindow(), false);
		Beliefs b = agent.getBeliefs();
		addWlComponent(new BeliefbaseComponent("World", b.getWorldKnowledge()));
		for(String viewName : b.getViewKnowledge().keySet()) {
			BaseBeliefbase actView = b.getViewKnowledge().get(viewName);
			addWlComponent(new BeliefbaseComponent("View->"+viewName, actView));
		}
		addWlComponent(new BeliefbaseComponent("Confidential", b.getConfidentialKnowledge()));
		
		add(ftp, BorderLayout.CENTER);
		Angerona.getInstance().addReportListener(this);
	}
	
	private void addWlComponent(BaseComponent bc) {
		ftp.addWlComponent(bc);
		components.add(bc);
	}

	@Override
	public String getComponentTypeName() {
		return "Default Agent-Component";
	}

	@Override
	public ReportAttachment getAttachment() {
		return agent;
	}

	@Override
	public ReportEntry getCurrentEntry() {
		return currentEntry;
	}

	@Override
	public void setCurrentEntry(ReportEntry entry) {
		reportReceived(entry);
	}

	@Override
	public void reportReceived(ReportEntry entry) {
		if(entry.getAttachment() == null)	return;
		
		Long id = entry.getAttachment().getGUID();
		if(agent.getGUID().equals(id) ||
			agent.getChilds().contains(id)) {
			currentEntry = entry;
			
			for(BaseComponent bc : components) {
				if(bc instanceof BeliefbaseComponent) {
					BeliefbaseComponent bbc = (BeliefbaseComponent)bc;
					if(bbc.getAttachment().getGUID().equals(id)) {
						ftp.activateComponent(bbc);
					}
				}
			}
		}
	}
}
