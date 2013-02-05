package angerona.fw.gui.view;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTree;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.gui.controller.ReportTreeController;
import angerona.fw.gui.controller.TreeController;

/**
 * shows the reports of the actual simulation in a list-view.
 * @author Tim Janus
 */
public class ReportView extends BaseView {

	/** kick warning */
	private static final long serialVersionUID = 1L;

	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(ReportView.class);
    
	private JTree tree;

	private TreeController controller;
	
    @Override
	public void init() {
    	tree = new JTree();
		JScrollPane pane = new JScrollPane(tree);
		controller = new ReportTreeController(tree);

		setLayout(new BorderLayout());
		add(pane, BorderLayout.CENTER);
	}
    
    @Override
    public void cleanup() {}

	@Override
	public Class<?> getObservedType() {
		return null;
	}

}
