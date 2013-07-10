package interactive;

import angerona.fw.Agent;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.util.ModelAdapter;

/**
 * 
 * @author Pia Wierzoch
 */
public class InteractiveModelAdapter extends ModelAdapter {
	private String[] receiver, actionTypes = { "Query", "Revision" };
	private Agent a;
	
	public InteractiveModelAdapter(AngeronaEnvironment environment){
		this.receiver = environment.getAgentNames().toArray(new String[environment.getAgentNames().size()]);
		
		//find the agent controlled by the user
		for(String agent : this.receiver){
			if(environment.getAgentByName(agent).getType().equals("User")){
				a = environment.getAgentByName(agent);
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
	
	public Agent getAgent(){
		return a;
	}
}
