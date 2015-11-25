package com.github.kreaturesfw.core;



public class InteractiveAgent extends Agent{

	/** reference to the logback logger instance */
	//private static Logger LOG = LoggerFactory.getLogger(InteractiveAgent.class);
	
	private boolean hasPerception;
	
	private NextActionRequester nextActionRequester;
	
	public InteractiveAgent(String name, AngeronaEnvironment env) {
		super(name, env);
		hasPerception = true;
	}

	public boolean cycle() {
		return hasPerceptions();
	}
	
	/** 
	 * returns always true, except the User pressed the Finish Button
	 */
	public boolean hasPerceptions() {
		if(hasPerception){
			System.out.println("HasPerception true in Interactive Agent");
			nextActionRequester.request();
		}
		return hasPerception;
	}
	
	public void setNextActionRequester(NextActionRequester req){
		this.nextActionRequester = req;
	}
	
	public void setHasPerception(boolean a){
		this.hasPerception = a;
	}
	
}