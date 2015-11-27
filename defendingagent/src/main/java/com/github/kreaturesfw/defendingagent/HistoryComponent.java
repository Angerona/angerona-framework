package com.github.kreaturesfw.defendingagent;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.kreaturesfw.core.basic.BaseAgentComponent;
import com.github.kreaturesfw.core.basic.Perception;
import com.github.kreaturesfw.core.bdi.components.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.AnswerValue;
import com.github.kreaturesfw.plwithknowledge.logic.PLWithKnowledgeBeliefbase;

public class HistoryComponent extends BaseAgentComponent{
	
	private Map<String, GeneralHistory> history = new HashMap<String, GeneralHistory>();
	
	private String instance = null;
	
	/** Default Ctor: Used for dynamic creation, creates empty history */
	public HistoryComponent() {}
	
	/** Copy Ctor */
	public HistoryComponent(HistoryComponent other) {
		super(other);
		for(Entry<String, GeneralHistory> entry : other.history.entrySet()) {
			if(entry.getValue() instanceof CompressedHistory){
				history.put(entry.getKey(), new CompressedHistory((CompressedHistory)entry.getValue()));
			}else if(entry.getValue() instanceof History){
				history.put(entry.getKey(), new History((History)entry.getValue()));
			}
			
		}
		this.instance = other.getInstanceOfHistories();
	}
	
	public String getInstanceOfHistories(){
		return instance;
	}
	
	/**
	 * Updates the History with the received Perception with the corresponding AnswerValue
	 * @param percept The Perception that caused the updateHistory
	 * @param value The AnswerValue for the Perception
	 * @param senderId The ID of the sender of the Perception
	 */
	public void updateHistory(Perception percept, AnswerValue value, String senderId) {
		if(history.get(senderId) == null){
			if(instance.equals("ViewCompressed")){
				history.put(senderId, new CompressedHistory());
			}else if(instance.equals("ViewHistory")){
				history.put(senderId, new History());
			}
		}else{
			history.get(senderId).putAction(percept, value);
		}
	}
	
	@Override
	public BaseAgentComponent clone() {
		return new HistoryComponent(this);
	}
	
	public Map<String, GeneralHistory> getHistories(){
		return history;
	}
	
	@Override
	public void init(Map<String, String> additionalData) {
		if(additionalData.containsKey("View")) {
			instance = additionalData.get("View");
			if(instance.equals("ViewCompressed")){
				Map<String, BaseBeliefbase> bbviews = getAgent().getBeliefs().getViewKnowledge();
				for(String agent : bbviews.keySet()) {
					PLWithKnowledgeBeliefbase bbase = (PLWithKnowledgeBeliefbase) bbviews.get(agent);
					CompressedHistory h = new CompressedHistory();
					h.init(bbase.getAssertions().getLast());
					history.put(agent, h);
				}
			}
		}
	}
}
