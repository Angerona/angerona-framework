package angerona.fw.aspgraph.util;

import java.util.ArrayList;
import java.util.List;

import angerona.fw.aspgraph.graphs.EDGEdge;
import angerona.fw.aspgraph.graphs.EDGVertex;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;

public class Tarjan {
	private int index = 0;
	private List<EDGVertex> stack = new ArrayList<EDGVertex>();
	private List<List<EDGVertex>> SCC = new ArrayList<List<EDGVertex>>();

	public List<LinkedCycle> executeTarjan(ExtendedDependencyGraph graph){
		SCC.clear();
	    index = 0;
	    stack.clear();
	    if(graph != null){
	    	List<EDGVertex> nodeList = new ArrayList<EDGVertex>(graph.getVertices());
	        if(nodeList != null){
	        	for (EDGVertex node : nodeList){
	        		if(node.getIndex() == -1) tarjan(node, graph);
	            }
	        }
	    }
	    
	    List<LinkedCycle> linkedCycles = new ArrayList<LinkedCycle>();
	    for (List<EDGVertex> component : SCC){
	    	linkedCycles.add(new LinkedCycle(component,graph));
	    }
	    return linkedCycles;
	}
	
	private List<List<EDGVertex>> tarjan(EDGVertex v, ExtendedDependencyGraph edg){
		v.setIndex(index);
	    v.setLowlink(index);
	    index++;
	    stack.add(0, v);
	    for(EDGEdge e : edg.getIncidentEdges(v)){
	    	EDGVertex n = e.getTarget();
	        if(n.getIndex() == -1){
	        	tarjan(n, edg);
	            v.setLowlink(Math.min(v.getLowlink(), n.getLowlink()));
	        }else if(stack.contains(n)){
	        	v.setLowlink(Math.min(v.getLowlink(), n.getIndex()));
	        }
	    }
	    if(v.getLowlink() == v.getIndex()){
	    	EDGVertex n;
	        List<EDGVertex> component = new ArrayList<EDGVertex>();
	        do{
	        	n = stack.remove(0);
	            component.add(n);
	        }while(n != v);
	        SCC.add(component);
	    }
	    return SCC;
	 }
}
