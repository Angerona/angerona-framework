package com.github.angerona.fw.gui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bibliothek.gui.dock.DefaultDockable;

import com.github.angerona.fw.Angerona;
import com.github.angerona.fw.gui.AngeronaWindow;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.gui.controller.ReportTreeController;
import com.github.angerona.fw.gui.controller.TreeControllerAdapter;
import com.github.angerona.fw.report.ReportWikiGenerator;

/**
 * shows the reports of the actual simulation in a list-view.
 * @author Tim Janus
 */
public class ReportView extends JPanel implements ViewComponent {

	/** kick warning */
	private static final long serialVersionUID = 1L;

	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(ReportView.class);
    
	private JTree tree;

	private boolean showDetailsFlag = true;
	
	private ReportTreeController controller;
	
	public ReportView() {
    	tree = new JTree();
    	JPanel treeViewContainer = new JPanel();
    	treeViewContainer.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(tree);
		controller = new ReportTreeController(tree);
		treeViewContainer.add(scrollPane, BorderLayout.CENTER);
	
		final JButton btn = new JButton("Hide Details");
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showDetailsFlag = !showDetailsFlag;
				btn.setText(showDetailsFlag ? "Hide Details" : "Show Details");
				controller.showDetail(showDetailsFlag);
			}
		});
		treeViewContainer.add(btn, BorderLayout.SOUTH);
		
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
		tabbedPane.addTab("TreeView", treeViewContainer);

		final JTextArea txtArea = new JTextArea();
		scrollPane = new JScrollPane(txtArea);
		tabbedPane.addTab("Wiki-Syntax", scrollPane);
		
		setLayout(new BorderLayout());
		add(tabbedPane, BorderLayout.CENTER);
		
		final ReportWikiGenerator rwg = new ReportWikiGenerator();
		rwg.addPropertyListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if(arg0.getPropertyName().equals("output")) {
					txtArea.setText(rwg.getOutput());
				}
			}
		});
		Angerona.getInstance().addReportListener(rwg);
	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}

	@Override
	public void decorate(DefaultDockable dockable) {
		dockable.setTitleText("Report");
		dockable.setTitleIcon(AngeronaWindow.get().getIcons().get("report"));
	}

}
