package com.github.kreatures.swarm.components;

import java.util.LinkedList;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.BaseAgentComponent;
import com.github.kreatures.core.comp.Presentable;
import com.github.kreatures.core.serialize.CreateKReaturesXMLDateiDefault;
import com.github.kreatures.swarm.serialize.SwarmConfigRead;
import com.github.kreatures.swarm.serialize.SwarmPerspectiveConfig;
import com.github.kreatures.swarm.serialize.SwarmPlaceEdgeConfig;
import com.github.kreatures.swarm.serialize.SwarmStationTypeConfig;

public  class SwarmMappingGeneric extends BaseAgentComponent implements SwarmMapping, Presentable{
	private static final Logger LOG = LoggerFactory.getLogger(SwarmMappingGeneric.class);
	private static SwarmConfigRead swarmconfig;
	private CreateKReaturesXMLDateiDefault kreaturesXMLFile;
	private static List<DefaultEdge> listSwarmEdges;//
	private static List<DefaultStation> listSwarmSations;

	/**
	 * The constructor without parameter
	 */
	public SwarmMappingGeneric() {
		 //"src/main/config/PerspektivenLg.xml");
		String filepath="config/swarm/PerspektivenLg.xml"; 
		//This is a singleton and has to be declared one time.
				if(swarmconfig==null){
					try {
						kreaturesXMLFile=new CreateKReaturesXMLDateiDefault(filepath);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					swarmconfig=kreaturesXMLFile.getSwarmConfigRead();
				}
				//This is a singleton and has to be declared one time.
				if(listSwarmEdges==null){
					listSwarmEdges=new LinkedList<DefaultEdge>() ;
					setAllEdges();
				}
				//This is a singleton and has to be declared one time.
				if(listSwarmSations==null){
					listSwarmSations=new LinkedList<DefaultStation>() ;
					setAllStations();
				}
				//For each station, This defines all its edges.
				for(DefaultStation station:listSwarmSations){
					for(DefaultEdge edge:listSwarmEdges){
						if(edge.getIncommingStation()==station.getStationId() || edge.getOutcommingStation()==station.getStationId()){
							station.listofEdge.add(edge);
						}
					}
				}
	}

	/**
	 * The constructor with parameter, which is use to clone the object.
	 */
	public SwarmMappingGeneric(SwarmMappingGeneric swarmMappingGeneric) {
		super(swarmMappingGeneric);
		this.kreaturesXMLFile =swarmMappingGeneric.kreaturesXMLFile ;
	}

	/**
	 * This constructor has to be run only one time, in order to load all datas.	
	 * @param filepath file's direction + name, where the swarmXML is.
	 * @throws Exception When the file wasn't found.
	 */
	public SwarmMappingGeneric(String filepath) throws Exception {
		//This is a singleton and has to be declared one time.
		if(swarmconfig==null){
			kreaturesXMLFile=new CreateKReaturesXMLDateiDefault(filepath);//"src/main/config/PerspektivenLg.xml");
			swarmconfig=kreaturesXMLFile.getSwarmConfigRead();
		}
		//This is a singleton and has to be declared one time.
		if(listSwarmEdges==null){
			listSwarmEdges=new LinkedList<DefaultEdge>() ;
			setAllEdges();
		}
		//This is a singleton and has to be declared one time.
		if(listSwarmSations==null){
			listSwarmSations=new LinkedList<DefaultStation>() ;
			setAllStations();
		}
		//For each station, This defines all its edges.
		for(DefaultStation station:listSwarmSations){
			for(DefaultEdge edge:listSwarmEdges){
				if(edge.getIncommingStation()==station.getStationId() || edge.getOutcommingStation()==station.getStationId()){
					station.listofEdge.add(edge);
				}
			}
		}
		
	} 

	@Override
	public DefaultStation getStationIntance(String stationName) {
		// TODO Auto-generated method stub
		for(DefaultStation station:listSwarmSations){
			if(station.getName().equalsIgnoreCase(stationName))
				return station;
		}
		LOG.debug("the station wohse name is "+stationName+"  doesn't exist.");
		throw new IllegalArgumentException("The name of the station must be exist.");
	}
	@Override
	public DefaultStation getStationIntance(int stationId) {
		// TODO Auto-generated method stub
		for(DefaultStation station:listSwarmSations){
			if(station.getStationId()==stationId)
				return station;
		}
		LOG.debug("the station wohse name is "+stationId+"  doesn't exist.");
		throw new IllegalArgumentException("The id of the station must be exist.");
	}

	@Override
	public DefaultEdge getEdgeIntance(int  edgeId) {
		for(DefaultEdge edge:listSwarmEdges){
			if(edge.getIdEdge()==edgeId)
				return edge;
		}
		LOG.debug("the edge whose id is "+edgeId+" doesn't exist.");
		throw new IllegalArgumentException("The id of the edge must be exist.");
	}

	@Override
	public List<DefaultEdge> getAllEdgeOfSation(String stationName) {
		// TODO Auto-generated method stub
		if(stationName==null)
			throw new IllegalArgumentException("The name of the station must not be null.");

		DefaultStation defaultStation=getStationIntance(stationName);

		if(defaultStation==null)
			throw new IllegalArgumentException("The name of the station must not be null.");

		for(DefaultEdge edge: listSwarmEdges){
			if(edge.getIncommingStation()==defaultStation.getStationId() || defaultStation.getStationId()==edge.getOutcommingStation()){
				defaultStation.listofEdge.add(edge);
			}

		}

		return  defaultStation.listofEdge;
	}
	/**
	 * This gives all stations there are in the scenario.
	 * @return a list of all Stations
	 */
	public List<DefaultStation> getAllStations(){
		return listSwarmSations ;
	}
	/**
	 * This gives all stations there are in the scenario.
	 */
	private void setAllStations(){
		DefaultStation defaultStation;
		for (SwarmPerspectiveConfig perception: swarmconfig.getListPerspective())
			for (SwarmStationTypeConfig station:perception.getListStationType()){
				defaultStation=new DefaultStation(station.getIdSwarmStationType(), station.getCountSwarmStationType(),station.getNameSwarmStationType());
				//defaultStation.
				listSwarmSations.add(defaultStation);
			}		
	}
	/**
	 * This gives all edges there are in the scenario.
	 * @return a list of all edges
	 */
	public List<DefaultEdge> getAllEdges(){
		return listSwarmEdges;
	}
	/**
	 * This gives all edges there are in the scenario.
	 */
	private void setAllEdges(){
		boolean directed=false;
		DefaultEdge defaultEdge;
		for (SwarmPlaceEdgeConfig placedEdge: swarmconfig.getListPlaceEdge()){
			//the directed parameter of SwarmPlaceEdgeConfig is a String. Because of that we convert it to boolean.
			if(placedEdge.getDirectedSwarmPlaceEdge().equals("yes"))
				directed=true;

			defaultEdge=new DefaultEdge(placedEdge.getIdSwarmPlaceEdge() ,placedEdge.getFirstConnectedIdRefSwarmPlaceEdge(),placedEdge.getSecondConnectedIdRefSwarmPlaceEdge(), placedEdge.getValueSwarmPlaceEdge(), directed);

			listSwarmEdges.add(defaultEdge);
		}
	}
	@Override
	public DefaultEdge getEdgeIntance(String departStationName, String arriveStationName) {
		if(departStationName!=null && arriveStationName!=null ){
			DefaultStation departStation=getStationIntance(departStationName);
			DefaultStation arriveStation=getStationIntance(arriveStationName);
			for(DefaultEdge edge: departStation.getListofEdge()){
				if(edge.isUnderected() ){
					if(edge.getOutcommingStation()==arriveStation.getStationId())
						return edge;
				}else if(edge.getIncommingStation()==arriveStation.getStationId()|| edge.getOutcommingStation()==arriveStation.getStationId()){
					return edge;
				}
			}
		}
		LOG.debug("no edge between "+departStationName+" and "+arriveStationName);
		return null; 
	}
	/**
	 * return a clone of agentComponent of the calling object. 
	 */
	@Override
	public BaseAgentComponent clone() {

		return new SwarmMappingGeneric(this) ;
	}
	
	@Override
	public String toString() {
		return "Platform: []";
	}

	@Override
	public void getRepresentation(List<String> representation) {
		//This is a singleton and has to be declared one time.
		if(listSwarmSations==null)
			return;
			
		for(DefaultStation station :listSwarmSations){
			representation.add("Station id: " +station.getStationId());
			representation.add("Station name: " +station.name);
			representation.add("Capacity: " + station.stationCapacity);
			representation.add("number of busied place: " + station.getPlacesBusy());
		}
		//This is a singleton and has to be declared one time.
		if(listSwarmEdges==null)
			return;
		
		for(DefaultEdge edge :listSwarmEdges){
			representation.add("edge id: " +edge.getIdEdge());
			representation.add("start's station: " + edge.incommingStation);
			representation.add("end's station: " + edge.outcommingStation);
			representation.add("lenght: " + edge.edgeLenght);
			representation.add("undirected: " + edge.underected);
		}
		
	}

	
}