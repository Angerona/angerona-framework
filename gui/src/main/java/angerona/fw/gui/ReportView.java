package angerona.fw.gui;

import java.awt.BorderLayout;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import angerona.fw.Angerona;
import angerona.fw.util.ReportListener;
import angerona.fw.util.ReportPoster;

public class ReportView extends BaseComponent implements ReportListener {

	/** kill warning */
	private static final long serialVersionUID = 697392233654570429L;

	PipedInputStream piOut;
    PipedInputStream piErr;
    PipedOutputStream poOut;
    PipedOutputStream poErr;
    JTextArea textArea = new JTextArea();

    JScrollPane pane;

	public ReportView() {
		super("Report");
		
        // Add a scrolling text area
        textArea.setEditable(false);
        textArea.setRows(20);
        textArea.setColumns(50);

        pane = new JScrollPane(textArea);
        add(pane, BorderLayout.CENTER);
        setVisible(true);
        
        Angerona.getInstance().addReportListener(this);
	}

	@Override
	public void reportReceived(String msg, ReportPoster sender, Object attachment) {
		String txt = textArea.getText();
		txt += (txt == "" ? "" : "\n") + sender.getPosterName()+":"+sender.getSimTick()+" - " + msg;
		textArea.setText(txt);
	}


}
