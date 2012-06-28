package angerona.fw.aspgraph.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import angerona.fw.aspgraph.exceptions.NotValidProgramException;
import angerona.fw.aspgraph.graphs.EDGEdge;
import angerona.fw.aspgraph.graphs.EDGEdge.EdgeType;
import angerona.fw.aspgraph.graphs.EDGVertex;
import angerona.fw.aspgraph.graphs.ExtendedDependencyGraph;
import angerona.fw.aspgraph.util.Contradiction;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSet;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;

/**
 * Responsible for construction and administration of Extended Dependency Graphs
 * @author ella
 *
 */
public class EDGController {
	
	/**
	 * Controlled Extended Dependency Graphs
	 */
	private HashMap<AnswerSet,ExtendedDependencyGraph> edgs;
	
	/**
	 * Instance of EDGController
	 */
	private static EDGController instance;
	
	/**
	 * Maps names of positive literal to nodes in EDG
	 */
	private HashMap<String,List<EDGVertex>> positiveNodes;
	
	/** 
	 * Maps names of negative literal to nodes in EDG
	 */
	private HashMap<String,List<EDGVertex>> negativeNodes;

	
	private EDGController(){}
	
	/**
	 * Returns the instance of EDGController
	 * @return Instance of EDGController
	 */
	public static EDGController instance(){
		if (instance == null) instance = new EDGController();
		return instance;
	}
	
	/**
	 * Creates an Extended Dependency Graph to a logic program
	 * @param p Logic program
	 * @throws NotValidProgramException Exception is thrown when program is not suitable for being represented
	 * as an Extended Dependency Graph
	 */
	public void createEDG(Program p, AnswerSetList asl) throws NotValidProgramException{
		if (isProgramValid(p)){
			
			/* Initialize Maps */
			edgs = new HashMap<AnswerSet,ExtendedDependencyGraph>();
			positiveNodes = new HashMap<String, List<EDGVertex>>();
			negativeNodes = new HashMap<String, List<EDGVertex>>();
			
			/* Create basic (uncolored) EDG */
			ExtendedDependencyGraph edg = new ExtendedDependencyGraph();
			HashMap<String, List<EDGVertex>> nodes = new HashMap<String, List<EDGVertex>>();
			HashSet<EDGVertex> conNodes = new HashSet<EDGVertex>();
		
			/* Add nodes for literals appearing in a rule head */
			Iterator<Rule> rules = p.iterator();
			int ruleNo = 0;
			while (rules.hasNext()){
				Rule r = rules.next();
				ruleNo++;
				String atomName;
				EDGVertex v;
				
				/* Rule is constraint, insert special "contradiction"-node with label "-" */
 				if (r.isConstraint()){
					atomName = "-";
					v = new EDGVertex(atomName, ruleNo);
					conNodes.add(v);
				}
 				/* Rule is not a constraint */
				else {	
					List<Literal> head = r.getHead();
					Literal l = head.get(0);
					
					/* True negated literal */
					if (l instanceof Neg){
						atomName = "-" + l.getAtom().toString();
						v = new EDGVertex(atomName, ruleNo);
						if (!negativeNodes.containsKey(atomName)){
							List<EDGVertex> list = new LinkedList<EDGVertex>();
							list.add(v);
							negativeNodes.put(atomName, list);
						}						
						else{
							List<EDGVertex> list = negativeNodes.get(atomName);
							list.add(v);
						}
						if (!nodes.containsKey(atomName)){
							List<EDGVertex> list = new LinkedList<EDGVertex>();
							list.add(v);
							nodes.put(atomName, list);
						}						
						else{
							List<EDGVertex> list = nodes.get(atomName);
							list.add(v);
						}	
					}
					/* Positive literal */
					else {
						atomName = l.getAtom().toString();
						v = new EDGVertex(atomName, ruleNo);
						if (!positiveNodes.containsKey(atomName)){
							List<EDGVertex> list = new LinkedList<EDGVertex>();
							list.add(v);
							positiveNodes.put(atomName, list);
						}						
						else{
							List<EDGVertex> list = positiveNodes.get(atomName);
							list.add(v);
						}
						if (!nodes.containsKey(atomName)){
							List<EDGVertex> list = new LinkedList<EDGVertex>();
							list.add(v);
							nodes.put(atomName, list);
						}						
						else{
							List<EDGVertex> list = nodes.get(atomName);
							list.add(v);
						}
						edg.addVertex(v);
					}
				}
			}	
			
			/* Add nodes that do not appear in a rule head */
 			rules = p.iterator();
 			while (rules.hasNext()){
 				Rule r = rules.next();
 				ruleNo++;
 				String atomName;
 				EDGVertex v;
				List<Literal> body = r.getBody();
				for (Literal lit : body){
					/* True negated literals */
					if (lit instanceof Neg){
						atomName = "-" + lit.getAtom().toString();
						if (!negativeNodes.containsKey(atomName)){
							v = new EDGVertex (atomName, 0);
							List<EDGVertex> list = new LinkedList<EDGVertex>();
							list.add(v);
							negativeNodes.put(atomName, list);
							List<EDGVertex> list2 = new LinkedList<EDGVertex>();
							list2.add(v);
							nodes.put(atomName, list2);
							edg.addVertex(v);
						}
					}
					/* Positive literals */
					else {
						atomName = lit.getAtom().toString();
						if (!positiveNodes.containsKey(atomName)){
							List<EDGVertex> list = new LinkedList<EDGVertex>();
							v = new EDGVertex(atomName, 0);
							list.add(v);
							positiveNodes.put(atomName, list);
							List<EDGVertex> list2 = new LinkedList<EDGVertex>();
							list2.add(v);
							nodes.put(atomName, list2);
							edg.addVertex(v);
						}
					}
				}		
			}
		
			/* Add edges */
			Iterator<Rule> rules2 = p.iterator();
			ruleNo = 0;
			while(rules2.hasNext()){
				Rule r = rules2.next();
				ruleNo++;
				List<Literal> body = r.getBody();
				List<Literal> head = r.getHead();
				String headName;
				boolean and = false;
				if (body.size() > 1) and = true;
				/* Rule is a constraint */
				if (r.isConstraint()){
					headName = "-";
				}
				/* Rule is not a constraint */
				else
					/* Head literal is true negated */
					if (head.get(0) instanceof Neg){
					headName = "-" + head.get(0).getAtom().toString();
					}
					/* Head literal is positive */
					else headName = head.get(0).getAtom().toString(); 
				
				/* Rule is not a fact */
				if (!body.isEmpty()){
					for (Literal literal : body){
						String name;
						
						if (literal instanceof Neg) name = "-" + literal.getAtom().toString();
						else name = literal.getAtom().toString();
						
						/* All nodes that represent source literal */
						List<EDGVertex> sourcelist = nodes.get(name);
						EDGVertex target = null;
						EDGEdge e = null;
						
						/* Find right target node */
						for (EDGVertex v : nodes.get(headName)){
							if (v.getRuleNo() == ruleNo) target = v;
						}
						
						/* Add edges from all source nodes to target node */
						boolean or = false;
						if (sourcelist.size() > 1) or = true;
						for(EDGVertex v : sourcelist){
							if (literal.isDefaultNegated()){
								e = new EDGEdge(v, target, EDGEdge.EdgeType.NEG, and, or);
							}
							else {
								e = new EDGEdge(v, target, EDGEdge.EdgeType.POS, and, or);
							}
							edg.addEdge(v, target, e);
						}
					}
				}
			}
			
			/* Add edges to represent contradiction between positive and negative literals */
			HashSet<Contradiction> contradictions = getContradictions();
			ruleNo = p.size();
			
			/* Create contradiction node for each pair of positive and negative literal */
			for (Contradiction c : contradictions){
				for (EDGVertex pos : c.getPositive()){
					for (EDGVertex neg : c.getNegative()){
						ruleNo++;
						EDGVertex con = new EDGVertex("-", ruleNo);
						conNodes.add(con);
						EDGEdge e1 = new EDGEdge(pos, con, EdgeType.NEG,true,false);
						EDGEdge e2 = new EDGEdge(neg, con, EdgeType.NEG,true,false);
						edg.addEdge(pos, con, e1);
						edg.addEdge(neg, con, e2);
					}
				}
			}
			edg.setContradictionNodes(conNodes);
			edg.setNodeMap(nodes);
			edgs.put(null, edg);
			
			/* Create EDGs according to answer sets */
			try {			
				for (AnswerSet as : asl){
					ExtendedDependencyGraph edg2 = edg.deepCopy();
					edg2.setAnswerSet(as);
					edgs.put(as, edg2);
				}
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		else throw new NotValidProgramException();
	}
		
	/**
	 * Checks if program is appropriate for representation as graph
	 * @param p Logic program
	 * @return True, if program is appropriate
	 */
	private boolean isProgramValid(Program p) {
		Iterator<Rule> rules = p.iterator();
		while (rules.hasNext()){
			Rule r = rules.next();
			if (r.numHead() > 1) return false;
			else{
				List<Literal> head = r.getHead();
				for (Literal l : head){
					if (l.isAggregate() || l.isArithmetic() || l.isCondition() ||
						l.isRelational() || l.isWeightLiteral())
						return false;
				}
				
				List<Literal> body = r.getBody();
				for (Literal l : body){
					if (l.isAggregate() || l.isArithmetic() || l.isCondition() ||
						l.isRelational() || l.isWeightLiteral())
						return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Calculates all contradictory nodes
	 * @return Set of all contradictions
	 */
	private HashSet<Contradiction> getContradictions(){
		HashSet<Contradiction> contradictions = new HashSet<Contradiction>();
		for (String positive : positiveNodes.keySet()){
			String negative = "-" + positive;
			if (negativeNodes.containsKey(negative)){
				Contradiction c = new Contradiction(positiveNodes.get(positive), negativeNodes.get(negative));
				contradictions.add(c);
			}
		}
		return contradictions;
	}

	
	
	/**
	 * Returns the controlled Extended Dependency Graph according to the given answer set
	 * @param as Answer Set
	 * @return Extended Dependency Graph
	 */
	public ExtendedDependencyGraph getEDG(AnswerSet as){
		return edgs.get(as);
	}
	
	/**
	 * Returns Mapping from answer set to the specific Extended Dependency Graph
	 * @return Map: AnswerSet -> ExtendedDependencyGraph
	 */
	public HashMap<AnswerSet, ExtendedDependencyGraph> getEDGs(){
		return edgs;
	}

}
