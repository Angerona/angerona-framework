package angerona.fw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Angerona;
import angerona.fw.gui.nav.NavigationControl;
import angerona.fw.report.ReportEntry;

/**
 * The Navigation Panel is a generic panel which allows components
 * to navigate to the report-history. The component must provide
 * a NavigationUser object which is used by the NavigationPanel for
 * communication and selecting the correct ReportAttachments.
 * @author Tim Janus
 *
 */
public class NavigationPanel extends JPanel implements ActionListener {

	private static Logger LOG = LoggerFactory.getLogger(Angerona.class);
	
	/** kill warning */
	private static final long serialVersionUID = -5215434068009560588L;
	
	/** reference to the current selected report-entry of the NavigationPanel */
	private ReportEntry currentEntry;
	
	/** reference to the navigation user object which is responsible for the communication between the NavigationPanel and its holding component */
	private NavigationUser user;
	
	private NavigationControl navAllEntries;
	
	private NavigationControl navTicks;
	
	private NavigationControl navTickEntries;
	
	public NavigationPanel(NavigationUser nu) {
		this.user = nu;
		
		add(new JLabel("Navigation:"));
		navAllEntries = (NavigationControl) add(new NavigationControl("Gesamte Einträge: "));
		navAllEntries.addActionListener(this);
		
		navTicks = (NavigationControl)add(new NavigationControl("Aktueller Tick: "));
		navTicks.setMin(0);
		navTicks.addActionListener(this);
		
		navTickEntries = (NavigationControl)add(new NavigationControl("Einträge im aktuellen Tick: "));
		navTickEntries.addActionListener(this);
	}
	
	/**
	 * Gives the owner of the NavigationPanel the ability to change the actual selected ReportEntry externally.
	 * The timeline will also be updated. If the given entry is not found an IllegalArgumentException is thrown.
	 * @param entry Reference to the entry which will become the current selected entry of the NavigationPanel
	 * 				The entry must be in the list of entries given by the observed attachment of the user.
	 */
	public void setEntry(ReportEntry entry) {
		if(entry == currentEntry)
			return;
		
		List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
		if(entries != null) {
			int index = entries.indexOf(entry);
			if(index != -1) {
				onReportEntryUpdate(entries, index, false);
			} else {
				throw new IllegalArgumentException();
			}
		} else {
			LOG.warn("Cannot find report-entries for: {} with id #{}", 
					user.getAttachment().getClass().getSimpleName(), user.getAttachment().getGUID());
		}
	}
	
	/**
	 * Helper method: 	Updates the current selected report entry of the user and the text in the 
	 * 					timeline label. It informs the user about the changes.
	 * @param entries	list containing all the report-entries of the attachment observed by the user
	 * @param index		the index of the newly selected report-entry.
	 */
	private void onReportEntryUpdate(List<ReportEntry> entries, int index) {
		 onReportEntryUpdate(entries, index, true);
	}
	
	/**
	 * Helper method:	Updates the current selected report entry of the user and the text in the 
	 * 					timeline label. It informs the user about the changes.
	 * @param entries	list containing all the report-entries of the attachment observed by the user
	 * @param index		the index of the newly selected report-entry.
	 * @param inform	flag indicating if the user-object is informed of the change of the report-entry.
	 */
	private void onReportEntryUpdate(List<ReportEntry> entries, int index, boolean inform) {
		
		ReportEntry re = entries.get(index);
		
		int first = -1;
		int last = -1;
		for(int i=0; i<entries.size(); ++i) {
			if(entries.get(i).getSimulationTick() == re.getSimulationTick()) {
				if(first == -1) {
					first = i;
				}
				last = i;
			} else if(last != -1 && first != -1){
				break;
			}
		}
		
		if(first == -1) {
			navTickEntries.set(0, 0, 0);
		} else {
			navTickEntries.set(1, index - first + 1, last - first + 1);
		}
		
		int curTick = re.getSimulation().getSimulationTick();
		currentEntry = re;
		navAllEntries.setNumbers(index+1, entries.size());
		navTicks.setNumbers(re.getSimulationTick(), curTick);
		
		if(inform) {
			user.setCurrentEntry(re);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton) {
			JButton btn = (JButton) e.getSource();
			int move = 0;
			if(e.getActionCommand().equals("forward")) {
				move = 1;
			} else if(e.getActionCommand().equals("backward")) {
				move = -1;
			}
			
			List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
			if(navAllEntries.isAncestorOf(btn) || navTickEntries.isAncestorOf(btn)) {
				if(entries != null) {
					int index = entries.indexOf(user.getCurrentEntry()) + move;
					if(index >= 0 && index < entries.size())
						onReportEntryUpdate(entries, index);
				} else {
					LOG.warn("Cannot find report-entries for: {} with id #{}", 
							user.getAttachment().getClass().getSimpleName(), user.getAttachment().getGUID());
				}
			} else if(navTicks.isAncestorOf(btn)) {
				int cur = user.getCurrentEntry().getSimulationTick() + move;
				for(int i=0; i<entries.size(); ++i) {
					ReportEntry entry = entries.get(i);
					if(entry.getSimulationTick() == cur) {
						onReportEntryUpdate(entries, i);
						break;
					}
				}
			}
		}
	}
}
