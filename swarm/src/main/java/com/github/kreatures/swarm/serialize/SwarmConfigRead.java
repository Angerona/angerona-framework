package com.github.kreatures.swarm.serialize;


import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementListUnion;
import org.simpleframework.xml.Root;

import com.github.kreatures.core.report.Report;

import bibliothek.gui.dock.perspective.Perspective;


@Root(name="logisticsGraph")
public class SwarmConfigRead implements SwarmConfig {
	@Element(name="name", required=false)
	protected String name = "";
	@Element(name="category", required=false)
	protected String category = "";

	@Element(name="description", required=false)
	protected String description = "";
	
	/** used behavior implementation */
	@Element(name="behavior", required=false)
	protected String behaviorCls;
	
/*	@ElementListUnion({
        @ElementList(entry="perspective", type=SwarmPerspectiveConfig.class, inline=true),
        @ElementList(entry="visitEdge", type=SwarmVisitEdgeConfig.class, inline=true),
        @ElementList(entry="timeEdge", type=SwarmTimeEdgeConfig.class, inline=true),
        @ElementList(entry="placeEdge", type=SwarmPlaceEdgeConfig.class, inline=true)
     })
     */
	@ElementList(entry="perspective", type=SwarmPerspectiveConfig.class, inline=true)
	protected List<SwarmPerspectiveConfig> listPerspective;
    @ElementList(entry="visitEdge", type=SwarmVisitEdgeConfig.class, inline=true)
    protected List<SwarmVisitEdgeConfig> listVisitEdge;
    @ElementList(entry="timeEdge", type=SwarmTimeEdgeConfig.class, inline=true)
    protected List<SwarmTimeEdgeConfig> listTimeEdge;
    @ElementList(entry="placeEdge", type=SwarmPlaceEdgeConfig.class, inline=true)
     protected List<SwarmPlaceEdgeConfig> listPlaceEdge;


	public List<SwarmPerspectiveConfig> getListPerspective() {
		return listPerspective;
	}

	public List<SwarmVisitEdgeConfig> getListVisitEdge() {
		return listVisitEdge;
	}

	public List<SwarmTimeEdgeConfig> getListTimeEdge() {
		return listTimeEdge;
	}

	public List<SwarmPlaceEdgeConfig> getListPlaceEdge() {
		return listPlaceEdge;
	}

	@Override
	public String getName() {
		
		return name;
	}

	@Override
	public String getDescription() {
		
		return description;
	}

	@Override
	public String getResourceType() {
		// TODO Auto-generated method stub
		return RESOURCE_TYPE;
	}

	@Override
	public String getCategory() {
		
		return category;
	}
	
	/**
	 * 
	 * @param visitEdge the visit's edge that tell which station a agent can visit
	 * @return name of agent whose id is given in the visitedge.
	 */
	public String getAgentNamefromVisitEdge(SwarmVisitEdgeConfig visitEdge){
		if (visitEdge==null)
			return null;
		if(listPerspective==null)
			return null;
		for(SwarmPerspectiveConfig perceptConfig :listPerspective ){
			if(perceptConfig.getListAgentType().isEmpty())
				return null;
			for(SwarmAgentTypeConfig agentConfig: perceptConfig.getListAgentType()){
				if(agentConfig.getIdSwarmAgentType()==visitEdge.getFirstConnectedIdRefSwarmVisitEdge()){
					return agentConfig.getNameSwarmAgentType();
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param visitEdge the visit's edge that tell which station a agent can visit
	 * @return name of station whose id is given in the visitedge.
	 */
	public String getStationNamefromVisitEdge(SwarmVisitEdgeConfig visitEdge){
		if (visitEdge==null)
			return null;
		if(listPerspective==null)
			return null;
		for(SwarmPerspectiveConfig perceptConfig :listPerspective ){
			if(perceptConfig.getListStationType().isEmpty())
				return null;
			for(SwarmStationTypeConfig stationConfig: perceptConfig.getListStationType()){
				if(stationConfig.getIdSwarmStationType()==visitEdge.getSecondConnectedIdRefSwarmVisitEdge()){
					return stationConfig.getNameSwarmStationType();
				}
			}
		}
		return null;
	}

}
