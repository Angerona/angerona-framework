package angerona.fw.gui;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import angerona.fw.Angerona;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

public class ReportView extends UIComponent implements ReportListener {

	/** kill warning */
	private static final long serialVersionUID = 697392233654570429L;

	/** the list containing all the report entries */
    JList reportList = new JList();
    
    private DefaultListModel reportModel = new DefaultListModel();
    
    @Override
	public void init() {
		setTitle("Report");
		
		setLayout(new BorderLayout());
		
		JLabel lbl = new JLabel("Reports");
		add(lbl, BorderLayout.NORTH);
		
		reportList.setModel(reportModel);
		JScrollPane pane = new JScrollPane(reportList);
        add(pane, BorderLayout.CENTER);
        setVisible(true);
                
        Angerona.getInstance().addReportListener(this);
	}

	@Override
	public void reportReceived(ReportEntry entry) {
		reportModel.add(0, entry.getMessage() + " from " + entry.getPoster().getPosterName());
	}

	@Override
	public String getComponentTypeName() {
		return "Report-List Component";
	}
}
