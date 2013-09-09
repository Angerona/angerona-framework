package com.github.angerona.fw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InteractiveAgent extends Agent{

	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(InteractiveAgent.class);
	
	private boolean hasPerception;
	
	private NextActionRequester nextActionRequester;
	
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
		if(hasPerception)
			hasPerception = nextActionRequester.request();
		return hasPerception;
	}
	
	public void setNextActionRequester(NextActionRequester req){
		this.nextActionRequester = req;
	}
	
}