package angerona.fw.aspgraph.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.collections15.Transformer;

import angerona.fw.aspgraph.controller.EDGController;
import angerona.fw.aspgraph.controller.EGController;
import angerona.fw.aspgraph.graphs.EDGEdge;
import angerona.fw.aspgraph.graphs.EDGVertex;
import angerona.fw.aspgraph.graphs.EGEdge;
import angerona.fw.aspgraph.graphs.EGVertex;
import angerona.fw.aspgraph.graphs.ExplanationGraph;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;

import edu.uci.ics.jung.algorithms.layout.BalloonLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.DAGLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.layout.CachingLayout;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import edu.uci.ics.jung.visualization.renderers.VertexLabelRenderer;

import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;


public class EGView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6142473502907874961L;

	private JPanel graphPanel;
	private JComboBox<AnswerSet> answerSetsBox;
	private JComboBox<Set<String>> assumptionBox;
	private JComboBox<String> literalBox;
	private JComboBox<String> layoutBox;
	private ExtendedDependencyGraph edg;
	Layout<EGVertex, EGEdge> l;
	private Set<EGPanel> egPanels;
	private JScrollPane scrollpane;
	
	public EGView(){
		
	}
	
	public void initComponents(){
		graphPanel = new JPanel();
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.X_AXIS));
		scrollpane = new JScrollPane(graphPanel);
		scrollpane.setPreferredSize(new Dimension(800,400));
		edg = EDGController.instance().getEDG(null);
		egPanels = new HashSet<EGPanel>();
		
	    /* Create panel for options */
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.X_AXIS));
		selectionPanel.setMaximumSize(new Dimension(800,20));
		
		JLabel aspLabel = new JLabel("Answer Set: ");
		aspLabel.setAlignmentX(RIGHT_ALIGNMENT);
		answerSetsBox = new JComboBox<AnswerSet>();
		answerSetsBox.setMaximumSize(new Dimension(100,20));
		answerSetsBox.setPreferredSize(new Dimension(100,20));
		selectionPanel.add(aspLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(answerSetsBox);		

		selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
		JLabel assumptionLabel = new JLabel("Assumption: ");
		assumptionLabel.setAlignmentX(RIGHT_ALIGNMENT);
		assumptionBox = new JComboBox<Set<String>>();
		assumptionBox.setMaximumSize(new Dimension(100,20));
		selectionPanel.add(assumptionLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(assumptionBox);
		
		assumptionBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent a) {
				if (assumptionBox.getItemCount() > 0) updateGraphPanel();
				repaint();
			}
		});
		
		selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
		JLabel literalLabel = new JLabel("Literal: ");
		literalLabel.setAlignmentX(RIGHT_ALIGNMENT);
		literalBox = new JComboBox<String>();
		literalBox.setMaximumSize(new Dimension(50,20));
		selectionPanel.add(literalLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(literalBox);
		for (String literal : edg.getNodeMap().keySet()){
			literalBox.addItem(literal);
		}
		literalBox.setSelectedIndex(0);
		literalBox.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				updateGraphPanel();	
				repaint();
			}
			
		});
		
		selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
		JLabel layoutLabel = new JLabel("Layout: ");
		layoutLabel.setAlignmentX(RIGHT_ALIGNMENT);
		layoutBox = new JComboBox<String>();
		layoutBox.setMaximumSize(new Dimension(100,20));
		selectionPanel.add(layoutLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));	
		selectionPanel.add(layoutBox);
		initLayoutBox();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(selectionPanel);
		add(scrollpane);
	}
	
	public void setAnswerSets(AnswerSetList list){
		for (AnswerSet as : list){
			answerSetsBox.addItem(as);
		}
		answerSetsBox.setSelectedIndex(0);
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
		        assumptionBox.setSelectedIndex(0);
		        updateGraphPanel();
		        repaint();
			}
			
		});
	}
	
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
					case "CircleLayout": l = new CircleLayout<EGVertex, EGEdge>(eg); break;
					case "FRLayout": l = new FRLayout<EGVertex, EGEdge>(eg); break;
					case "FRLayout2": l = new FRLayout<EGVertex, EGEdge>(eg);  break;
					case "KKLayout": l = new KKLayout<EGVertex, EGEdge>(eg); break;
					default : l = new ISOMLayout<EGVertex, EGEdge>(eg); break;
					}
					e.setLayout(l);
				}			
			}});
	}
	
	private void updateGraphPanel(){
		remove(scrollpane);
		graphPanel.removeAll();
		egPanels = new HashSet<EGPanel>();
		AnswerSet as = (AnswerSet) answerSetsBox.getSelectedItem();
		Set<String> assumption = (Set<String>) assumptionBox.getSelectedItem();
		String literal = (String) literalBox.getSelectedItem();
		EGController egContr = EGController.instance();
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

}
