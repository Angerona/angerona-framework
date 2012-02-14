package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.report.Entity;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;
import angerona.fw.util.SimulationListener;

/**
 * Generic ui component to show a Beliefbase. It shows its content in a list
 * 
 * It has a navigation element
 * @author Tim Janus
 */
public class BeliefbaseComponent extends UIComponent implements ReportListener, NavigationUser, SimulationListener {

	/**
	 * Inner class used to represent the elements in a list.
	 * @author Tim Janus
	 */
	private class ListElement {
		public String name;
		
		public int status;
		
		public static final int ST_NOTCHANGED = 1;
		
		public static final int ST_NEW = 2;
		
		public static final int ST_DELETED = 3;
		
		public ListElement(String name, int status) {
			this.name = name;
			this.status = status;
		}
		
		public String toString() {
			return name; 
		}
	}
	
	/** kill warning */
	private static final long serialVersionUID = -3706152280500718930L;
	
	/** reference to the orignal beliefbase (which is in the agent) */
	protected BaseBeliefbase beliefbase;
	
	/** reference to the beliefbase instance which is actually shown. */
	protected BaseBeliefbase actualBeliefbase;
	
	/** reference to the predecessor beliefbase of the actual beliefbase, this will be null if actual is the first */
	protected BaseBeliefbase previousBeliefbase;
	
	/** JList containing the literals of the actual belief base and the literals which were removed in the last step (removed and new literals are highlighted) */
	private JList actualLiterals;
	
	/** The data model to accessing the JList */
	private DefaultListModel model;
	
	/** reference to the actually showed report entry. */
	private ReportEntry actEntry;
	

	@Override
	public void init() {
		super.init();
		if(beliefbase != null) {
			List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(beliefbase);
			if(entries.size() > 0) {
				actEntry = entries.get(entries.size()-1);
			}
		}
		this.setLayout(new BorderLayout());
		add(new NavigationPanel(this), BorderLayout.NORTH);
		
		actualLiterals = new JList();
		actualLiterals.setCellRenderer(new ListRenderer());
		this.add(actualLiterals, BorderLayout.CENTER);
		
		model = new DefaultListModel();
		actualLiterals.setModel(model);
		
		defaultUpdatePrevious();
		update();
		Angerona.getInstance().addReportListener(this);
		Angerona.getInstance().addSimulationListener(this);
	}
	
	/**
	 * Helper method: updates the content of the JList containing the literals.
	 */
	private void update() {	
		if(actualBeliefbase == null)	return;
		
		List<String> actual = actualBeliefbase.getAtoms();
		List<String> last = previousBeliefbase == null ? null : previousBeliefbase.getAtoms();
		
		model.clear();		
		for(String atom : actual) {
			if(last != null && !last.contains(atom)) {
				model.add(0, new ListElement(atom, ListElement.ST_NEW));
			} else {
				model.add(0, new ListElement(atom, ListElement.ST_NOTCHANGED));
			}
		}
		
		if(last != null) {
			for(String atom : last) {
				if(!actual.contains(atom)) {
					model.add(0, new ListElement(atom, ListElement.ST_DELETED));
				}
			}
		}
	}

	@Override
	public void reportReceived(ReportEntry entry) {
		if(entry.getAttachment() != null) {
			if(entry.getAttachment().getGUID().equals(beliefbase.getGUID())) {
				actEntry = entry;
				if(isVisible()) {
					updateView();
				}
			}
		}
	}

	private void updateView() {
		actualBeliefbase = (BaseBeliefbase) actEntry.getAttachment();
		defaultUpdatePrevious();
		update();
	}

	/** Helper method: updates the reference of the previous beliefbase */
	private void defaultUpdatePrevious() {
		if(beliefbase == null)	return;
		List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(beliefbase);
		int index = entries.indexOf(actEntry) - 1;

		if(index >= 0) {
			ReportEntry prevEntry = entries.get(index);
			previousBeliefbase = (BaseBeliefbase) prevEntry.getAttachment();
		} else {
			previousBeliefbase = null;
		}
	}

	@Override
	public Entity getAttachment() {
		return beliefbase;
	}

	@Override
	public ReportEntry getCurrentEntry() {
		return actEntry;
	}

	@Override
	public void setCurrentEntry(ReportEntry entry) {
		reportReceived(entry);
	}
	
	@Override
	public Class<?> getObservationObjectType() {
		return BaseBeliefbase.class;
	}

	@Override
	public String getComponentTypeName() {
		return "Default Beliefbase-Component";
	}
	
	@Override
	public void setObservationObject(Object obj) {
		if(!(obj instanceof BaseBeliefbase)) {
			throw new IllegalArgumentException("Observation object must be of type 'BaseBeliefbase'");
		}
		this.beliefbase = (BaseBeliefbase)obj;
		this.actualBeliefbase = this.beliefbase;
	}
	
	/** special list renderer for the literals of the beliefbases, using the ListElement class which
	 *  saves the status of the literal (new, deleted, old)
	 * 	
	 * 	@author Tim Janus
	 */
	private class ListRenderer extends JLabel implements ListCellRenderer {

		/** kill warning */
		private static final long serialVersionUID = -6867522427499396635L;

		public ListRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList list, Object value,
				int index2, boolean isSelected, boolean cellHasFocus) {
			if(!(value instanceof ListElement)) throw new IllegalArgumentException("This list only allows list elements of type ListElement");
			
			ListElement le = (ListElement)value;
			setText(le.toString());
			setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
			setForeground(le.status == ListElement.ST_NEW ? new Color(0,128,0) : (le.status == ListElement.ST_DELETED ? Color.red : Color.BLACK));
			
			return this;
		}
		
	}

	@Override
	public void simulationStarted(AngeronaEnvironment simulationEnvironment) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void simulationDestroyed(AngeronaEnvironment simulationEnvironment) {
		// TODO: Remove correctly
		//this.getWindow().remove(this);
		//this.getWindowSet().remove(this);
	}

	@Override
	public void agentAdded(AngeronaEnvironment simulationEnvironment,
			Agent added) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void agentRemoved(AngeronaEnvironment simulationEnvironment,
			Agent removed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tickDone(AngeronaEnvironment simulationEnvironment,
			boolean finished) {
		// TODO Auto-generated method stub
		
	}
}
