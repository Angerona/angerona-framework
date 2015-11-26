package com.github.kreaturesfw.gui.view;

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

import com.github.kreaturesfw.core.Angerona;
import com.github.kreaturesfw.core.report.ReportWikiGenerator;
import com.github.kreaturesfw.core.util.Utility;
import com.github.kreaturesfw.gui.AngeronaWindow;
import com.github.kreaturesfw.gui.base.ViewComponent;
import com.github.kreaturesfw.gui.controller.ReportTreeController;

import bibliothek.gui.dock.DefaultDockable;

/**
 * shows the reports of the actual simulation in a list-view.
 * @author Tim Janus
 */
@SuppressWarnings("deprecation")
public class ReportView extends JPanel implements ViewComponent {
    
	private static final long serialVersionUID = -3467235894575987510L;

	private JTree tree;

	private boolean showDetailsFlag = true;
	
	private ReportTreeController controller;
	
	public ReportView() {
    	tree = new JTree();
    	if(Utility.presentationMode) {
    		tree.setFont(tree.getFont().deriveFont(16f)); 
    	}
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
