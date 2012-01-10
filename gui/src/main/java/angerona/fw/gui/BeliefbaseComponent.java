package angerona.fw.gui;

import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;

import angerona.fw.Angerona;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.util.ReportListener;
import angerona.fw.util.ReportPoster;

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
	
	private JList actualLiterals;
	
	private DefaultListModel model;
	
	private List<List<String>> atomHistory = new LinkedList<List<String>>();
	
	public BeliefbaseComponent(String name, BaseBeliefbase bb) {
		super(name);
		this.beliefbase = bb;
		
		actualLiterals = new JList();
		this.add(actualLiterals);
		
		model = new DefaultListModel();
		actualLiterals.setModel(model);
		
		Angerona.getInstance().addReportListener(this);
		
		update();
	}

	private void update() {		
		List<String> actual = beliefbase.getAtoms();
		List<String> last = null;
		if(!atomHistory.isEmpty()) {
			//if(atomHistory.get(atomHistory.size()-1).equals(actual)) {
			//	return; // no changes...
			//} else {
				last = atomHistory.get(atomHistory.size()-1);
			//}
		}
		
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
		
		atomHistory.add(actual);
	}

	@Override
	public void reportReceived(String msg, ReportPoster sender,
			Object attachment) {
		if(attachment == beliefbase) {
			update();
		}
	}
}
