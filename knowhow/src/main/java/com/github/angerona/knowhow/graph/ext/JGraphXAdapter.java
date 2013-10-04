package com.github.angerona.knowhow.graph.ext;

import java.awt.Font;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import org.jgrapht.Graph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.event.GraphEdgeChangeEvent;
import org.jgrapht.event.GraphListener;
import org.jgrapht.event.GraphVertexChangeEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableDirectedGraph;

import com.github.angerona.fw.util.Utility;
import com.github.angerona.knowhow.graph.GraphNode;
import com.github.angerona.knowhow.graph.NodeListener;
import com.github.angerona.knowhow.graph.Processor;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.swing.handler.mxKeyboardHandler;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import com.mxgraph.view.mxGraph;

/**
 * It extends the mxGraph by supporting a ListenableGraph of the JGraphT package as
 * data model. It also adapts the editing behavior as the user is allowed to change
 * the view, that means he can move vertices but he cannot create new edges or disconnect
 * existing edges.
 * 
 * The idea was given by a user of the JGraph forum: {@link http
 * ://forum.jgraph.com/questions/133/using-jgraphx-to-visualize-jgrapht}
 * 
 * @author Tim Janus
 * 
 * @param <V>
 * @param <E>
 */
public class JGraphXAdapter<V extends GraphNode, E> 
	extends mxGraph 
	implements GraphListener<V, E>, NodeListener, Serializable {

	/** serial version id */
	private static final long serialVersionUID = 1776596628199375323L;

	/** the graph acting as data model */
	private ListenableGraph<V, E> graphT;

	/** a map from JGraphT vertices to mxGraph cells */
	private Map<V, mxCell> vertexToCellMap = new HashMap<V, mxCell>();

	/** a map from JGraphT edges to mxGraph cells */
	private Map<E, mxCell> edgeToCellMap = new HashMap<E, mxCell>();

	/** a map from mxGraph cells to JGraphT vertices */
	private Map<mxCell, V> cellToVertexMap = new HashMap<mxCell, V>();

	/** a map from mxGraph cells to JGraphT edges */
	private Map<mxCell, E> cellToEdgeMap = new HashMap<mxCell, E>();

	public JGraphXAdapter() {
		this(null);
	}
	
	/**
	 * Ctor: 	Generates an adapter between a GraphT as data model and a mxGraph for
	 * 			visualization.
	 * @param graphT	The listenable JGraphT that acts as data model
	 */
	public JGraphXAdapter(final ListenableGraph<V, E> graphT) {
		super();
		setDataSource(graphT);
		if(Utility.presentationMode) {
			getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_FONTSIZE, 16);
			getStylesheet().getDefaultVertexStyle().put(mxConstants.STYLE_FONTSTYLE, mxConstants.FONT_BOLD);
		}
		getStylesheet().getDefaultEdgeStyle().put(mxConstants.STYLE_NOLABEL, 1);
	}
	
	public void setDataSource(final ListenableGraph<V, E> graphT) {
		if(this.graphT != null) {
			this.graphT.removeGraphListener(this);
			clearmxGraph();
		}
		this.graphT = graphT;
		if(this.graphT != null) {
			graphT.addGraphListener(this);
			insertJGraphT(graphT);
		}
	}

	public void clear() {
		if(this.graphT != null) {
			this.graphT.removeAllEdges(this.graphT.edgeSet());
			this.graphT.removeAllVertices(this.graphT.vertexSet());
		}
	}
	
	private void clearmxGraph() {
		this.removeCells(cellToVertexMap.keySet().toArray());
		this.removeCells(cellToEdgeMap.keySet().toArray());
		
		cellToEdgeMap.clear();
		cellToVertexMap.clear();
		edgeToCellMap.clear();
		vertexToCellMap.clear();
	}

	public void addJGraphTVertex(V vertex) {
		getModel().beginUpdate();
		try {
			mxCell cell = new mxCell(vertex);
			cell.setVertex(true);
			cell.setId(null);
			addCell(cell, defaultParent);
			vertexToCellMap.put(vertex, cell);
			cellToVertexMap.put(cell, vertex);
			onStringChange(vertex, vertex.toString());
			vertex.addListener(this);
		} finally {
			getModel().endUpdate();
		}
	}

	public void addJGraphTEdge(E edge) {
		getModel().beginUpdate();
		try {
			V source = graphT.getEdgeSource(edge);
			V target = graphT.getEdgeTarget(edge);
			mxCell cell = new mxCell(edge);
			cell.setEdge(true);
			cell.setId(null);
			cell.setGeometry(new mxGeometry());
			cell.getGeometry().setRelative(true);
			addEdge(cell, defaultParent, vertexToCellMap.get(source),
					vertexToCellMap.get(target), null);
			edgeToCellMap.put(edge, cell);
			cellToEdgeMap.put(cell, edge);
		} finally {
			getModel().endUpdate();
		}
	}
	
	public mxGraphComponent generateDefaultGraphComponent() {
		mxGraphComponent reval = new mxGraphComponent(this);
		new mxKeyboardHandler(reval);
		return reval;
	}

	public Map<V, mxCell> getVertexToCellMap() {
		return Collections.unmodifiableMap(vertexToCellMap);
	}

	public Map<E, mxCell> getEdgeToCellMap() {
		return Collections.unmodifiableMap(edgeToCellMap);
	}

	public Map<mxCell, E> getCellToEdgeMap() {
		return Collections.unmodifiableMap(cellToEdgeMap);
	}

	public Map<mxCell, V> getCellToVertexMap() {
		return Collections.unmodifiableMap(cellToVertexMap);
	}
	
	//- GraphListener METHODS 

	@Override
	public void vertexAdded(GraphVertexChangeEvent<V> e) {
		addJGraphTVertex(e.getVertex());
	}

	@Override
	public void vertexRemoved(GraphVertexChangeEvent<V> e) {
		e.getVertex().removeListener(this);
		mxCell cell = vertexToCellMap.remove(e.getVertex());
		cellToVertexMap.remove(cell);
		removeCells(new Object[] { cell });
	}

	@Override
	public void edgeAdded(GraphEdgeChangeEvent<V, E> e) {
		addJGraphTEdge(e.getEdge());
	}

	@Override
	public void edgeRemoved(GraphEdgeChangeEvent<V, E> e) {
		mxCell cell = edgeToCellMap.remove(e.getEdge());
		cellToEdgeMap.remove(cell);
		removeCells(new Object[] { cell });
	}

	//- Adapt Graph Behavior METHODS
	
	/** no editable cells in the graph just view on it */
	@Override
	public boolean isCellEditable(Object cell) {
		return false;
	}
	
	/** only vertices (nodes) are movable */
/*	@Override 
	public boolean isCellMovable(Object cell) {
		if(cell instanceof mxCell) {
			mxCell curCell = (mxCell)cell;
			if(curCell.isEdge())
				return false;
		}
		return super.isCellMovable(cell);
	} */
	
	/** it is not allowed to connect new edges */
	@Override
	public boolean isCellConnectable(Object cell) {
		return false;
	}
	
	/** it is not allowed to disconnect edges */
	@Override
	public boolean isCellDisconnectable(Object cell, Object terminal, boolean source) {
		return false;
	}
	
	/** only the vertices (nodes) show their label, no labels for edges */
	@Override
	public String convertValueToString(Object cell) {
		if(cell instanceof mxCell) {
			mxCell c = (mxCell)cell;
			if(c.isEdge())
				return null;
		}
		return super.convertValueToString(cell);
	}

	//- NodeListener METHODS
	
	@Override
	public void onStringChange(GraphNode source, String newLabel) {
		if(source != null) {
			mxCell cell = vertexToCellMap.get(source);
			Font curFont = mxUtils.getFont(getCellStyle(cell));
			int width = mxUtils.getFontMetrics(curFont).stringWidth(newLabel) + 5;
			cell.setGeometry(new mxGeometry(0,0,width,30));
		}
	}
	
	//- Helper METHODS
	
	private void insertJGraphT(Graph<V, E> graphT) {
		getModel().beginUpdate();
		try {
			for (V vertex : graphT.vertexSet())
				addJGraphTVertex(vertex);
			for (E edge : graphT.edgeSet())
				addJGraphTEdge(edge);
		} finally {
			getModel().endUpdate();
		}

	}

	public static void main(String[] args) {
		// create a JGraphT graph
		ListenableDirectedGraph<GraphNode, DefaultEdge> g = new ListenableDirectedGraph<GraphNode, DefaultEdge>(
				DefaultEdge.class);

		// add some sample data (graph manipulated via JGraphT)
		Processor v1 = new Processor("Node", g);
		Processor v2 = new Processor("Information", g);
		Processor v3 = new Processor("Guess", g);
		Processor v4 = new Processor("Vertex", g);

		
		g.addVertex(v1);
		g.addVertex(v2);
		g.addVertex(v3);
		g.addVertex(v4);

		g.addEdge(v1, v2);
		g.addEdge(v2, v3);
		g.addEdge(v3, v1);
		g.addEdge(v4, v3);

		JGraphXAdapter<GraphNode, DefaultEdge> graph = new JGraphXAdapter<GraphNode, DefaultEdge>(g);

		JFrame frame = new JFrame();
		frame.getContentPane().add(graph.generateDefaultGraphComponent());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 320);
		frame.setVisible(true);

		g.addVertex(new Processor("v5", g));
		g.addVertex(new Processor("v6", g));
		g.addVertex(new Processor("v7", g));
		g.addVertex(new Processor("v8", g));

		/*
		graph.getModel().beginUpdate();
		double x = 20, y = 20;
		for (mxCell cell : graph.getVertexToCellMap().values()) {
			graph.getModel().setGeometry(cell, new mxGeometry(x, y, 20, 20));
			x += 40;
			if (x > 200) {
				x = 20;
				y += 40;
			}
		}
		graph.getModel().endUpdate();
		*/
		
		mxHierarchicalLayout layout = new mxHierarchicalLayout(graph);
		layout.execute(graph.getDefaultParent());
	}
}
