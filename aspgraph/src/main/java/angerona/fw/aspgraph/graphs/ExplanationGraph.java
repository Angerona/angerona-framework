package angerona.fw.aspgraph.graphs;

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

import org.apache.commons.collections15.CollectionUtils;

import angerona.fw.aspgraph.graphs.EGLiteralVertex.Annotation;
import angerona.fw.aspgraph.util.LinkedCycle;

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
