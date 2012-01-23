package angerona.fw.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import angerona.fw.Angerona;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.report.ReportAttachment;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

public class BeliefbaseComponent extends BaseComponent implements ReportListener, NavigationUser {

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
			return "[" + (status == ST_NOTCHANGED ? " " : (status == ST_NEW ? "N" : "D")) + "] " + name; 
		}
	}
	
	/** kill warning */
	private static final long serialVersionUID = -3706152280500718930L;
	
	private BaseBeliefbase beliefbase;
	
	private BaseBeliefbase actualBeliefbase;
	
	private BaseBeliefbase previousBeliefbase;
	
	private JList actualLiterals;
	
	private DefaultListModel model;
	
	private ReportEntry actEntry;
	
	public BeliefbaseComponent(String name, BaseBeliefbase bb) {
		super(name);
		this.beliefbase = bb;
		this.actualBeliefbase = bb;
		List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(beliefbase);
		if(entries.size() > 0) {
			actEntry = entries.get(entries.size()-1);
		}
		this.setLayout(new BorderLayout());
		add(new NavigationPanel(this), BorderLayout.NORTH);
		
		actualLiterals = new JList();
		this.add(actualLiterals, BorderLayout.CENTER);
		
		model = new DefaultListModel();
		actualLiterals.setModel(model);
		
		defaultUpdatePrevious();
		update();
		Angerona.getInstance().addReportListener(this);
	}
	
	private void update() {		
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
			if(isVisible() && entry.getAttachment().getGUID() == beliefbase.getGUID()) {
				actEntry = entry;
				actualBeliefbase = (BaseBeliefbase) entry.getAttachment();
				defaultUpdatePrevious();
				update();
			}
		}
	}

	private void defaultUpdatePrevious() {
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
	public ReportAttachment getAttachment() {
		return beliefbase;
	}

	@Override
	public ReportEntry getActualEntry() {
		return actEntry;
	}

	@Override
	public void setActualEntry(ReportEntry entry) {
		reportReceived(entry);
	}
}
