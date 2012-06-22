package angerona.fw.aspgraph.view;


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import angerona.fw.aspgraph.controller.EDGController;
import angerona.fw.aspgraph.controller.EGController;
import angerona.fw.aspgraph.graphs.EGEdge;
import angerona.fw.aspgraph.graphs.EGVertex;
import angerona.fw.aspgraph.graphs.ExplanationGraph;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;
import angerona.fw.aspgraph.view.util.SteppedComboBox;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;

import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;

/**
 * Main view for presenting Explanation Graphs
 * @author ella
 *
 */
public class EGView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6142473502907874961L;

	/**
	 * Panel on which the graphs with related to a selected answer set, an assumption and a literal are arranged
	 */
	private JPanel graphPanel;
	
	/**
	 * Combobox where the answer set can be selected
	 */
	private SteppedComboBox<AnswerSet> answerSetsBox;
	
	/**
	 * Combobox where the assumption can be selected
	 */
	private SteppedComboBox<Set<String>> assumptionBox;
	
	/**
	 * Combobox where the literal can be selected
	 */
	private SteppedComboBox<String> literalBox;
	
	/**
	 * Combobox where the layout for arranging the nodes can be selected
	 */
	private JComboBox<String> layoutBox;
	
	/**
	 * Layout for arranging the nodes
	 */
	Layout<EGVertex, EGEdge> layout;
	
	/**
	 * A single Explanation Graph is presented on each panel
	 */
	private Set<EGPanel> egPanels;
	
	/**
	 * Scrollpane for the EGPanels
	 */
	private JScrollPane scrollpane;
	
	/**
	 * Creates an empty view
	 */
	public EGView(){};
	
	/**
	 * Initializes the components of the view
	 */
	public void initComponents(){
		egPanels = new HashSet<EGPanel>();
			
	    /* Create panel for options */
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.X_AXIS));
		selectionPanel.setMaximumSize(new Dimension(800,20));
		
		/* Answer set selection */
		JLabel aspLabel = new JLabel("Answer Set: ");
		aspLabel.setAlignmentX(RIGHT_ALIGNMENT);
		answerSetsBox = new SteppedComboBox<AnswerSet>();		
		selectionPanel.add(aspLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(answerSetsBox);		

		/* Assumption selection */
		selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
		JLabel assumptionLabel = new JLabel("Assumption: ");
		assumptionLabel.setAlignmentX(RIGHT_ALIGNMENT);
		assumptionBox = new SteppedComboBox<Set<String>>();
		selectionPanel.add(assumptionLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(assumptionBox);
		
		assumptionBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				if (assumptionBox.getItemCount() > 0) updateGraphPanel();
				repaint();
			}
		});
		
		/* Literal selection */
		selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
		JLabel literalLabel = new JLabel("Literal: ");
		literalLabel.setAlignmentX(RIGHT_ALIGNMENT);
		literalBox = new SteppedComboBox<String>();
		selectionPanel.add(literalLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(literalBox);
		ExtendedDependencyGraph edg = EDGController.instance().getEDG(null);
		for (String literal : edg.getNodeMap().keySet()){
			literalBox.addItem(literal);
		}
		literalBox.setSelectedIndex(0);
		Dimension d = literalBox.getPreferredSize();
		literalBox.setPreferredSize(new Dimension(50,d.height));
		literalBox.setPopupWidth(d.width);
		literalBox.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				updateGraphPanel();	
				repaint();
			}
			
		});
		
		/* Layout selection */
		selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
		JLabel layoutLabel = new JLabel("Layout: ");
		layoutLabel.setAlignmentX(RIGHT_ALIGNMENT);
		layoutBox = new JComboBox<String>();
		selectionPanel.add(layoutLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));	
		selectionPanel.add(layoutBox);
		initLayoutBox();		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(selectionPanel);	
		
		graphPanel = new JPanel();
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.X_AXIS));
		scrollpane = new JScrollPane(graphPanel);
		scrollpane.setPreferredSize(new Dimension(800,420));
		add(scrollpane);
	}
	
	/**
	 * Fills answer set combobox with answer sets 
	 * @param list List with answer sets
	 */
	public void setAnswerSets(AnswerSetList list){
		for (AnswerSet as : list){
			answerSetsBox.addItem(as);
		}
		
		Dimension d = answerSetsBox.getPreferredSize();
	    answerSetsBox.setPreferredSize(new Dimension(170, d.height));
	    answerSetsBox.setPopupWidth(d.width);
		answerSetsBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JComboBox<AnswerSet> cb = (JComboBox<AnswerSet>) e.getSource();
		        AnswerSet as = (AnswerSet) cb.getSelectedItem();
		        EGController egController = EGController.instance();
		        assumptionBox.removeAllItems();
		        Set<Set<String>> assumptions =  egController.getAssumptions(as);
		        for (Set<String> assumption : assumptions){
		        	assumptionBox.addItem(assumption);
		        }
		        if (assumptionBox.getItemCount() > 0) assumptionBox.setSelectedIndex(0);
		        Dimension d = assumptionBox.getPreferredSize();
		        assumptionBox.setPreferredSize(new Dimension(100,d.height));
		        assumptionBox.setPopupWidth(d.width);
		        updateGraphPanel();
			}
			
		});
		answerSetsBox.setSelectedIndex(0);
	}
	
	/**
	 * Initializes combobox for layout
	 */
	private void initLayoutBox(){
		layoutBox.addItem("CircleLayout");
		layoutBox.addItem("FRLayout");
		layoutBox.addItem("FRLayout2");
		layoutBox.addItem("ISOMLayout");
		layoutBox.addItem("KKLayout");
		
		layoutBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				JComboBox<String> lBox = (JComboBox<String>) a.getSource();
				String name = (String) lBox.getSelectedItem();
				for (EGPanel e : egPanels){
					ExplanationGraph eg = e.getEG();
					switch(name){
					case "CircleLayout": layout = new CircleLayout<EGVertex, EGEdge>(eg); break;
					case "FRLayout": layout = new FRLayout<EGVertex, EGEdge>(eg); break;
					case "FRLayout2": layout = new FRLayout<EGVertex, EGEdge>(eg);  break;
					case "KKLayout": layout = new KKLayout<EGVertex, EGEdge>(eg); break;
					default : layout = new ISOMLayout<EGVertex, EGEdge>(eg); break;
					}
					e.setLayout(layout);
				}			
			}});
	}
	
	/**
	 * Updates graph panels when changing answer set, assumption or literal
	 */
	private void updateGraphPanel(){
		/* Clean up graphPanel */
		remove(scrollpane);
		graphPanel.removeAll();
		egPanels = new HashSet<EGPanel>();
		
		AnswerSet as = (AnswerSet) answerSetsBox.getSelectedItem();
		Set<String> assumption = (Set<String>) assumptionBox.getSelectedItem();
		String literal = (String) literalBox.getSelectedItem();
		EGController egContr = EGController.instance();
		
		/* Construct new EGPanels */
		Set<ExplanationGraph> egs = egContr.getEGs(as, assumption, literal);
		for (ExplanationGraph eg : egs){
			EGPanel egPanel = new EGPanel(eg);
			graphPanel.add(egPanel);
			egPanels.add(egPanel);
			egPanel.revalidate();
		}
		
		scrollpane = new JScrollPane(graphPanel);
		scrollpane.setPreferredSize(new Dimension(800,420));
		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollpane);
		revalidate();
	}
	
	/**
	 * Emptys view and shows a hint that no explanation graphs can be created
	 */
	public void setNoAnswerSet(){
		removeAll();
		add(new JLabel("Program has no answer sets. Construction of Explanation Graphs not possible."));
	}

}
