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
import angerona.fw.report.ReportEntry;

/**
 * The Navigation Panel is a generic panel which allows components
 * to navigate to the report-history. The component must provide
 * a NavigationUser object which is used by the NavigationPanel for
 * communication and selecting the correct ReportAttachments.
 * @author Tim Janus
 *
 */
public class NavigationPanel extends JPanel {

	private static Logger LOG = LoggerFactory.getLogger(Angerona.class);
	
	/** kill warning */
	private static final long serialVersionUID = -5215434068009560588L;

	/** button to perform one time step back */
	private JButton btnStepBack;
	
	/** button to perform one time step forward */
	private JButton btnStepForward;
	
	/** button to perform a complete rewind (begin by the first report entry of the observed report-attachment */
	private JButton btnRewind;
	
	/** button to perform a complete fast-forward (select the last report-entry of the observer report-attachment */
	private JButton btnForward;
	
	/** A Label containing text in the form "Tick: 2/5 - Entry: 2/3" helping the user to navigate through the simulation */
	private JLabel lblTimeline;
	
	/** reference to the current selected report-entry of the NavigationPanel */
	private ReportEntry currentEntry;
	
	/** reference to the navigation user object which is responsible for the communication between the NavigationPanel and its holding component */
	private NavigationUser user;
	
	public NavigationPanel(NavigationUser nu) {
		this.user = nu;
		
		lblTimeline = new JLabel("Tick xxxx/xxxx - Entry yyy/yyy");
		add(lblTimeline);
		
		btnRewind = new JButton("<--|");
		btnRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				if(entries != null) {
					if(entries.size() > 0)
						onReportEntryUpdate(entries, 0);
				} else {
					LOG.warn("Cannot find report-entries for: {} with id #{}", 
							user.getAttachment().getClass().getSimpleName(), user.getAttachment().getGUID());
				}
			}
		});
		add(btnRewind);
		
		btnStepBack = new JButton("<-");
		btnStepBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				if(entries != null) {
					int index = entries.indexOf(user.getCurrentEntry()) - 1;
					if(index >= 0)
						onReportEntryUpdate(entries, index);
				} else {
					LOG.warn("Cannot find report-entries for: {} with id #{}", 
							user.getAttachment().getClass().getSimpleName(), user.getAttachment().getGUID());
				}
			}
		});
		add(btnStepBack);
		
		btnStepForward = new JButton("->");
		btnStepForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				if(entries != null) {
					int index = entries.indexOf(user.getCurrentEntry()) + 1;
					if(index < entries.size())
						onReportEntryUpdate(entries, index);
				} else {
					LOG.warn("Cannot find report-entries for: {} with id #{}", 
							user.getAttachment().getClass().getSimpleName(), user.getAttachment().getGUID());	
				}
			}
		});
		add(btnStepForward);
		
		btnForward = new JButton("|-->");
		btnForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				if(entries != null) {
					if(entries.size() > 0)
						onReportEntryUpdate(entries, entries.size()-1);
				} else {
					LOG.warn("Cannot find report-entries for: {} with id #{}", 
							user.getAttachment().getClass().getSimpleName(), user.getAttachment().getGUID());
				}
			}
		});
		add(btnForward);
		
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
		int entriesBefore = 0;
		int entriesAfter = 0;
		
		for(int i=index-1; i>0; i--) {
			if(entries.get(i).getSimulationTick() == re.getSimulationTick()) {
				entriesBefore += 1;
			} else {
				break;
			}
		}
		
		for(int i=index+1; i<entries.size(); ++i) {
			if(entries.get(i).getSimulationTick() == re.getSimulationTick()) {
				entriesAfter += 1;
			} else {
				break;
			}
		}
		
		int curTick = re.getPoster().getSimulation().getSimulationTick() + 1;
		String txt = index+1 + "/" + entries.size() + " in Tick: " + (re.getSimulationTick() + 1) + "/" + curTick + " - InTick-Entry: ";
		txt += (entriesBefore+1) + "/" + (entriesBefore + entriesAfter + 1);
		lblTimeline.setText(txt);
		currentEntry = re;
		if(inform) {
			user.setCurrentEntry(re);
		}
	}
}
