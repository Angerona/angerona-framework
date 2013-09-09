package com.github.angerona.fw.aspgraph.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import angerona.fw.aspgraph.graphs.EDGEdge;
import angerona.fw.aspgraph.graphs.EDGVertex;
import angerona.fw.aspgraph.graphs.EGEdge;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;

public class LinkedCycle {
	private Set<EDGVertex> vertices;
	private Set<String> containedLiterals;
	private Set<EDGVertex> ip_in;
	private Set<EDGVertex> ip_out;
	private Set<Path> validPaths;
	private Set<Path> assumptionPaths;
	private ExtendedDependencyGraph edg;
	private Set<EDGEdge> externalEdges;
	private Set<Set<EDGVertex>> positiveCycles;
	
	public LinkedCycle(List<EDGVertex> component, ExtendedDependencyGraph edg){
		this.vertices = new HashSet<EDGVertex>(component);
		containedLiterals = new HashSet<String>();
		
		for(EDGVertex v : vertices){
			containedLiterals.add(v.getLiteral());
		}
		
		this.edg = edg;
		externalEdges = new HashSet<EDGEdge>();
		positiveCycles = new HashSet<Set<EDGVertex>>();
		calculateImportantPoints(); 
		if (vertices.size() == 1){ // Linked Cycle is just one node
			validPaths = new HashSet<Path>();
			assumptionPaths = new HashSet<Path>();
		}
		else if (ip_in.isEmpty() && ip_out.isEmpty()){ // Linked Cycle is a basic cycle
				validPaths = new HashSet<Path>();
				assumptionPaths = new HashSet<Path>();
				Path path = new Path(component);
				validPaths.add(path);
				assumptionPaths.add(path);
				/* remove positive cycle */
				Iterator<EDGVertex> iterator = component.iterator();
				if (iterator.hasNext()){
					EDGVertex v = iterator.next();
					Collection<EDGEdge> e = edg.getOutEdges(v);
					Iterator<EDGEdge> itEdges = e.iterator();
					findPositiveCycle(v,itEdges.next().getTarget(),null);
					if (!positiveCycles.isEmpty()){
						assumptionPaths = new HashSet<Path>();
					}
				}
			} else{
				calculateValidPaths();
				calculateAssumptionPaths(validPaths);
				if (!component.isEmpty()){
					for (EDGVertex v : component){
						for (EDGEdge e : edg.getOutEdges(v)){
							findPositiveCycle(v,e.getTarget(),null);
						}
					}
				}
				/* Clean assumption paths */
				
				// Remove positive cycles
				for (Set<EDGVertex> positiveCycle : positiveCycles){
					for (EDGVertex v : positiveCycle){
						if (!hasORHandle(positiveCycle)){
							Path assPath = getAssumptionPath(v);
							if (assPath != null) assumptionPaths.remove(assPath);
						}
					}
				}
			}
		
			// Remove assumptions with active edge
			for (EDGEdge e : externalEdges){
				if (e.isActive()){
				 EDGVertex v = e.getTarget();
				 Path assPath = getAssumptionPath(v);
				 if (assPath != null) assumptionPaths.remove(assPath);
			}
		}
		
		/* Add external edges as AND-Handles or OR-Handles */
		for (EDGEdge e : externalEdges){
			if (vertices.size() > 1){
				if (containedLiterals.contains(e.getSource().getLiteral())) edg.addOrHandle(e);
				else edg.addAndHandle(e);
			}
		}	
	}
	
	private void calculateAssumptionPaths(Set<Path> paths) {
		Set<Path> newPaths = new HashSet<Path>();
		
		HashMap<Path, Boolean> usedPath = new HashMap<Path, Boolean>();
		for (Path p : paths){
			usedPath.put(p, false);
		}
		
		for (Path path1 : paths){
			for (Path path2 : paths){
				/* Paths are not subsets of each other */
				if (!path1.equals(path2)){
					if (path1.containsAll(path2)) usedPath.put(path2, true);
					else if (path2.containsAll(path1)) usedPath.put(path1, true);
					else{
						EDGVertex end = path1.getEnd();
						EDGVertex start = path2.getStart();
				
						if (start.equals(end)){
							Path p = new Path();
							p.addAll(path1);
							p.addAll(path2);
							p.setStart(path1.getStart());
							p.setEnd(path2.getEnd());
							newPaths.add(p);
							usedPath.put(path1, true);
							usedPath.put(path2,true);
						}
					}
				}
			}
		}
		for (Path p : usedPath.keySet()){
			if (!usedPath.get(p)) newPaths.add(p);
		}
		if (newPaths.equals(paths)) assumptionPaths = paths;
		else calculateAssumptionPaths(newPaths);
	
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
			
			Set<EDGEdge> outEdges = new HashSet<EDGEdge>();
			for (EDGEdge e : edg.getOutEdges(v)){
				if (vertices.contains(e.getTarget())) outEdges.add(e);
			}
			if (outEdges.size() > 1) ip_out.add(v);	
		}	}
	
	private void calculateValidPaths(){
		validPaths = new HashSet<Path>();
		
		/* Calculate valid paths starting from ip_in*/
		for (EDGVertex v : ip_in){
			Set<EDGEdge> outEdges = new HashSet<EDGEdge>(edg.getOutEdges(v));
			for (EDGEdge e:  edg.getOutEdges(v)){
				EDGVertex t = e.getTarget();
				if (!vertices.contains(t)) outEdges.remove(e);
			}
			for (EDGEdge e : outEdges){
				Path path = new Path();
				path.setStart(v);
				path.setEnd(v);
				followInPath(e.getTarget(),path);
				if (!path.isEmpty()) validPaths.add(path);
			}
		}
		
		/* Calculate valid paths starting from ip_out*/
		for (EDGVertex v : ip_out){
			Set<EDGEdge> outEdges = new HashSet<EDGEdge>(edg.getOutEdges(v));
			for (EDGEdge e:  edg.getOutEdges(v)){
				EDGVertex t = e.getTarget();
				if (!vertices.contains(t)) outEdges.remove(e);
			}
			for (EDGEdge e : outEdges){
				Path path = new Path();
				path.setStart(v);
				if (ip_in.contains(e.getTarget())){
					if (e.isActive()) path.setEnd(e.getTarget());
				}
				else followOutPath(e.getTarget(),path);
				if (!path.isEmpty()) validPaths.add(path);
			}
		}
	}
	
	
	private void followOutPath(EDGVertex v, Path path) {
		if (ip_out.contains(v) && !ip_in.contains(v)) path.setEnd(v);
		else {
			path.setEnd(v);
			for (EDGEdge e : edg.getOutEdges(v)){
				EDGVertex target = e.getTarget();
				if (vertices.contains(target)){
					if (ip_in.contains(target)){
						if (e.isActive()) path.setEnd(target);
						else if (target.hasActiveEdge(edg)) path = new Path();
					} else followOutPath(target, path);
				}
			}
		}
	}
	
	private void findPositiveCycle(EDGVertex start, EDGVertex next, Set<EDGVertex> path){
		Set<EDGVertex> path2;
		if (path != null) path2 = new HashSet<EDGVertex>(path);
		else path2 = new HashSet<EDGVertex>();
		if (!start.equals(next)){
			if (!path2.contains(next)){
			for (EDGEdge e : edg.getOutEdges(next)){
				if (e.getLabel().equals(EDGEdge.EdgeType.POS)){
					path2.add(next);
					findPositiveCycle(start,e.getTarget(),path2);
				}
			}
			}
		} else{
			path2.add(next);
			positiveCycles.add(path2);
		}
	}

	private void followInPath(EDGVertex v, Path path){
		if (ip_in.contains(v)){
			for(EDGEdge e : edg.getInEdges(v)){
				if (path.contains(e.getSource())){
					if (e.isActive()) path.setEnd(v);
					else if (v.hasActiveEdge(edg)) path = new Path();
				}
			}
		}
		else{ 
			path.setEnd(v);
			if (!ip_out.contains(v)){
				ArrayList<EDGVertex> successors = new ArrayList<EDGVertex>(edg.getSuccessors(v));
				successors.retainAll(vertices);
				if (successors.size() == 1)	followInPath(successors.get(0), path);
			}
		}	
	}
	
	public Set<Path> getAssumptionPaths(){
		return assumptionPaths;
	}
	
	private Path getAssumptionPath(EDGVertex v){
		for (Path assumptionPath : assumptionPaths){
			if (assumptionPath.contains(v)) return assumptionPath;
		}
		return null;
	}
	
	public Set<EDGEdge> getExternalEdges(){
		return externalEdges;
	}
	
	private boolean hasORHandle(Set<EDGVertex> positiveCycle){
		for (EDGVertex v : positiveCycle){
			for (EDGVertex v2 : edg.getNodeMap().get(v.getLiteral())){
				// there exists an OR-handle for at least one vertex in the positive cycle
				if (!v.equals(v2) && vertices.contains(v2)) return true;
			}
		}
		return false;
	}
	
}
