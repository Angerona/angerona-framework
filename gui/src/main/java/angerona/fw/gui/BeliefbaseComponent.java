package angerona.fw.gui;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import angerona.fw.Angerona;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.report.Report;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

public class BeliefbaseComponent extends BaseComponent implements ReportListener {

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
	
	public BeliefbaseComponent(String name, BaseBeliefbase bb) {
		super(name);
		this.beliefbase = bb;
		this.actualBeliefbase = bb;
		
		actualLiterals = new JList();
		this.add(actualLiterals);
		
		model = new DefaultListModel();
		actualLiterals.setModel(model);
		
		// TODO: Determine the environment using ReportAttachment hierarchy.
		defaultUpdatePrevious(null);
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
		if(isVisible() && entry.getAttachment() == beliefbase) {
			defaultUpdatePrevious(entry.getPoster().getSimulation());
			update();
		}
	}

	private void defaultUpdatePrevious(AngeronaEnvironment simulation) {
		Report r = simulation == null ? Angerona.getInstance().getActualReport() : Angerona.getInstance().getReport(simulation);
		List<ReportEntry> entries = r.getEntriesOf(beliefbase);

		if(entries.size() >= 2) {
			ReportEntry prevEntry = entries.get(entries.size()-2);
			previousBeliefbase = (BaseBeliefbase) prevEntry.getAttachment();
		}
	}
}
