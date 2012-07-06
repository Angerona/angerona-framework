package angerona.fw.aspgraph.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import angerona.fw.aspgraph.graphs.ExplanationGraph;

public class EGList {
	
	private HashMap<Set<String>, HashMap<String, Set<ExplanationGraph>>> assumptionEGMap;
	
	public EGList(){
		assumptionEGMap = new HashMap<Set<String>, HashMap<String, Set<ExplanationGraph>>>();
	}
	
	public void addEGtoLiteral(String literal, Set<String> assumption, ExplanationGraph eg){
		/* Assumption already contained in map */
		if (assumptionEGMap.containsKey(assumption)){
			HashMap<String, Set<ExplanationGraph>> literalEGMap = assumptionEGMap.get(assumption);
			/* Literal ist bereits in der Map enthalten, EG hinzufügen */
			if (literalEGMap.containsKey(literal)){
				Set<ExplanationGraph> egList = literalEGMap.get(literal);
				egList.add(eg);
			}
			/* Literal ist noch nicht in der Map enthalten */
			else{
				Set<ExplanationGraph> egList = new HashSet<ExplanationGraph>();
				egList.add(eg);
				literalEGMap.put(literal, egList);
			}
		} else {
			HashMap<String, Set<ExplanationGraph>> literalEGMap = new HashMap<String, Set<ExplanationGraph>>();
			Set<ExplanationGraph> egList = new HashSet<ExplanationGraph>();
			egList.add(eg);
			literalEGMap.put(literal, egList);
			assumptionEGMap.put(assumption, literalEGMap);
		}
	}
	
	public Set<ExplanationGraph> getEGList(String literal, Set<String> assumption){
		if (assumptionEGMap.containsKey(assumption)){
			if (assumptionEGMap.get(assumption).containsKey(literal)){
				return assumptionEGMap.get(assumption).get(literal);		
			}
		}
		return null;
	}

}
