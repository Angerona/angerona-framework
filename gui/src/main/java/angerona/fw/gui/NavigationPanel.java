package angerona.fw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
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
	
	/** button to perfom a complete fast-forward (select the last report-entry of the observer report-attachment */
	private JButton btnForward;
	
	/** reference to the navigation user object which is responsible for the communication between the NavigationPanel and its holding component */
	private NavigationUser user;
	
	public NavigationPanel(NavigationUser nu) {
		this.user = nu;
		btnRewind = new JButton("<--|");
		btnRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				if(entries != null) {
					if(entries.size() > 0)
						user.setCurrentEntry(entries.get(0));
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
						user.setCurrentEntry(entries.get(index));
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
						user.setCurrentEntry(entries.get(index));
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
						user.setCurrentEntry(entries.get(entries.size()-1));
				} else {
					LOG.warn("Cannot find report-entries for: {} with id #{}", 
							user.getAttachment().getClass().getSimpleName(), user.getAttachment().getGUID());
				}
			}
		});
		add(btnForward);
		
	}
}
