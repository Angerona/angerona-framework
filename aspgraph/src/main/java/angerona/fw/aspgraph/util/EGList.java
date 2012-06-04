package angerona.fw.aspgraph.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import angerona.fw.aspgraph.graphs.ExplanationGraph;

public class EGList {
	
	private HashMap<String, List<ExplanationGraph>> literalEGMap;
	
	public EGList(){
		literalEGMap = new HashMap<String, List<ExplanationGraph>>();
	}
	
	public void addEGtoLiteral(String literal, ExplanationGraph eg){
		
		/* Literal ist bereits in der Map enthalten, EG hinzufügen */
		if (literalEGMap.containsKey(literal)){
			List<ExplanationGraph> egList = literalEGMap.get(literal);
			egList.add(eg);
		}
		/* Literal ist noch nicht in der Map enthalten */
		else{
			List<ExplanationGraph> egList = new LinkedList<ExplanationGraph>();
			egList.add(eg);
			literalEGMap.put(literal, egList);
		}
	}
	
	public List<ExplanationGraph> getEGList(String literal){
		if (literalEGMap.containsKey(literal)) return literalEGMap.get(literal);
		else return null;
	}

}
