package angerona.fw.gui;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.Beliefs;
import angerona.fw.report.Entity;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

import com.whiplash.gui.FancyTabbedPane;

public class AgentComponent extends UIComponent implements NavigationUser, ReportListener {

	/** kill warning */
	private static final long serialVersionUID = -4199687668546277953L;

	private Agent agent;
	
	private ReportEntry currentEntry;
	
	private List<UIComponent> components = new LinkedList<UIComponent>();
	
	private FancyTabbedPane ftp;
	
	@Override
	public void init() {
		if(agent == null) {
			// TODO: Error
		}
		
		this.setLayout(new BorderLayout());
		setTitle("Agent '" + agent.getName() +"'");
		JLabel agName = new JLabel("Name: " + agent.getName());
		add(agName, BorderLayout.NORTH);
		
		ftp = new FancyTabbedPane(AngeronaWindow.getInstance().getWindow(), false);
		Beliefs b = agent.getBeliefs();
		
		BeliefbaseComponent comp = AngeronaWindow.createBaseComponent(BeliefbaseComponent.class, b.getWorldKnowledge());
		comp.setTitle("World");
		addWlComponent(comp);
		for(String viewName : b.getViewKnowledge().keySet()) {
			BaseBeliefbase actView = b.getViewKnowledge().get(viewName);
			BeliefbaseComponent actComp = AngeronaWindow.createBaseComponent(BeliefbaseComponent.class, actView);
			actComp.setTitle("View->" + viewName);
			addWlComponent(actComp);
		}
		BeliefbaseComponent bc = AngeronaWindow.createBaseComponent(BeliefbaseComponent.class, b.getConfidentialKnowledge());
		bc.setTitle("Conf");
		addWlComponent(bc);
		
		add(ftp, BorderLayout.CENTER);
		Angerona.getInstance().addReportListener(this);
	}
	
	private void addWlComponent(UIComponent bc) {
		ftp.addWlComponent(bc);
		components.add(bc);
	}

	@Override
	public String getComponentTypeName() {
		return "Default Agent-Component";
	}

	@Override
	public Entity getAttachment() {
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
			
			for(UIComponent bc : components) {
				if(bc instanceof BeliefbaseComponent) {
					BeliefbaseComponent bbc = (BeliefbaseComponent)bc;
					if(bbc.getAttachment().getGUID().equals(id)) {
						ftp.activateComponent(bbc);
					}
				}
			}
		}
	}
	
	@Override
	public void setObservationObject(Object obj) {
		if(!(obj instanceof Agent)) {
			throw new IllegalArgumentException("The observatin Object must be of type 'Agent'");
		}
		this.agent = (Agent)obj;
	}
}
