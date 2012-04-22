package angerona.fw.gui.view;

import java.awt.BorderLayout;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import angerona.fw.Agent;
import angerona.fw.Angerona;
import angerona.fw.internal.Entity;
import angerona.fw.internal.IdGenerator;
import angerona.fw.report.ReportEntry;
import angerona.fw.report.ReportListener;

public class ReportView extends BaseView implements ReportListener {

	/** kill warning */
	private static final long serialVersionUID = 697392233654570429L;

	/** the list containing all the report entries */
    JList<String> reportList = new JList<String>();
    
    private DefaultListModel<String> reportModel = new DefaultListModel<String>();
    
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
		String prefix = "<Sim> ";
		Entity attach = entry.getAttachment();
		if(attach != null) {
			while(attach.getParent() != null) {
				attach = IdGenerator.getEntityWithId(attach.getParent());
			}
			if(attach instanceof Agent) {
				Agent ag = (Agent)attach;
				prefix = "<"+ag.getName()+"> ";
			}
		}
		reportModel.add(0, prefix + entry.getMessage() + " from " + entry.getPoster().getPosterName());
	}

	@Override
	public String getComponentTypeName() {
		return "Report-List Component";
	}
}
