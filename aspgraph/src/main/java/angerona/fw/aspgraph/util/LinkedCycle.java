package angerona.fw.aspgraph.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import angerona.fw.aspgraph.graphs.EDGEdge;
import angerona.fw.aspgraph.graphs.EDGVertex;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;

public class LinkedCycle {
	private Set<EDGVertex> vertices;
	private Set<EDGVertex> ip_in;
	private Set<EDGVertex> ip_out;
	private Set<ArrayList<EDGVertex>> validPaths;
	private ArrayList<Set<EDGVertex>> assumptionPaths;
	private ExtendedDependencyGraph edg;
	private Set<EDGEdge> externalEdges;
	
	public LinkedCycle(List<EDGVertex> vertices, ExtendedDependencyGraph edg){
		this.vertices = new HashSet<EDGVertex>(vertices);
		this.edg = edg;
		externalEdges = new HashSet<EDGEdge>();
		calculateImportantPoints();
		calculateValidPaths();
		calculateAssumptionPaths();
	}
	
	private void calculateAssumptionPaths() {
		assumptionPaths = new ArrayList<Set<EDGVertex>>();
		HashMap<ArrayList<EDGVertex>,Integer> usedPaths = new HashMap<ArrayList<EDGVertex>,Integer>();
		for (ArrayList<EDGVertex> path1 : validPaths){
			for (ArrayList<EDGVertex> path2 : validPaths){
				EDGVertex end = path1.get(path1.size()-1);
				EDGVertex start = path2.get(0);
				if (start.equals(end)){
					if (usedPaths.containsKey(path1)){
						if (!usedPaths.containsKey(path2)){
							Integer index = usedPaths.get(path1);
							assumptionPaths.get(index).addAll(path2);
							usedPaths.put(path2,index);
						}
					} else if (usedPaths.containsKey(path2)){
						Integer index = usedPaths.get(path2);
						assumptionPaths.get(index).addAll(path1);
						usedPaths.put(path1,index);
					} else{
						HashSet<EDGVertex> path1Set = new HashSet<EDGVertex>(path1);
						assumptionPaths.add(path1Set);
						Integer index = assumptionPaths.indexOf(path1Set);
						assumptionPaths.get(index).addAll(path2);
						usedPaths.put(path1, index);
						usedPaths.put(path2, index);
					}
				} else {
					HashSet<EDGVertex> path1Set = new HashSet<EDGVertex>(path1);
					assumptionPaths.add(path1Set);
					Integer index = assumptionPaths.indexOf(path1Set);
					usedPaths.put(path1, index);					
				}
			}
		}
		
	}

	private void calculateImportantPoints(){
		ip_in = new HashSet<EDGVertex>();
		ip_out = new HashSet<EDGVertex>();
		for (EDGVertex v : vertices){
			Set<EDGEdge> inEdges = new HashSet<EDGEdge>();
			for (EDGEdge e : edg.getInEdges(v)){
				if (vertices.contains(e.getSource())) inEdges.add(e);
				else externalEdges.add(e);
			}
			if (inEdges.size() > 1) ip_in.add(v);
			
			Set<EDGEdge> outEdges = new HashSet<EDGEdge>(edg.getOutEdges(v));
			outEdges.retainAll(vertices);
			if (outEdges.size() > 1) ip_out.add(v);			
		}
	}
	
	private void calculateValidPaths(){
		validPaths = new HashSet<ArrayList<EDGVertex>>();
		
		/* Calculate valid paths from ip_in to ip_out */
		for (EDGVertex v : ip_in){
			ArrayList<EDGVertex> path = new ArrayList<EDGVertex>();
			followInPath(v,path);
			validPaths.add(path);
		}
		
		/* Calculate valid paths from ip_out to ip_in */
		for (EDGVertex v : ip_out){
			Set<EDGEdge> outEdges = new HashSet<EDGEdge>(edg.getOutEdges(v));
			outEdges.retainAll(vertices);
			for (EDGEdge e : outEdges){
				ArrayList<EDGVertex> path = new ArrayList<EDGVertex>();
				path.add(v);
				EDGEdge lastEdge = followOutPath(e.getTarget(),path);
				if (lastEdge != null && lastEdge.isActive()) validPaths.add(path);
			}
		}
	}
	
	private EDGEdge followOutPath(EDGVertex v, ArrayList<EDGVertex> path) {
		path.add(v);
		for (EDGEdge e : edg.getOutEdges(v)){
			EDGVertex target = e.getTarget();
			if (vertices.contains(target)){
				if (ip_in.contains(target)){
					path.add(target);
					return e;
				} else return followOutPath(target, path);
			}
		}
		return null;
	}

	private void followInPath(EDGVertex v, ArrayList<EDGVertex> path){
		path.add(v);
		if (ip_out.contains(v)) return;
		else{
			ArrayList<EDGVertex> successors = new ArrayList<EDGVertex>(edg.getSuccessors(v));
			successors.retainAll(vertices);
			if (successors.size() == 1)	followInPath(successors.get(0), path);
		}
	}
	
}
