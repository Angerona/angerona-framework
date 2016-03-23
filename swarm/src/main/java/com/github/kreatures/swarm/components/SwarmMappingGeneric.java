package com.github.kreatures.swarm.components;


import java.util.Hashtable;
import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.BaseAgentComponent;
import com.github.kreatures.core.comp.Presentable;
import com.github.kreatures.core.serialize.CreateKReaturesXMLFileDefault;
import com.github.kreatures.swarm.serialize.SwarmConfigRead;
import com.github.kreatures.swarm.serialize.SwarmPerspectiveConfig;
import com.github.kreatures.swarm.serialize.SwarmPlaceEdgeConfig;
import com.github.kreatures.swarm.serialize.SwarmStationTypeConfig;
/**
 * 
 * @author donfack
 *
 */

public  class SwarmMappingGeneric extends BaseAgentComponent implements SwarmMapping, Presentable{
	private static final Logger LOG = LoggerFactory.getLogger(SwarmMappingGeneric.class);
	private static SwarmConfigRead swarmconfig;
	//private CreateKReaturesXMLFileDefault kreaturesXMLFile;
	/**
	 * This hashtable contents all relations between a agent and him visitEdge.
	 */
	private static Hashtable<DefaultVisitEdge, String> hashSwarmVisitEdges;
	/**
	 * This hashtable contents all relations between a placeEdge and the corresponding stations.
	 */
	private static Hashtable<String,DefaultEdge> hashSwarmEdges;
	/**
	 * That is a collection of all stations which exist in the scenario.
	 */
	private static Hashtable<String, DefaultStation> hashSwarmSations;

	/**
	 * This is a collections of all informations about the agent's characteristic 
	 */
	//private static Hashtable<String, StatusAgentComponents> hashSwarmAgent;

	/**
	 * The constructor without parameter
	 */
	public SwarmMappingGeneric() {

		//This is a singleton and has to be declared one time.
		swarmconfig=CreateKReaturesXMLFileDefault.getSwarmConfig();
		//This is a singleton and has to be declared one time.
		if(hashSwarmSations==null){
			hashSwarmSations=new Hashtable<String, DefaultStation>() ;
			setAllStations();
		}else {
			return;
		}

		//This is a singleton and has to be declared one time.
		if(hashSwarmEdges==null){
			hashSwarmEdges=new  Hashtable<String, DefaultEdge>();
			setAllEdges();
			setAllEdgesOfAllStations();
		}

		//This is a singleton and has to be declared one time.
		if(hashSwarmVisitEdges==null){
			hashSwarmVisitEdges=new Hashtable<DefaultVisitEdge, String>() ;
			//setAllStations();
		}	
	}

	/**
	 * The constructor with parameter, which is use to clone the object.
	 */
	public SwarmMappingGeneric(SwarmMappingGeneric swarmMappingGeneric) {
		super(swarmMappingGeneric);
		//this.kreaturesXMLFile =swarmMappingGeneric.kreaturesXMLFile ;
	}

	/**
	 * This constructor has to be run only one time, in order to load all datas.	
	 * @param filepath file's direction + name, where the swarmXML is.
	 * @throws Exception When the file wasn't found.
	 */
	public SwarmMappingGeneric(String filepath) throws Exception {
		//This is a singleton and has to be declared one time.
		swarmconfig=CreateKReaturesXMLFileDefault.getSwarmConfig();

		//This is a singleton and has to be declared one time.
		if(hashSwarmSations==null){
			hashSwarmSations=new Hashtable<String, DefaultStation>() ;
			setAllStations();
		}else {
			return;
		}

		//This is a singleton and has to be declared one time.
		if(hashSwarmEdges==null){
			hashSwarmEdges=new  Hashtable<String, DefaultEdge>();
			setAllEdges();
			setAllEdgesOfAllStations();
		}

		//This is a singleton and has to be declared one time.
		if(hashSwarmVisitEdges==null){
			hashSwarmVisitEdges=new Hashtable<DefaultVisitEdge, String>() ;
			//setAllStations();
		}		
	} 

	/**
	 * This throw a exception when the name no exist or null.
	 * @param stationName
	 * @return
	 */

	@Override
	public DefaultStation getStationIntance(String stationName) {		
		if(stationName!=null){
			DefaultStation station= hashSwarmSations.get(stationName); 
			if(station!=null){
				return station;
			}
		}
		LOG.debug("the station whose name is "+stationName+"  doesn't exist.");
		throw new IllegalArgumentException("The name of the station must be exist.");
	}

	@Override
	public Hashtable<String, DefaultEdge> getAllEdgeOfSation(String stationName) {

		if(stationName==null)
			throw new IllegalArgumentException("The name of the station must not be null.");

		DefaultStation defaultStation=getStationIntance(stationName);

		if(defaultStation==null)
			throw new IllegalArgumentException("The name of the station must not be null.");


		//		for(DefaultEdge edge: listSwarmEdges){
		//			if(edge.getOutgoingStation()==defaultStation.getStationId() || defaultStation.getStationId()==edge.getIncomingStation()){
		//				defaultStation.listofEdge.add(edge);
		//			}
		//
		//		}

		return  defaultStation.hashTableofEdge;
	}
	/**
	 * This gives all stations there are in the scenario.
	 * @return a list of all Stations
	 */
	public Hashtable<String,DefaultStation> getAllStations(){
		return hashSwarmSations ;
	}
	/**
	 * This gives all stations there are in the scenario.
	 */
	private void setAllStations(){
		DefaultStation defaultStation;

		for (SwarmPerspectiveConfig perception: swarmconfig.getListPerspective())
			for (SwarmStationTypeConfig station:perception.getListStationType()){

				defaultStation=new DefaultStation(station.getNameSwarmStationType(), station.getIdSwarmStationType());

				defaultStation.count=station.getCountSwarmStationType();

				if(station.getItemSwarmStationType()!=null){
					defaultStation.item=station.getItemSwarmStationType().getValue();
				}

				if(station.getTimeSwarm()!=null){
					defaultStation.time=station.getTimeSwarm().getValue();
				}

				if(station.getSpaceSwarmStationType()!=null){
					defaultStation.space=station.getSpaceSwarmStationType().getValue();
				}

				if(station.getFrequencySwarm()!=null){
					defaultStation.frequency =station.getFrequencySwarm().getValue();
				}

				if(station.getPrioritySwarm()!=null){
					defaultStation.priority=station.getPrioritySwarm().getValue();
				}

				if(station.getNecessitySwarm()!=null){
					defaultStation.necessity=station.getNecessitySwarm().getValue();
				}

				if(station.getCycleSwarm()!=null){
					defaultStation.cycle=station.getCycleSwarm().getValue();
				}
				hashSwarmSations.put(defaultStation.name,defaultStation);
			}		
	}
	/**
	 * This gives all edges there are in the scenario.
	 * @return a list of all edges
	 */
	public Hashtable<String,DefaultEdge> getAllEdges(){
		return hashSwarmEdges;
	}

	/**
	 * This gives all edges there are in the scenario.
	 */
	private void setAllEdges(){
		boolean directed=false;
		DefaultEdge defaultEdge;
		String outgoingStation=null;
		String incomingStation=null;
		boolean hilfsVariable=false;

		for (SwarmPlaceEdgeConfig placedEdge: swarmconfig.getListPlaceEdge()){
			//the directed parameter of SwarmPlaceEdgeConfig is a String. Because of that we convert it to boolean.
			if(placedEdge.getDirectedSwarmPlaceEdge().equals("yes")){
				directed=true;
			}

			for(DefaultStation station: hashSwarmSations.values()){
				if(placedEdge.getFirstConnectedIdRefSwarmPlaceEdge()==station.getStationId()){
					outgoingStation=station.getStationName();
					if(!hilfsVariable){
						hilfsVariable=true;
						continue;
					}else{
						break;
					}

				}

				if(placedEdge.getSecondConnectedIdRefSwarmPlaceEdge()==station.getStationId()){
					incomingStation=station.getStationName();
					if(!hilfsVariable){
						hilfsVariable=true;
					}else{
						break;
					}
				}

			}

			defaultEdge=new DefaultEdge(placedEdge.getIdSwarmPlaceEdge(),outgoingStation,incomingStation, placedEdge.getValueSwarmPlaceEdge(), directed);

			hashSwarmEdges.put(outgoingStation+";"+incomingStation, defaultEdge);

			//The variables will be initialized for the next edge.
			directed=false;
			hilfsVariable=false;
			outgoingStation=null;			
			incomingStation=null;
		}
	}
	/**
	 * for each station, This creates the edges's collection which belong to it.
	 * 
	 */
	private void setAllEdgesOfAllStations(){
		//For each station, This defines all its edges.
		for(DefaultStation station:hashSwarmSations.values()){
			for(DefaultEdge edge:hashSwarmEdges.values()){
				if(edge.getIncomingStation().equals(station.getStationName()) || edge.getOutgoingStation().equals(station.getStationName())){
					station.hashTableofEdge.put(edge.name,edge);
				}
			}
		}
	}

	/**
	 * 
	 * @param departStationName is the name of the outgoing's station
	 * @param arriveStationName is the name of the incoming's station
	 * @return a PlacedEdge, when there is a edge, otherwise Null. 
	 * It returns also Null, when either departStationName or arriveStationName null.
	 */
	@Override
	public DefaultEdge getEdgeIntance(String departStationName, String arriveStationName) {
		if(departStationName!=null && arriveStationName!=null ){
			String hilfsVariable=departStationName+";"+arriveStationName;
			DefaultEdge placedEdge=hashSwarmEdges.get(hilfsVariable);
			if(placedEdge!=null){
				return placedEdge;
			}
			hilfsVariable=arriveStationName+";"+departStationName;
			placedEdge=hashSwarmEdges.get(hilfsVariable);
			if(placedEdge!=null){
				return placedEdge;
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
		if(hashSwarmSations==null)
			return;
		for(DefaultStation station :hashSwarmSations.values()){
			representation.add("Station name: " +station.name);
			representation.add("Capacity: " + station.space);
			representation.add("number of busied place: " + station.getPlacesBusy());
		}

		//This is a singleton and has to be declared one time.
		if(hashSwarmEdges==null)
			return;

		for(DefaultEdge edge :hashSwarmEdges.values()){
			representation.add("edge id: " +edge.getIdEdge());
			representation.add("outgoing's station: " + edge.outgoingStation);
			representation.add("incoming's station: " + edge.incomingStation);
			representation.add("lenght: " + edge.edgeLenght);
			representation.add("undirected: " + edge.underected);
		}

	}

	//	@Override
	//	public DefaultEdge getEdgeIntance(int edgeId) {
	//		// TODO Auto-generated method stub
	//		return null;
	//	}


}