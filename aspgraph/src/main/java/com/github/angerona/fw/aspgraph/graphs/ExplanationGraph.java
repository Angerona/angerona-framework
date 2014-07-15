package com.github.angerona.fw.aspgraph.graphs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import com.github.angerona.fw.aspgraph.graphs.EGLiteralVertex.Annotation;

import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * Represents an Explanation Graph
 * @author ella
 *
 */
public class ExplanationGraph extends DirectedSparseGraph<EGVertex, EGEdge> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9176371316817967877L;

	/**
	 * Creates a new explanation graph
	 */
	public ExplanationGraph() {
		super();
	}

	/**
	 * Adds an edge to the graph
	 * @param source Source node of edge
	 * @param target Target node of edge
	 * @param e EGEdge that should be added
	 */
	public void addEdge(EGVertex source, EGVertex target, EGEdge e) {
		super.addEdge(e, new Pair<EGVertex>(source,target), EdgeType.DIRECTED);	
	}
	
	/**
	 * Adds an subgraph to the graph
	 * @param source Source node to which an edge to subgraph should be added
	 * @param eg Explanation Graph that should be added as subgraph
	 * @param literal Literal of root node of the passed Explanation Graph
	 * @param type
	 */
	public void addEdge(EGVertex source, ExplanationGraph eg, String literal, EGEdge.EdgeType type){
		Collection<EGEdge> edges = eg.getEdges();
		for (EGEdge e : edges){
			EGVertex s = e.getSource();
			EGVertex t = e.getTarget();
			
			// Add edge from source vertex to root node of passed EG
			if (s instanceof EGLiteralVertex && ((EGLiteralVertex) s).getLiteral().equals(literal)){
					this.addEdge(source, s, new EGEdge(source,s,type));
			}
			
			this.addEdge(s, t, e);
		}
	}
	
	/**
	 * Creates a deep copy of the Explanation Graph
	 * @return Copy of the Explanation Graph
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
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
	
	/**
	 * Checks if Explanation Graph contains positive cycles
	 * @return true, if EG contains a positive cycle
	 */
	public boolean hasPositiveCycle(){
		TarjanEG tarjan = new TarjanEG();
		List<List<EGVertex>> components = tarjan.executeTarjan(this);
		for (List<EGVertex> component : components){
			for (EGVertex v : component){
				if (v instanceof EGLiteralVertex){
					EGLiteralVertex start = (EGLiteralVertex) v;
					if (start.getAnnotation().equals(Annotation.POS)){
						for (EGEdge e : this.getOutEdges(v)){
							if (findPositiveCycle(start,e.getTarget(),null)) return true;					
						}
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * Looks for positive cycles in Explanation Graph
	 * @param start Start vertex of path
	 * @param next Next vertex in path, which should be visited
	 * @param path Visited path
	 * @return true, if a positive cycle is found
	 */
	private boolean findPositiveCycle(EGLiteralVertex start, EGVertex next, Set<EGVertex> path){
		Set<EGVertex> path2;
		if (path != null) path2 = new HashSet<EGVertex>(path);
		else path2 = new HashSet<EGVertex>();
		
		if (!start.equals(next)){
			for (EGEdge e : this.getOutEdges(next)){
				if (e.getLabel().equals(EGEdge.EdgeType.POS)){
					if (e.getTarget() instanceof EGLiteralVertex){
						EGLiteralVertex target = (EGLiteralVertex) e.getTarget();
						if (target.getAnnotation().equals(Annotation.POS)){
							path2.add(next);
							if (findPositiveCycle(start,e.getTarget(),path2)) return true;
						}
					}
				}
			}
		} else{
			return true;
		}
		return false;
	}
	
	/**
	 * Tarjan algorithm for Explanation Graphs
	 * @author ella
	 *
	 */
	class TarjanEG {
		private int index = 0;
		private List<EGVertex> stack = new ArrayList<EGVertex>();
		private List<List<EGVertex>> SCC = new ArrayList<List<EGVertex>>();

		List<List<EGVertex>> executeTarjan(ExplanationGraph eg){
			SCC.clear();
		    index = 0;
		    stack.clear();

		    List<EGVertex> nodeList = new ArrayList<EGVertex>(eg.getVertices());
		    if(nodeList != null){
		        	for (EGVertex node : nodeList){
		        		if(node.getIndex() == -1) tarjan(node, eg);
		            }
		    }
		    
		    
		    return SCC;
		}
		
		private List<List<EGVertex>> tarjan(EGVertex node, ExplanationGraph eg){
			node.setIndex(index);
		    node.setLowlink(index);
		    index++;
		    stack.add(0, node);
		    for(EGEdge e : eg.getIncidentEdges(node)){
		    	EGVertex n = e.getTarget();
		        if(n.getIndex() == -1){
		        	tarjan(n, eg);
		            node.setLowlink(Math.min(node.getLowlink(), n.getLowlink()));
		        }else if(stack.contains(n)){
		        	node.setLowlink(Math.min(node.getLowlink(), n.getIndex()));
		        }
		    }
		    if(node.getLowlink() == node.getIndex()){
		    	EGVertex n;
		        List<EGVertex> component = new ArrayList<EGVertex>();
		        do{
		        	n = stack.remove(0);
		            component.add(n);
		        }while(n != node);
		        SCC.add(component);
		    }
		    return SCC;
		 }
	}
}
