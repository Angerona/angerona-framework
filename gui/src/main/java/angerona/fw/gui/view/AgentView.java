package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import angerona.fw.Agent;
import angerona.fw.AgentComponent;
import angerona.fw.Angerona;
import angerona.fw.gui.AngeronaWindow;
import angerona.fw.gui.NavigationUser;
import angerona.fw.internal.Entity;
import angerona.fw.logic.BaseBeliefbase;
import angerona.fw.logic.Beliefs;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

import com.whiplash.gui.FancyTabbedPane;

/**
 * The agent view is a container of views. It shows the different components of an agent 
 * and also its belief-bases. However the views of the components and the views of the
 * belief base are separate classes.
 * 
 * @author Tim Janus
 */
public class AgentView extends BaseView implements NavigationUser, ReportListener {

	/** kill warning */
	private static final long serialVersionUID = -4199687668546277953L;

	/** reference to the agent */
	private Agent agent;
	
	/** the actual shown state of the agent (identified by an report-entry) */
	private ReportEntry currentEntry;
	
	/** a list of views showing the several aspects of the agent */
	private List<BaseView> views = new LinkedList<BaseView>();
	
	/** the tabbed pane WlWindow Tabbed Pane which is used as parent. */
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
		
		AngeronaWindow wnd = AngeronaWindow.getInstance();
		BeliefbaseView comp = wnd.createBaseView(BeliefbaseView.class, b.getWorldKnowledge());
		comp.setTitle("World");
		addWlComponent(comp);
		for(String viewName : b.getViewKnowledge().keySet()) {
			BaseBeliefbase actView = b.getViewKnowledge().get(viewName);
			BeliefbaseView actComp = wnd.createBaseView(BeliefbaseView.class, actView);
			actComp.setTitle("View->" + viewName);
			addWlComponent(actComp);
		}
		

		DesiresView dc = wnd.createBaseView(DesiresView.class, agent.getDesires());
		dc.setTitle("Desires");
		addWlComponent(dc);
		
		for(AgentComponent ac : agent.getComponents()) {
			BaseView view = AngeronaWindow.getInstance().createViewForAgentComponent(ac);
			if(view != null) {
				addWlComponent(view);
			}
		}
		
		add(ftp, BorderLayout.CENTER);
		Angerona.getInstance().addReportListener(this);
	}
	
	private void addWlComponent(BaseView bc) {
		ftp.addWlComponent(bc);
		views.add(bc);
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
			
			for(BaseView bc : views) {
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
