package angerona.fw.aspgraph.graphs;

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
import java.util.Map;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import angerona.fw.aspgraph.controller.EDGController;
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
	
	private AnswerSetTwoValued answerSet;
	private HashSet<EDGVertex> contradictionNodes;
	private HashMap<String, List<EDGVertex>> nodeMap;

	public ExtendedDependencyGraph(){
		super();
	}
	
	
	public void setAnswerSet(AnswerSet as){
		answerSet = new AnswerSetTwoValued(nodeMap.keySet(), as);
		colorEDG();
	}
	
	public AnswerSetTwoValued getAnswerSet(){
		return answerSet;
	}

	public void addEdge(EDGVertex source, EDGVertex target, EDGEdge e) {
		super.addEdge(e, new Pair<EDGVertex>(source,target), EdgeType.DIRECTED);
		
	}
	
	public void setContradictionNodes(HashSet<EDGVertex> conNodes){
		contradictionNodes = conNodes;
	}
	
	public void setNodeMap(HashMap<String, List<EDGVertex>> nodes){
		this.nodeMap = nodes;
	}
	
	public ExtendedDependencyGraph deepCopy() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		new ObjectOutputStream( baos ).writeObject(this);

		ByteArrayInputStream bais =
		new ByteArrayInputStream( baos.toByteArray() );

		return (ExtendedDependencyGraph) new ObjectInputStream(bais).readObject();
	}
	
	private void colorEDG(){
		if (answerSet != null){
			HashMap<String,List<EDGVertex>> notColored = new HashMap<String,List<EDGVertex>>();
			notColored.putAll(nodeMap);
			
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
								if (e.getSource().getColor().equals(Color.GREEN) && 
									e.getLabel().equals(EDGEdge.EdgeType.NEG)){
									color = Color.RED;
								}
								else if (e.getSource().getColor().equals(Color.RED) &&
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
	
	public HashMap<String, List<EDGVertex>> getNodeMap(){
		return nodeMap;
	}

}
