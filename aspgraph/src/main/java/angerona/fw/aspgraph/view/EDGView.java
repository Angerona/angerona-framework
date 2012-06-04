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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import angerona.fw.aspgraph.controller.EDGController;
import angerona.fw.aspgraph.graphs.EDGEdge;
import angerona.fw.aspgraph.graphs.EDGVertex;
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


public class EDGView extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6142473502907874961L;

	private JPanel graphPanel;
	private JComboBox<AnswerSet> answerSetsBox;
	private JComboBox<String> layoutBox;
	private BasicVisualizationServer<EDGVertex, EDGEdge> visServer;
	private ExtendedDependencyGraph edg;
	Layout<EDGVertex, EDGEdge> l;
	
	public EDGView(){
		
	}
	
	public void initComponents(){
		JLabel aspLabel = new JLabel("Answer Set: ");
		aspLabel.setAlignmentX(RIGHT_ALIGNMENT);
		answerSetsBox = new JComboBox<AnswerSet>();
		layoutBox = new JComboBox<String>();
		graphPanel = new JPanel();
		edg = EDGController.instance().getEDG(null);
		initLayoutBox();

		/* Visualisierung des Graphen */
	    l = new ISOMLayout<EDGVertex,EDGEdge>(edg);
	    //layout.setRepulsionMultiplier(1);
	    //layout.setAttractionMultiplier(1);
	    //layout.setMaxIterations(1000);
	    l.setSize(new Dimension(400,400)); // sets the initial size of the space
	    visServer = new BasicVisualizationServer<EDGVertex, EDGEdge>(l);
	    visServer.setPreferredSize(new Dimension(400,400));
		
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.X_AXIS));
		selectionPanel.add(aspLabel);
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(answerSetsBox);
		selectionPanel.add(Box.createRigidArea(new Dimension(10,0)));
		selectionPanel.add(new JLabel("Layout: "));
		selectionPanel.add(Box.createRigidArea(new Dimension(5,0)));
		selectionPanel.add(layoutBox);
		graphPanel.add(visServer);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(graphPanel);
		add(selectionPanel);
		
	     /* Formatierung der Knoten und Kanten anpassen */
		Transformer<EDGVertex, Paint> vertexPaint = new Transformer<EDGVertex, Paint>() {
			public Paint transform(EDGVertex v) {
				if (v.getColor() != null){
					switch(v.getColor()){
					case RED : return Color.RED;
					case GREEN : return Color.GREEN;
					default: return Color.WHITE;
					}
				} else return Color.WHITE;
			}
		};
	     Transformer<EDGVertex,Shape> vertexSize = new Transformer<EDGVertex,Shape>(){
	            public Shape transform(EDGVertex v){
	            	Font font = visServer.getFont();
	            	Rectangle2D rect = font.getStringBounds(v.pureString(), ((Graphics2D) visServer.getGraphics()).getFontRenderContext());
	            	double width = rect.getWidth();
	            	double height = rect.getHeight();
	                Ellipse2D circle = new Ellipse2D.Double(-15, -15, width+13, height+13);
	                // in this case, the vertex is twice as large
	                return circle;
	            }
	        };
		
		Transformer<EDGEdge, Font> edgeFont = new Transformer<EDGEdge, Font>(){
			public Font transform(EDGEdge arg0) {
				return new Font("Arial", 1, 14);
			}	
		};
		
		visServer.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		visServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<EDGVertex>());
		visServer.getRenderContext().setVertexShapeTransformer(vertexSize);
		visServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<EDGEdge>());
		visServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		visServer.getRenderContext().setEdgeFontTransformer(edgeFont);
	}
	
	public void setAnswerSets(AnswerSetList list){
		for (AnswerSet as : list){
			answerSetsBox.addItem(as);
		}
		answerSetsBox.addItem(null);
		answerSetsBox.setSelectedItem(null);
		answerSetsBox.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				JComboBox<AnswerSet> cb = (JComboBox<AnswerSet>) e.getSource();
		        AnswerSet as = (AnswerSet) cb.getSelectedItem();
		        edg = EDGController.instance().getEDG(as);
		        l.setGraph(edg);
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
				switch(name){
				case "CircleLayout": l = new CircleLayout<EDGVertex, EDGEdge>(edg); break;
				case "FRLayout": l = new FRLayout<EDGVertex, EDGEdge>(edg); break;
				case "FRLayout2": l = new FRLayout<EDGVertex, EDGEdge>(edg);  break;
				case "KKLayout": l = new KKLayout<EDGVertex, EDGEdge>(edg); break;
				default : l = new ISOMLayout<EDGVertex, EDGEdge>(edg); break;
				}
				visServer.setGraphLayout(l);
				visServer.doLayout();
			}			
		});
	}

}
