package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.NavigationUser;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.Beliefs;
import angerona.fw.report.Entity;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

import com.whiplash.gui.FancyTabbedPane;

public class AgentView extends BaseView implements NavigationUser, ReportListener {

	/** kill warning */
	private static final long serialVersionUID = -4199687668546277953L;

	private Agent agent;
	
	private ReportEntry currentEntry;
	
	private List<BaseView> components = new LinkedList<BaseView>();
	
	private FancyTabbedPane ftp;
	
	@Override
	public void init() {
		if(agent == null) {
			// TODO: Error
		}
		
		this.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		setTitle("Agent '" + agent.getName() +"'");
		JLabel agName = new JLabel("Name: " + agent.getName());
		top.add(agName, BorderLayout.NORTH);
		
		ftp = new FancyTabbedPane(AngeronaWindow.getInstance().getWindow(), false);
		Beliefs b = agent.getBeliefs();
		
		DesiresView dc = AngeronaWindow.createBaseComponent(DesiresView.class, agent.getDesires());
		dc.setTitle("Desires");
		addWlComponent(dc);
		
		BeliefbaseView comp = AngeronaWindow.createBaseComponent(BeliefbaseView.class, b.getWorldKnowledge());
		comp.setTitle("World");
		addWlComponent(comp);
		for(String viewName : b.getViewKnowledge().keySet()) {
			BaseBeliefbase actView = b.getViewKnowledge().get(viewName);
			BeliefbaseView actComp = AngeronaWindow.createBaseComponent(BeliefbaseView.class, actView);
			actComp.setTitle("View->" + viewName);
			addWlComponent(actComp);
		}
		
		/* TODO Readd
		BeliefbaseComponent bc = AngeronaWindow.createBaseComponent(BeliefbaseComponent.class, b.getConfidentialKnowledge());
		bc.setTitle("Conf");
		addWlComponent(bc);
		*/
		
		PlanView pc = AngeronaWindow.createBaseComponent(PlanView.class, agent.getPlan());
		addWlComponent(pc);
		
		add(ftp, BorderLayout.CENTER);
		Angerona.getInstance().addReportListener(this);
	}
	
	private void addWlComponent(BaseView bc) {
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
			
			for(BaseView bc : components) {
				if(bc instanceof BeliefbaseView) {
					BeliefbaseView bbc = (BeliefbaseView)bc;
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
