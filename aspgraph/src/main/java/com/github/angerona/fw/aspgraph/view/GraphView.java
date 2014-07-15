package com.github.angerona.fw.aspgraph.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.github.angerona.fw.gui.view.BeliefbaseView;

import net.sf.tweety.lp.asp.syntax.Program;

import com.github.angerona.fw.gui.view.BeliefbaseView;

public class GraphView extends BeliefbaseView {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6909762863850487692L;

	private EDGView edgPanel;
	private EGView egPanel;
	private JPanel programPanel;
	
	public GraphView() {
		this.setLayout(new BorderLayout());
		JTabbedPane tabbedPane = new JTabbedPane();
		edgPanel = new EDGView();
		egPanel = new EGView();
		programPanel = new JPanel();
		tabbedPane.add("Extended Dependency Graph", edgPanel);
		tabbedPane.add("Explanation Graphs", egPanel);
		tabbedPane.add("Program", programPanel);
		this.add(tabbedPane, BorderLayout.CENTER);
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
