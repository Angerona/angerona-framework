package com.github.kreatures.swarm.components;


import com.github.kreatures.core.BaseAgentComponent;
/**
 * 
 * @author donfack
 *
 */
public class DefaultStation extends SwarmStationGeneric{


	public DefaultStation(DefaultStation defaultStation) {
		super(defaultStation);
		// TODO Auto-generated constructor stub
	}

	public DefaultStation(String name, int stationId) {
		super(name,stationId);
		// TODO Auto-generated constructor stub
	}

	public DefaultStation(String name,int stationId,int count, int space,	int item,int time,	int priority,	int frequency,	int necessity,	int cycle) {
		super(name,stationId,count,space,item,time,priority,frequency,necessity,cycle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public BaseAgentComponent clone() {
		// TODO Auto-generated method stub
		return new DefaultStation(this);
	}


}
