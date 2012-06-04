package angerona.fw.aspgraph.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import angerona.fw.gui.view.BaseView;

public class GraphView extends BaseView {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6909762863850487692L;

	private EDGView edgPanel;
	private EGView egPanel;
	private static GraphView instance;
	
	private GraphView() {
		JTabbedPane tabbedPane = new JTabbedPane();
		edgPanel = new EDGView();
		egPanel = new EGView();
		tabbedPane.add("Extended Dependency Graph", edgPanel);
		tabbedPane.add("Explanation Graphs", egPanel);
		add(tabbedPane);
	}
	
	public static GraphView instance(){
		if (instance == null) instance = new GraphView();
		return instance;
	}
	
	public void setEDGPanel(EDGView edgv){
		edgPanel = edgv;
		repaint();
	}
	
	public void setEGPanel(EGView egv){
		egPanel = egv;
	}
	
	public EDGView getEDGPanel(){
		return edgPanel;
	}
	
	public EGView getEGPanel(){
		return egPanel;
	}
}
