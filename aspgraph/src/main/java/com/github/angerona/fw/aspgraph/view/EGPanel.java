package com.github.angerona.fw.aspgraph.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

import angerona.fw.aspgraph.graphs.EGEdge;
import angerona.fw.aspgraph.graphs.EGVertex;
import angerona.fw.aspgraph.graphs.ExplanationGraph;

public class EGPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1411923116464648892L;
	
	private Layout<EGVertex,EGEdge> layout;
	
	private BasicVisualizationServer<EGVertex, EGEdge> visServer;

	private ExplanationGraph eg;

	public EGPanel(ExplanationGraph eg){
		this.eg = eg;
		this.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		this.setPreferredSize(new Dimension(600,400));
		/* Visualizing the graph */
	    layout = new ISOMLayout<EGVertex,EGEdge>(eg);
	    layout.setSize(new Dimension(600,400));
	    visServer = new BasicVisualizationServer<EGVertex, EGEdge>(layout);
	    visServer.setPreferredSize(new Dimension(600,400));
	    add(visServer);
	    
	     /* Formatting nodes and edges */
	     Transformer<EGVertex,Shape> vertexSize = new Transformer<EGVertex,Shape>(){
	            public Shape transform(EGVertex v){
	            	Font font = visServer.getFont();
	            	Rectangle2D rect = font.getStringBounds(v.pureString(), ((Graphics2D) visServer.getGraphics()).getFontRenderContext());
	            	double width = rect.getWidth();
	            	double height = rect.getHeight();
	                Ellipse2D circle = new Ellipse2D.Double(-15, -15, width+20, height+20);
	                return circle;
	            }
	        };
		
		Transformer<EGEdge, Font> edgeFont = new Transformer<EGEdge, Font>(){
			public Font transform(EGEdge arg0) {
				return new Font("Arial", 0, 14);
			}	
		};
		
		Transformer<EGVertex, Paint> vertexPaint = new Transformer<EGVertex, Paint>() {
			public Paint transform(EGVertex v) {
				return Color.WHITE;
			}
		};
		
		Transformer<Context<Graph<EGVertex, EGEdge>, EGEdge>, Number> edgeLabelCloseness = new Transformer<Context<Graph<EGVertex, EGEdge>, EGEdge>, Number>(){

			@Override
			public Number transform(Context<Graph<EGVertex, EGEdge>, EGEdge> arg0) {
				return 0.5;
			}
		};
		
		Transformer<EGEdge, Paint> edgePaint = new Transformer<EGEdge, Paint>(){

			@Override
			public Paint transform(EGEdge e) {
				if (e.getLabel().equals(EGEdge.EdgeType.POS)) return Color.GREEN;
				else return Color.RED;
			}
		};
		
		visServer.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		visServer.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
		visServer.getRenderContext().setEdgeLabelClosenessTransformer(edgeLabelCloseness);
		visServer.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<EGVertex>());
		visServer.getRenderContext().setVertexShapeTransformer(vertexSize);
		visServer.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<EGEdge>());
		visServer.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		visServer.getRenderContext().setEdgeFontTransformer(edgeFont);
		visServer.getRenderContext().setArrowFillPaintTransformer(edgePaint);
		visServer.getRenderContext().setArrowDrawPaintTransformer(edgePaint);
		visServer.getRenderContext().setArrowPlacementTolerance(1);
	}
	
	public void setLayout(Layout<EGVertex,EGEdge> l){
		layout = l;
		l.setSize(new Dimension(600,400));
		visServer.setGraphLayout(l);
		visServer.doLayout();
	}
	
	public ExplanationGraph getEG(){
		return eg;
	}
}
