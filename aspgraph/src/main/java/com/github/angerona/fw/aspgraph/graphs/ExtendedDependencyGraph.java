package com.github.angerona.fw.aspgraph.graphs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import angerona.fw.aspgraph.graphs.EDGVertex.Color;
import angerona.fw.aspgraph.util.AnswerSetTwoValued;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;



public class ExtendedDependencyGraph extends DirectedSparseGraph<EDGVertex,EDGEdge> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6245092624779526429L;
	
	/**
	 * Two valued representation of answer set
	 */
	private AnswerSetTwoValued answerSet;
	
	/**
	 * Set of all nodes representing contradictions
	 */
	private HashSet<EDGVertex> contradictionNodes;
	
	/**
	 * Map Literal -> List of vertices taht represent that literal
	 */
	private HashMap<String, List<EDGVertex>> nodeMap;
	
	/**
	 * Set of AND-handles
	 */
	private Set<EDGEdge> andHandles;
	
	/**
	 * Set of OR-handles
	 */
	private Set<EDGEdge> orHandles;

	/**
	 * Creates a new Extended Dependency Graph
	 */
	public ExtendedDependencyGraph(){
		super();
		andHandles = new HashSet<EDGEdge>();
		orHandles = new HashSet<EDGEdge>();
	}
	
	/**
	 * Sets answer set of EDG and colors it	
	 * @param as Answer set 
	 */
	public void setAnswerSet(AnswerSet as){
		answerSet = new AnswerSetTwoValued(nodeMap.keySet(), as);
		colorEDG();
	}
	
	/**
	 * Returns twoValued version of answer set
	 * @return Two valued answer set
	 */
	public AnswerSetTwoValued getAnswerSet(){
		return answerSet;
	}

	/**
	 * Adds a new edge to EDG
	 * @param source Source vertex of edge
	 * @param target Targer vertex of edge
	 * @param e Edge which should be added
	 */
	public void addEdge(EDGVertex source, EDGVertex target, EDGEdge e) {
		super.addEdge(e, new Pair<EDGVertex>(source,target), EdgeType.DIRECTED);
		
	}
	
	/**
	 * Sets contradiction nodes
	 * @param conNodes Set of contradiction nodes
	 */
	public void setContradictionNodes(HashSet<EDGVertex> conNodes){
		contradictionNodes = conNodes;
	}
	
	/**
	 * Sets node map
	 * @param nodes Node map
	 */
	public void setNodeMap(HashMap<String, List<EDGVertex>> nodes){
		this.nodeMap = nodes;
	}
	
	/**
	 * Creates a deep copy of the EDG 
	 * @return Copy of the EDG
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public ExtendedDependencyGraph deepCopy() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream( baos ).writeObject(this);

		ByteArrayInputStream bais =
		new ByteArrayInputStream( baos.toByteArray() );

		return (ExtendedDependencyGraph) new ObjectInputStream(bais).readObject();
	}
	
	/**
	 * Colors EDG relating to answer set
	 */
	private void colorEDG(){
		if (answerSet != null){
			HashMap<String,List<EDGVertex>> notColored = new HashMap<String,List<EDGVertex>>();
			for (String key : nodeMap.keySet()){
				List<EDGVertex> list = new ArrayList<EDGVertex>(nodeMap.get(key));
				notColored.put(key, list);
			}
			
			/* Color all constraint nodes red */
			for (EDGVertex v : contradictionNodes){
				v.setColor(Color.RED);
				List<EDGVertex> list = notColored.get(v.getLiteral());
				list.remove(v);
			}
	
			/* Color all nodes red which are not contained in answer set */
			for (String not : answerSet.getUnfounded()){
				for (EDGVertex v : notColored.get(not)){
					v.setColor(Color.RED);
				}
				notColored.remove(not);
			}
	
			/* Color other nodes */
			for (String in : answerSet.getFounded()){
				List<EDGVertex> list = notColored.get(in);
				if (list.size() == 1){
					EDGVertex v = list.get(0);
					v.setColor(Color.GREEN);
					notColored.remove(in);
				}
			}

			while (!notColored.isEmpty()){
				Set<String> keys = new HashSet<String>(notColored.keySet());
				for (String s : keys){
					if (notColored.containsKey(s)){
					List<EDGVertex> list = new ArrayList<EDGVertex>(notColored.get(s));
					if (list.isEmpty()) notColored.remove(s);
					else{
						for (EDGVertex v : list){
							Collection<EDGEdge> edges = getInEdges(v);
							Color color = Color.GREEN;
							for (EDGEdge e : edges){
								if (e.getSource().getColor() != null && e.getSource().getColor().equals(Color.GREEN) && 
									e.getLabel().equals(EDGEdge.EdgeType.NEG)){
									color = Color.RED;
								}
								else if (e.getSource().getColor() != null && e.getSource().getColor().equals(Color.RED) &&
										 e.getLabel().equals(EDGEdge.EdgeType.POS)){
										List<EDGVertex> others = nodeMap.get(e.getSource().getLiteral());
										boolean allRed = true;
										for (EDGVertex v2 : others){
											if (v2.getColor().equals(Color.GREEN)){
												allRed = false;
											}
										}
										if (allRed) color = Color.RED;
								}
								else if (e.getSource().getColor() == null) color = null;
							}
							if (color != null){
								v.setColor(color);
								notColored.get(s).remove(v);
							}
						}
					}
				}
			}
		}}
	}
	
	/**
	 * Returns node map
	 * @return Mapping of literals to their representing nodes
	 */
	public HashMap<String, List<EDGVertex>> getNodeMap(){
		return nodeMap;
	}
	
	/**
	 * Add AND-handles to EDG
	 * @param edges Set of edges that are AND-handles
	 */
	public void addAndHandles(Set<EDGEdge> edges){
		andHandles.addAll(edges);
	}
	
	/**
	 * Adds an AND-handle to EDG
	 * @param edge Edge that is an AND-handle
	 */
	public void addAndHandle(EDGEdge edge){
		andHandles.add(edge);
	}
	
	/**
	 * Returns AND-handles of the EDG
	 * @return AND-handles of EDg
	 */
	public Set<EDGEdge> getAndHandles(){
		return andHandles;
	}
	
	/**
	 * Adds OR-handles to EDG
	 * @param edges Edges that are OR-handles
	 */
	public void addOrHandles(Set<EDGEdge> edges){
		orHandles.addAll(edges);
	}
	
	/**
	 * Add OR-handle to EDG
	 * @param edge Edge that is an OR-handle
	 */
	public void addOrHandle(EDGEdge edge){
		orHandles.add(edge);
	}
	
	/**
	 * Returns OR-handles of EDG
	 * @return OR-handles of the EDG
	 */
	public Set<EDGEdge> getOrHandles(){
		return orHandles;
	}

}
