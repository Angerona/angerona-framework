package angerona.fw.aspgraph.graphs;

import java.util.Collection;
import java.util.Set;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class ExplanationGraph extends DirectedSparseGraph<EGVertex, EGEdge>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9176371316817967877L;

	public ExplanationGraph() {
		super();
	}

	public void addEdge(EGVertex source, EGVertex target, EGEdge e) {
		super.addEdge(e, new Pair<EGVertex>(source,target), EdgeType.DIRECTED);	
	}
	
	public void addEdge(EGVertex source, ExplanationGraph eg, String literal, EGEdge.EdgeType type){
		Collection<EGEdge> edges = eg.getEdges();
		for (EGEdge e : edges){
			EGVertex s = e.getSource();
			EGVertex t = e.getTarget();
			EGEdge.EdgeType label = e.getLabel();
			this.addVertex(s);
			this.addVertex(t);
			if (s instanceof EGLiteralVertex && ((EGLiteralVertex) s).getLiteral().equals(literal)){
					this.addEdge(source, s, new EGEdge(source,s,type));
			}
			this.addEdge(s, t, new EGEdge(s,t,label));
		}
	}
}
