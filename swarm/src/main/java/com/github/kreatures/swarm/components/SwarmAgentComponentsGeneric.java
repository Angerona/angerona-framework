package com.github.kreatures.swarm.components;
import com.github.kreatures.core.comp.Presentable;
import com.github.kreatures.core.serialize.CreateKReaturesXMLFileDefault;
import com.github.kreatures.swarm.serialize.SwarmConfigRead;
import com.github.kreatures.swarm.serialize.SwarmPerspectiveConfig;
import com.github.kreatures.swarm.serialize.SwarmStationTypeConfig;
import com.github.kreatures.swarm.serialize.SwarmVisitEdgeConfig;

import java.util.List;

import com.github.kreatures.core.BaseAgentComponent;

/**
 * @author Donfack
 * A Scenario can have more agent's type. The SwarmAgentGeneric is the abstract class, 
 * which gives all common parameter between agent.
 */
public abstract class SwarmAgentComponentsGeneric extends BaseAgentComponent implements Presentable {
	/**
	 * belong to swarm's config
	 */
	private static SwarmConfigRead swarmconfig;
	private static CreateKReaturesXMLFileDefault kreaturesXMLFile;
	//private static int index=0;
	private static int numberOfStation=1;
	private static List<SwarmStationTypeConfig> listSwarmStationTypeConfig;

	/**
	 * target is the name of the station where the agent is and it is Null when it is on way. 
	 */
	protected String current;
	/**
	 * target is the name of the station where the agent want to move and it is Null when there are no movement. 
	 */
	protected String target;
	/**
	 * visited allows to check if the agent is into the station name target (true) or no (false). 
	 */
	protected boolean visited;

	/**
	 * distance is using to check the distance between a agent and its incommingStation (Here target).  
	 */
	protected int distance;
	
	/**
	 * capacity is a numeric value and means that a agent can transports items whose totals size isn't greater than n with n=capacity.
	 * When this elements not exist, that the value is zero. 
	 */
	protected int capacity=0;
	
	/**
	 * speed is a numeric value and means that a agent moves with the speed n (n=speed) when it goes to the next station.
	 * When this elements not exist, that the value is one. 
	 */
	protected int speed=1;
	
	/**
	 * sizeAgent is a numeric value and means that the visited station has to have a space greater than n, where n=sizeAgent.
	 * When this elements not exist, that the value is zero. 
	 */
	protected int sizeAgent=0;
	
	/**
	 * This is the exactly visited duration, which each agent has when it is visiting a stations.
	 * When a station has its own duration, then visited duration into a given station is the minimum of both.  
	 * When a agent has no item's element, then the itemSize is zero. That means no duration is defined for this agent's type.
	 */
	protected int time=0;
	
	/**
	 * priority is a numeric value. When more agents can visit a station, than agent with high priority has to be prioritized.
	 * When this elements not exist, that the value is zero. 
	 */
	protected int priority=0;
	
	/**
	 * frequency is a numeric value and means that a agent has to visit all allowed stations and the sum of all visits have to be exactly n times, where n=frequency.
	 * When this elements not exist, that the value is zero. 
	 */
	protected int frequency=0;
	
	/**
	 * necessity is a numeric value and means that a agent has to visit all allowed stations exactly n times, where n=necessity.
	 *  When this elements not exist, that the value is zero. 
	 */
	protected int necessity=0;
	
	/**
	 * cycle is a numeric value and it is using for the timeEdge which allow to simulate how agents or stations working in parallel. 
	 * cycle has a sense when there are a timeEdge connected to this station. 
	 * When the timeEdge is directed, than outgoing cycle's value is the same as the cycle's value of incoming.
	 * When this elements not exist, that the value is zero or the value of incoming if exists. 
	 */
	protected int cycle=0;
	

	/**
	 * distance is using to check the distance between a agent and its incommingStation (Here target).  
	 */
	public int getDistance(){
		return this.distance;
	}
	/**
	 * target is the name of the station where the agent is or where it want to go. 
	 */
	public String getTarget(){
		return this.target;
	}

	/**
	 * target is the name of the station where the agent is or Null when it is on way. 
	 */
	public String getCurrent(){
		return this.current;
	}

	/**
	 * visited allows to check if the agent is into the station name target (true) or no (false). 
	 */
	public boolean getVisited(){
		return this.visited;
	}
	/**
	 * Default constructor
	 */
	public SwarmAgentComponentsGeneric(){
		//String filepath="config/swarm/PerspektivenLg.xml";
		//This is a singleton and has to be declared one time.
		if(swarmconfig==null){
			try {//TODO new CreateKReaturesXMLDateiDefault(filepath);
				swarmconfig=CreateKReaturesXMLFileDefault.getSwarmConfigRead();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(!swarmconfig.getListPerspective().isEmpty()){
			SwarmPerspectiveConfig perceptConfig=swarmconfig.getListPerspective().get(0);
			listSwarmStationTypeConfig=perceptConfig.getListStationType();
			numberOfStation=listSwarmStationTypeConfig.size();
			if(numberOfStation>0){
				if(!swarmconfig.getListVisitEdge().isEmpty()){
					for(SwarmVisitEdgeConfig visitEdge: swarmconfig.getListVisitEdge() ){
						//TODO return null has to be check.
						this.current=swarmconfig.getStationNamefromVisitEdge(visitEdge);
						this.target=this.current;
						break;	
					}
				}	
			}				
		}else{
			//TODO
			this.report("There are no stations define.");
		}




		/*
		if (!swarmconfig.getListVisitEdge().isEmpty()){
			for (SwarmVisitEdgeConfig visitEdge : swarmconfig.getListVisitEdge()){
				 if(this.getAgent().getName().equals(swarmconfig.getAgentNamefromVisitEdge(visitEdge))){


					 break;
				 }


			}

		}else{
			report("there are no visit-edges");
		}
		 */


	}
	/**
	 * Constructor allow that the object will be copy.
	 */
	public SwarmAgentComponentsGeneric(SwarmAgentComponentsGeneric other){
		super(other);
		this.target=other.target;
		this.visited=other.visited;
		this.current=other.current;
		this.distance=other.distance;
	}
	/**
	 * target is the name of the station where the agent is or Null when it is on way.
	 * @param current
	 */
	public void setCurrent(String current) {
		this.current = current;
	}
	/**
	 * target is the name of the station where the agent is or Null when it is on way.
	 * @param target
	 */
	public void setTarget(String target) {
		this.target = target;
	}
	/**
	 * visited allows to check if the agent is into the station name target (true) or no (false).
	 * @param visited 
	 */
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	/**
	 * distance is using to check the distance between a agent and its incommingStation (Here target).
	 * @param distance
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	/**
	 * decrement the distance of one.
	 */
	public void move() {
		if (0<distance)
			this.distance -=1;
	}
	/**
	 * A agent is on the way to a target's station.
	 */
	public abstract boolean isOnWay();
	/**
	 * A agent is visiting a station and it is doing its jobs on that station.
	 */
	public abstract boolean isVisit();
	/**
	 * A agent has selected a station as next destination.
	 * when true then it can leave the current's station.
	 */	
	public abstract boolean isLeave();
	/**
	 * A agent is located at a station and want to visit it.
	 * when true then it can visit the target's station.
	 */
	public abstract boolean isEnterStation();

	@Override
	public String toString() {
		return "Status-Agent [Current=" + current + ", next=" + target+ ", rest distance=" + distance+ ", Visited=" + visited +"]";
	}

	@Override
	public void getRepresentation(List<String> representation) {
		representation.add("CurrentStation: " + this.getCurrent());
		representation.add("Next Station: " + this.getTarget());
		representation.add("Distance between current and next station: " + this.getDistance());
		representation.add("Visited: " + this.getVisited());
	}

}
