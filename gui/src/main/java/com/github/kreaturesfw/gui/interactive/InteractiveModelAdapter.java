package com.github.kreaturesfw.gui.interactive;

import com.github.kreaturesfw.core.KReaturesEnvironment;
import com.github.kreaturesfw.core.legacy.InteractiveAgent;
import com.github.kreaturesfw.core.util.ModelAdapter;

/**
 * 
 * @author Pia Wierzoch
 */
public class InteractiveModelAdapter extends ModelAdapter {
	private String[] receiver, actionTypes = { "Query", "Revision" };
	private InteractiveAgent a;
	
	public InteractiveModelAdapter(KReaturesEnvironment environment){
		this.receiver = environment.getAgentNames().toArray(new String[environment.getAgentNames().size()]);
		
		//find the agent controlled by the user
		for(String agent : this.receiver){
			if(environment.getAgentByName(agent) instanceof InteractiveAgent){
				a = (InteractiveAgent) environment.getAgentByName(agent);
				break;
			}
		}
		
		//filter the sending agent from the receiverlist
		String[] rec = new String[this.receiver.length-1];
		int i = 0;
		for(String agent : this.receiver){
			if(!agent.equals(a.getName())){
				rec[i++] = agent;
			}
		}
		this.receiver = rec;
	}

	public String[] getReceiver(){
		return this.receiver;
	}
	
	public String[] getActionTypes(){
		return this.actionTypes;
	}
	
	public InteractiveAgent getAgent(){
		return a;
	}
}
