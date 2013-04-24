package angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.AgentComponent;
import angerona.fw.Angerona;
import angerona.fw.gui.base.EntityViewComponent;
import angerona.fw.gui.nav.NavigationPanel;
import angerona.fw.gui.nav.NavigationUser;
import angerona.fw.internal.Entity;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

/**
 * Base class for UI-Views which show a colored list of their content.
 * For example the atoms in a belief base.
 * @author Tim Janus
 *
 * @param <T> the type of the observed object
 */
public abstract class ListViewColored<T extends Entity> 
	extends EntityViewComponent<T>
	implements 
	ReportListener, 
	NavigationUser {

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ListViewColored.class);
	
	/** kill warning */
	private static final long serialVersionUID = 5572343160200460695L;
	
	/** reference to the data instance which is actually shown. */
	protected T actual;
	
	/** reference to the predecessor data instance of the actual data instance, this will be null if actual is the first */
	protected T previous;
	
	/** JList containing the literals of the actual belief base and the literals which were removed in the last step (removed and new literals are highlighted) */
	private JList<ListElement> actualLiterals;
	
	/** The data model to accessing the JList */
	private DefaultListModel<ListElement> model;
	
	private JTree callstackTree;
	
	/** reference to the actually showed report entry. */
	private ReportEntry actEntry;
	
	protected NavigationPanel navPanel;
	
	private DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("operator-callstack:");
	
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
		
		public static final int ST_RESERVED = 4;
		
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
	private class ListRenderer extends JLabel implements ListCellRenderer<ListElement> {

		/** kill warning */
		private static final long serialVersionUID = -6867522427499396635L;

		public ListRenderer() {
			setOpaque(true);
		}
		
		@Override
		public Component getListCellRendererComponent(JList<? extends ListViewColored<T>.ListElement> list, 
				ListViewColored<T>.ListElement le, int index2, boolean isSelected, boolean cellHasFocus) {
			setText(le.toString());
			setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);
			setForeground(le.status == ListElement.ST_NEW ? new Color(0,128,0) : (le.status == ListElement.ST_DELETED ? Color.red : Color.BLACK));
			return this;
		}
		
	}
	
	@Override 
	public void init() {
		if(ref != null) {
			List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(ref);
			if(entries != null && entries.size() > 0) {
				actEntry = entries.get(entries.size()-1);
			} else {
				LOG.info("No entry added to report system yet.");
			}
		} else {
			throw new IllegalArgumentException("The attribute ref must not be null.");
		}
		
		this.setLayout(new BorderLayout());
		navPanel = new NavigationPanel(this);
		add(navPanel, BorderLayout.NORTH);
		navPanel.setEntry(actEntry);
		
		actualLiterals = new JList<ListElement>();
		actualLiterals.setCellRenderer(new ListRenderer());
		this.add(actualLiterals, BorderLayout.CENTER);
		
		model = new DefaultListModel<ListElement>();
		actualLiterals.setModel(model);	
		actualLiterals.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent evt) {
				if(evt.getClickCount() >= 2) {
					int index = actualLiterals.locationToIndex(evt.getPoint());
					onElementClicked(index, actualLiterals.getModel().getElementAt(index).status);
				}
			}
		});
		
		callstackTree = new JTree();
		this.add(callstackTree, BorderLayout.SOUTH);
		
		updateView();
		Angerona.getInstance().addReportListener(this);
	}
	
	@Override
	public void cleanup() {
		
	}
	
	protected void onElementClicked(int index, int status) {	}
	
	protected abstract List<String> getStringRepresentation(Entity obj);
	
	/**
	 * Helper method: updates the content of the JList containing the literals:
	 * Shows the change set of the belief base
	 */
	protected void update(DefaultListModel<ListElement> model) {	
		if(ref == null)	return;
		
		updateBeliefbaseOutput(model);
	}

	/**
	 * Helper method: Updates the output of the belief base and puts the output in the
	 * given list model.
	 * @param model
	 */
	protected void updateBeliefbaseOutput(DefaultListModel<ListElement> model) {
		// prepare for changeset.
		List<String> actual = getStringRepresentation(this.actual);
		List<String> last = previous == null ? null : getStringRepresentation(this.previous);
		
		// Show the changeset in the entity:
		model.clear();		
		for(String atom : actual) {
			if(last != null && !last.contains(atom)) {
				model.addElement(new ListElement(atom, ListElement.ST_NEW));
			} else {
				model.addElement(new ListElement(atom, ListElement.ST_NOTCHANGED));
			}
		}
		
		// at the end of the list show all rules which are removed by this step
		if(last != null) {
			for(String atom : last) {
				if(!actual.contains(atom)) {
					model.addElement(new ListElement(atom, ListElement.ST_DELETED));
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
				//if(isVisible()) {
					updateView();
					fillTreeWithCallstack();
				//}
			}
		}
	}
	
	private void fillTreeWithCallstack() {
		DefaultTreeModel dtm = (DefaultTreeModel)callstackTree.getModel();
		dtm.setRoot(rootNode);
		DefaultMutableTreeNode dmtn = rootNode;
		dmtn.removeAllChildren();
		
		for(String val : actEntry.getStack()) {
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(val);
			dmtn.add(newNode);
		}
		
		callstackTree.repaint();
	}

	private void updateView() {
		actual = (T)actEntry.getAttachment();
		defaultUpdatePrevious();
		update(model);
		fillTreeWithCallstack();
	}

	/** Helper method: updates the reference of the previous beliefbase */
	private void defaultUpdatePrevious() {
		if(ref == null)	return;
		List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(ref);
		int index = entries.indexOf(actEntry) - 1;

		AgentComponent actAtomic = (AgentComponent)actEntry.getAttachment();
		while(index >= 0) {
			ReportEntry prevEntry = entries.get(index);
			AgentComponent temp = ((AgentComponent)prevEntry.getAttachment());
			
			// select the first predecessor which has the same or a lesser copy depth as the
			// current one as the 'real' previous entry.
			if(temp.getCopyDepth() < actAtomic.getCopyDepth() || 
					(temp.getCopyDepth() == 1 && actAtomic.getCopyDepth() == 1)) {
				previous = (T) temp;
				break;
			}
			
			if(index == 0)
				previous = null;
			--index;
		}
	}
	
	/**
	 * sub classes should set the ref and actual member variable and proof if the type of obj
	 * is correct.
	 */
	@Override
	public void setObservedEntity(T ent) {
		this.ref = ent;
		this.actual = ent;
		this.previous = null;
	}
	
	@Override
	public void setObservedEntity(Object entity) {
		setObservedEntity((T) entity);
	}	
	
	@Override
	public T getObservedEntity() {
		return (T)ref;
	}
	
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
