package angerona.fw.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import angerona.fw.Angerona;
import angerona.fw.report.ReportEntry;

public class NavigationPanel extends JPanel {

	/** kill warning */
	private static final long serialVersionUID = -5215434068009560588L;

	private JButton btnStepBack;
	
	private JButton btnStepForward;
	
	private JButton btnRewind;
	
	private JButton btnForward;
	
	private NavigationUser user;
	
	public NavigationPanel(NavigationUser nu) {
		this.user = nu;
		btnRewind = new JButton("<--|");
		btnRewind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				if(entries.size() > 0)
					user.setActualEntry(entries.get(0));
			}
		});
		add(btnRewind);
		
		btnStepBack = new JButton("<-");
		btnStepBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				int index = entries.indexOf(user.getActualEntry()) - 1;
				if(index >= 0)
					user.setActualEntry(entries.get(index));
			}
		});
		add(btnStepBack);
		
		btnStepForward = new JButton("->");
		btnStepForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				int index = entries.indexOf(user.getActualEntry()) + 1;
				if(index < entries.size())
					user.setActualEntry(entries.get(index));
			}
		});
		add(btnStepForward);
		
		btnForward = new JButton("|-->");
		btnForward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<ReportEntry> entries = Angerona.getInstance().getActualReport().getEntriesOf(user.getAttachment());
				if(entries.size() > 0)
					user.setActualEntry(entries.get(entries.size()-1));
			}
		});
		add(btnForward);
		
	}
}
