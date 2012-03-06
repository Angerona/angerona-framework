package angerona.fw.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import angerona.fw.Angerona;
import angerona.fw.report.Entity;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

/**
 * Base class for UI-Components which show a colored list of their content.
 * For example the atoms in a beliefbase.
 * @author Tim Janus
 *
 * @param <T> the type of the observed object
 */
public abstract class ListViewColored<T extends Entity> extends UIComponent implements ReportListener, NavigationUser {

	/** kill warning*/
	private static final long serialVersionUID = 347925312291828783L;

	/** reference to the original data (which is in the agent) */
	protected Entity ref;
	
	/** reference to the data instance which is actually shown. */
	protected Entity actual;
	
	/** reference to the predecessor data instance of the actual data instance, this will be null if actual is the first */
	protected Entity previous;
	
	/** JList containing the literals of the actual belief base and the literals which were removed in the last step (removed and new literals are highlighted) */
	private JList actualLiterals;
	
	/** The data model to accessing the JList */
	private DefaultListModel model;
	
	/** reference to the actually showed report entry. */
	private ReportEntry actEntry;
	
	protected NavigationPanel navPanel;
	
	/**
	 * Inner class used to represent the elements in a list.
	 * @author Tim Janus
	 */
	protected class ListElement {
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
			if(!(value instanceof ListViewColored.ListElement)) 
				throw new IllegalArgumentException("This list only allows list elements of type ListElement");
			else {
				ListElement le = (ListElement)value;
				setText(le.toString());
				setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
				setForeground(le.status == ListElement.ST_NEW ? new Color(0,128,0) : (le.status == ListElement.ST_DELETED ? Color.red : Color.BLACK));
			}
			return this;
		}
		
	}
	
	@Override 
	public void init() {
		super.init();
		if(ref != null) {
			List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(ref);
			if(entries != null && entries.size() > 0) {
				actEntry = entries.get(entries.size()-1);
			} else {
				// TODO: LOG
			}
		} else {
			// TODO: Error
		}
		
		this.setLayout(new BorderLayout());
		navPanel = new NavigationPanel(this);
		add(navPanel, BorderLayout.NORTH);
		navPanel.setEntry(actEntry);
		
		actualLiterals = new JList();
		actualLiterals.setCellRenderer(new ListRenderer());
		this.add(actualLiterals, BorderLayout.CENTER);
		
		model = new DefaultListModel();
		actualLiterals.setModel(model);
		
		defaultUpdatePrevious();
		update();
		Angerona.getInstance().addReportListener(this);
	}
	
	protected abstract List<String> getStringRepresentation(Entity obj);
	
	/**
	 * Helper method: updates the content of the JList containing the literals.
	 */
	private void update() {	
		if(ref == null)	return;
		
		List<String> actual = getStringRepresentation(this.actual);
		List<String> last = previous == null ? null : getStringRepresentation(this.previous);
		
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
			if(entry.getAttachment().getGUID().equals(ref.getGUID())) {
				actEntry = entry;
				navPanel.setEntry(actEntry);
				if(isVisible()) {
					updateView();
				}
			}
		}
	}

	private void updateView() {
		actual = actEntry.getAttachment();
		defaultUpdatePrevious();
		update();
	}

	/** Helper method: updates the reference of the previous beliefbase */
	private void defaultUpdatePrevious() {
		if(ref == null)	return;
		List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(ref);
		int index = entries.indexOf(actEntry) - 1;

		if(index >= 0) {
			ReportEntry prevEntry = entries.get(index);
			previous = prevEntry.getAttachment();
		} else {
			previous = null;
		}
	}
	
	/**
	 * sub classes should set the ref and actual member variable and proof if the type of obj
	 * is correct.
	 */
	@Override
	public abstract void setObservationObject(Object obj);
	
	@Override
	public Entity getAttachment() {
		return ref;
	}

	@Override
	public ReportEntry getCurrentEntry() {
		return actEntry;
	}

	@Override
	public void setCurrentEntry(ReportEntry entry) {
		reportReceived(entry);
	}

}
