package angerona.fw.aspgraph.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import angerona.fw.aspgraph.graphs.EDGVertex;

public class Path {
	private Set<EDGVertex> vertices;
	private EDGVertex start;
	private EDGVertex end;
	
	public Path(){
		vertices = new HashSet<EDGVertex>();
	}
	
	public Path(List<EDGVertex> component) {
		vertices = new HashSet<EDGVertex>();
		vertices.addAll(component);
		start = component.get(0);
		end = component.get(component.size()-1);
	}
	
	public void setStart(EDGVertex start){
		if (!vertices.contains(start)) vertices.add(start);
		this.start = start;
	}
	
	public EDGVertex getStart(){
		return start;
	}
	
	public void setEnd(EDGVertex end){
		if (!vertices.contains(end)) vertices.add(end);
		this.end = end;
	}
	
	public EDGVertex getEnd(){
		return end;
	}
	
	public void add(EDGVertex v){
		vertices.add(v);
	}
	
	public boolean contains(EDGVertex v){
		return vertices.contains(v);
	}
	
	public String toString(){
		String s = "";
		for (EDGVertex v : vertices){
			s = s + v.pureString() + " ";
		}
		return s;
	}
	
	public boolean equals(Object o){
		if (o instanceof Path){
			Path p = (Path) o;
			return this.vertices.equals(p.vertices);
		}
		return false;
	}
	
	public boolean isEmpty(){
		return vertices.isEmpty();
	}
	
	public void addAll(Collection<EDGVertex> c){
		vertices.addAll(c);
	}
	
	public void addAll(Path p){
		vertices.addAll(p.vertices);
	}
	
	public Set<EDGVertex> getVertices(){
		return vertices;
	}
	
	public void remove(EDGVertex v){
		vertices.remove(v);
	}

	public boolean containsAll(Path p) {
		return vertices.containsAll(p.vertices);
	}
	
	public int hashCode(){
		return vertices.hashCode();
	}
}
