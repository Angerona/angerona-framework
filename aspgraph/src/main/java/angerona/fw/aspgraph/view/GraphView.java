package angerona.fw.aspgraph.view;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;

import angerona.fw.gui.view.BaseView;

public class GraphView extends BaseView {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6909762863850487692L;

	private EDGView edgPanel;
	private EGView egPanel;
	private JPanel programPanel;
	
	public GraphView() {
		JTabbedPane tabbedPane = new JTabbedPane();
		edgPanel = new EDGView();
		egPanel = new EGView();
		programPanel = new JPanel();
		tabbedPane.add("Extended Dependency Graph", edgPanel);
		tabbedPane.add("Explanation Graphs", egPanel);
		tabbedPane.add("Program", programPanel);
		add(tabbedPane);
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
	
	public void setProgramToPanel(Program p){
		String text ="<html>";
		String pText = p.toString();
		pText = pText.replace(":-", "&larr;");
		pText = pText.replace("-", "&not;");
		pText = pText.replace(".", ".<br />");
		text += pText;
		text += "</html>";
		programPanel.add(new JLabel(text));
	}
}
