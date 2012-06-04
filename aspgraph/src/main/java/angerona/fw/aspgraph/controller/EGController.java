package angerona.fw.aspgraph.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import angerona.fw.aspgraph.graphs.EDGEdge;
import angerona.fw.aspgraph.graphs.EDGVertex;
import angerona.fw.aspgraph.graphs.EDGVertex.Color;
import angerona.fw.aspgraph.graphs.EGAssumptionVertex;
import angerona.fw.aspgraph.graphs.EGBotVertex;
import angerona.fw.aspgraph.graphs.EGEdge;
import angerona.fw.aspgraph.graphs.EGEdge.EdgeType;
import angerona.fw.aspgraph.graphs.EGLiteralVertex;
import angerona.fw.aspgraph.graphs.EGLiteralVertex.Annotation;
import angerona.fw.aspgraph.graphs.EGTopVertex;
import angerona.fw.aspgraph.graphs.EGVertex;
import angerona.fw.aspgraph.graphs.ExplanationGraph;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;
import angerona.fw.aspgraph.util.AnswerSetTwoValued;
import angerona.fw.aspgraph.util.EGList;
import angerona.fw.aspgraph.util.LinkedCycle;
import angerona.fw.aspgraph.util.Tarjan;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;

public class EGController {
	
	private HashMap<AnswerSet, EGList> egLists;
	private static EGController instance;
	private Set<String>[] nneArray;
	private Set<Set<String>> lces;

	
	private EGController(){}
	
	public static EGController instance(){
		if (instance == null) return new EGController();
		else return instance;
	}
	
	public void createEGs(Map<AnswerSet,ExtendedDependencyGraph> edgs){
		egLists = new HashMap<AnswerSet,EGList>();
		
		for (AnswerSet as : edgs.keySet()){
			ExtendedDependencyGraph edg = edgs.get(as);
			AnswerSetTwoValued answerSet = edg.getAnswerSet();
			List<Set<String>> assumptions = calculateAssumptions(answerSet, edg);
			Set<String> literals = edg.getNodeMap().keySet();
			
			/* Calculate basic nodes: facts, constraints, unfounded */
			Set<EDGVertex> factNodeSet = new HashSet<EDGVertex>();
			Set<EDGVertex> unfoundedNodeSet = new HashSet<EDGVertex>();
			Set<EDGVertex> constraintNodeSet = new HashSet<EDGVertex>();
			for (EDGVertex v : edg.getVertices()){
				if (edg.getInEdges(v).size() == 0){
					if (v.getRuleNo() == 0) unfoundedNodeSet.add(v);
					else factNodeSet.add(v);
				} else if (v.getLiteral().equals("-")) constraintNodeSet.add(v);
			}
			egLists.put(as, new EGList());
			for (String literal : literals){
				for (Set<String> assumption : assumptions){
					createEGsToLiteral(literal, assumption,factNodeSet,unfoundedNodeSet,constraintNodeSet,edg,as);
				}
			}
		}
	}

	private void createEGsToLiteral(String literal, Set<String> assumption, Set<EDGVertex> factNodeSet, Set<EDGVertex> unfoundedNodeSet, Set<EDGVertex> constraintNodeSet, ExtendedDependencyGraph edg, AnswerSet as){
		/* Literal is an assumption */
		if (assumption.contains(literal)){
			ExplanationGraph eg = new ExplanationGraph();
			EGVertex litVertex = new EGLiteralVertex(Annotation.NEG, literal);
			EGVertex assumeVertex = new EGAssumptionVertex();
			EGEdge e = new EGEdge(litVertex,assumeVertex,EdgeType.NEG);
			eg.addVertex(litVertex);
			eg.addVertex(assumeVertex);
			eg.addEdge(litVertex, assumeVertex, e);
			egLists.get(as).addEGtoLiteral(literal, eg);
		}
		else{
			List<EDGVertex> specificNodes = edg.getNodeMap().get(literal);
			for (EDGVertex v : specificNodes){
				/* fact */
				if (factNodeSet.contains(v)){
					ExplanationGraph eg = new ExplanationGraph();
					EGVertex litVertex = new EGLiteralVertex(Annotation.POS, literal);
					EGVertex topVertex = new EGTopVertex();
					EGEdge e = new EGEdge(litVertex,topVertex,EdgeType.POS);
					eg.addVertex(litVertex);
					eg.addVertex(topVertex);
					eg.addEdge(litVertex, topVertex, e);
					egLists.get(as).addEGtoLiteral(literal, eg);
					/* unfounded node */
				} else if (unfoundedNodeSet.contains(v)){
					ExplanationGraph eg = new ExplanationGraph();
					EGVertex litVertex = new EGLiteralVertex(Annotation.NEG, literal);
					EGVertex botVertex = new EGBotVertex();
					EGEdge e = new EGEdge(litVertex,botVertex,EdgeType.NEG);
					eg.addVertex(litVertex);
					eg.addVertex(botVertex);
					eg.addEdge(litVertex, botVertex, e);
					egLists.get(as).addEGtoLiteral(literal, eg);
					/* dependent of other nodes */
				} else if (!constraintNodeSet.contains(v)){
					/* node is green */
					if (v.getColor().equals(Color.GREEN)){
						ExplanationGraph eg = new ExplanationGraph();
						EGLiteralVertex litVertex = new EGLiteralVertex(Annotation.POS, literal);
						eg.addVertex(litVertex);
						for (EDGEdge e : edg.getInEdges(v)){
							EDGVertex source = e.getSource();
							EGEdge.EdgeType type = null;
							switch(e.getLabel()){
							case POS : type = EGEdge.EdgeType.POS; break;
							case NEG : type = EGEdge.EdgeType.NEG;
							}
							List<ExplanationGraph> list = egLists.get(as).getEGList(source.getLiteral());
							/* Literals for sourcenode already build */
							if (list == null) createEGsToLiteral(source.getLiteral(), assumption, constraintNodeSet, constraintNodeSet, constraintNodeSet, edg, as);
							for (ExplanationGraph subgraph : list){
								eg.addEdge(litVertex, subgraph, literal, type);
							}
						}
						// TODO hinzugügen zu globaler EG-Liste
					/* node is red and represents a literal which is not in the answer set*/	
					} else if (edg.getAnswerSet().getUnfounded().contains(v.getLiteral())){
						
						/* calculate negative node explanations */
						Set<Set<String>> nnes = new HashSet<Set<String>>();
						for (EDGVertex node : edg.getNodeMap().get(literal)){
							Set<String> nne = new HashSet<String>();
							for (EDGEdge e : edg.getInEdges(node)){
								EDGVertex source = e.getSource();
								EDGEdge.EdgeType label = e.getLabel();
								if (source.getColor().equals(Color.GREEN) && label.equals(EdgeType.NEG)){
									nne.add(source.getLiteral());
								}
								if (source.getColor().equals(Color.RED) && label.equals(EdgeType.POS)){
									boolean allRed = true;
									for (EDGVertex v2 : edg.getNodeMap().get(source)){
										if (v2.getColor().equals(Color.GREEN)) allRed = false;
									}
									if (allRed) nne.add(source.getLiteral());
								}
							}
							nnes.add(nne);
						}
						
						/* calculate LCEs */
						lces = new HashSet<Set<String>>(); 
						nneArray = nnes.toArray(nneArray);
						constructLCE(nneArray.length-1,new HashSet<String>());
						
						/* construct EGs */
						for (Set<String> lce : lces){
							ExplanationGraph eg = new ExplanationGraph();
							EGLiteralVertex litVertex = new EGLiteralVertex(Annotation.NEG, literal);
							eg.addVertex(litVertex);
							for (String lit : lce){
								
							}
						}
					}
				}
			}	
		}
	}
	
	private void constructLCE(int i, Set<String> lce){
		if (i == -1) lces.add(lce);
		else{
			for (String lit : nneArray[i]){
				Set<String> nextLce = new HashSet<String>();
				nextLce.addAll(lce);
				nextLce.add(lit);
				constructLCE(i-1,nextLce);
			}
		}
	}
	
	private List<Set<String>> calculateAssumptions(AnswerSetTwoValued as, ExtendedDependencyGraph edg){
		List<Set<String>> assumptions = new LinkedList<Set<String>>();
		Tarjan t = new Tarjan();
		List<LinkedCycle> linkedCycles = t.executeTarjan(edg);
		// TODO Assumptions bestimmen
		return assumptions;	    
	}

	
	
	
		
}
