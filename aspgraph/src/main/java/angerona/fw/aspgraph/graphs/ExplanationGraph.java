package angerona.fw.aspgraph.graphs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.collections15.CollectionUtils;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class ExplanationGraph extends DirectedSparseGraph<EGVertex, EGEdge> implements Serializable{

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
			// if (!this.containsVertex(s)) this.addVertex(s);
			// if (!this.containsVertex(t)) this.addVertex(t);
			if (s instanceof EGLiteralVertex && ((EGLiteralVertex) s).getLiteral().equals(literal)){
					this.addEdge(source, s, new EGEdge(source,s,type));
			}
			this.addEdge(s, t, e);
		}
	}
	
	public ExplanationGraph deepCopy() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream( baos ).writeObject(this);

		ByteArrayInputStream bais =
		new ByteArrayInputStream( baos.toByteArray() );

		return (ExplanationGraph) new ObjectInputStream(bais).readObject();
	}
	
	@Override
	public boolean equals(Object obj){
		ExplanationGraph eg ;
		if (obj instanceof ExplanationGraph){
			eg = (ExplanationGraph) obj;
		} else { 
			return false;
		}
		if (!CollectionUtils.isEqualCollection(eg.getVertices(), this.getVertices())) return false;
		if (!CollectionUtils.isEqualCollection(eg.getEdges(), this.getEdges())) return false;
		return true;
	}
	
	@Override
	public int hashCode(){
		int h = toString().hashCode();
		return h;
	}
}
