package angerona.fw;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InteractiveAgent extends Agent implements Observable{

	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(InteractiveAgent.class);
	
	private List<Observer> observer = new LinkedList<Observer>();
	
	private boolean hasPerception, action = false;
	
	public InteractiveAgent(String name, AngeronaEnvironment env) {
		super(name, env);
		hasPerception = true;
	}

//	public boolean cycle() {
//		Perception percept = perceptions.get(0);
//		perceptions.clear();
//		LOG.info("[" + this.getName() + "] Cylce starts: " + percept);
//
//		regenContext();
//		Context c = getContext();
//		c.set("perception", percept);
//		
//		return asmlCylce.execute(c);
//	}
	
	/** 
	 * returns always true, except the User pressed the Finish Button
	 */
	public boolean hasPerceptions() {
		if(!action)this.informAll();
		while(!action){}
		action = false;
		return hasPerception;
	}

	@Override
	public boolean register(Observer o){
		return this.observer.add(o);
	}

	@Override
	public boolean remove(Observer o){
		return this.observer.remove(o);
	}

	@Override
	public void informAll(){
		for(int i  = this.observer.size()-1; i>=0; i--){
			this.observer.get(i).update();
		}
	}

	@Override
	public void inform(Observer o){
		o.update();
	}
	
	public void setHasPerception(boolean a){
		this.hasPerception = a;
	}
	
	public void setAction(boolean a){
		action = a;
	}
	
}