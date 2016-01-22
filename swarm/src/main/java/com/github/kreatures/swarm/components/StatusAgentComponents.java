package com.github.kreatures.swarm.components;


import com.github.kreatures.core.BaseAgentComponent;
/**
 * 
 * @author donfack
 *
 */
public class StatusAgentComponents extends SwarmAgentComponentsGeneric {
	/*################# start test #####################*/
	/**
	 * for Test. 
	 */
	private int doJob=0;
	
	public void doWork(){
		doJob+=1;
	}
	
	public boolean finishWwork(){
		return (doJob>=2?true:false);
	}
	

	/*################# End #####################*/
	/**
	 * Default constructor.
	 */
	public StatusAgentComponents() {
		
	}
	
	/**
	 * Constructor allow that the object will be copy.
	 */
	public StatusAgentComponents(StatusAgentComponents other) {
		super(other);
	}



	@Override
	public BaseAgentComponent clone() {
		
		return new StatusAgentComponents(this);
	}

	/**
	 * A agent is on the way to a target's station.
	 */
	@Override
	public boolean isOnWay(){
		return !this.current.equals(this.target) && this.distance>0 && !this.visited; 
	}
	/**
	 * A agent is visiting a station and it is doing its jobs on that station.
	 */
	@Override
	public boolean isVisit(){
		return this.current.equals(this.target) && this.distance==0 && this.visited;
	}
	/**
	 * A agent has selected a station as next destination.
	 * when true then it can leave the current's station.
	 */	
	@Override
	public boolean isLeave(){
		return !this.current.equals(this.target) && this.distance>0 && this.visited;
	}
	/**
	 * A agent is located at a station and want to visit it.
	 * when true then it can visit the target's station.
	 */
	@Override
	public boolean isEnterStation(){
		return this.distance==0 && !this.visited;
	}
	
	/**
	 * This decrements of 1 the distance between current and target station.
	
	public void restWay() {
		this.distance -=1;
	}
	 */
}
