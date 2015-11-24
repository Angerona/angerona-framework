package com.github.angerona.fw.defendingagent.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.github.angerona.fw.defendingagent.CompressedHistory;
import com.github.angerona.fw.defendingagent.GeneralHistory;
import com.github.angerona.fw.defendingagent.History;
import com.github.angerona.fw.defendingagent.HistoryComponent;
import com.github.angerona.fw.gui.view.ListViewColored;
import com.github.angerona.fw.internal.Entity;

/**
 * This class extends the ListViewColored View to report all histories
 * currently held by the agent. 
 * 
 * @author Pia Wierzoch
 */
public class HistoryView extends ListViewColored {


	private static final long serialVersionUID = 1L;


	@Override
	protected List<String> getStringRepresentation(Entity obj) {
		List<String> reval = new LinkedList<>();
		if(obj instanceof HistoryComponent){
			HistoryComponent historyComponent = (HistoryComponent)obj;
			Map<String, GeneralHistory> histories = historyComponent.getHistories();
			for(String agent : histories.keySet()) {
				if(histories.get(agent) instanceof History){
					History currentHistory = (History)histories.get(agent);
					reval.add("History for agent " + agent + ": <Perception, Answervalue> with");
					for(Object entry :currentHistory.getHistory()){
						reval.add("    " + entry.toString());	
					}
				}else if(histories.get(agent) instanceof CompressedHistory){
					CompressedHistory currentHistory = (CompressedHistory)histories.get(agent);
					reval.add("History for agent " + agent + ": <Query false/true, Query unknown, Update> with");
					for(Object entry :currentHistory.getHistory()){
						reval.add("    " + entry.toString());	
					}
				}
			}
		}
		return reval;
	}

	@Override
	public Class<? extends HistoryComponent> getObservedType() {
		return HistoryComponent.class;
	}

}
