package com.github.kreatures.swarm.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="stationType")
public class SwarmStationTypeConfig {
	@Attribute(name="id")
	protected int idSwarmStationType;
	@Attribute(name="name")
	protected String nameSwarmStationType;
	@Attribute(name="x", required=false)
	protected int xSwarmStationType;
	@Attribute(name="y", required=false)
	protected int ySwarmStationType;
	@Attribute(name="count")
	protected int countSwarmStationType;
	@Attribute(name="colorRed", required=false)
	protected int colorRedSwarmAgentType;
	@Attribute(name="colorGreen", required=false)
	protected int colorGreenSwarmAgentype;
	@Attribute(name="colorBlue", required=false)
	protected int colorBlueSwarmAgentype;
	@Attribute(name="interface")
	protected String interfaceSwarmStationType;
	
	/* Here are elements there belong only to a stationType. */
	
	@Element(name="space", required=false)
	protected SwarmStationTypeConfigSpace  spaceSwarmStationType;
	@Element(name="item", required=false)
	protected SwarmStationTypeConfigItem  itemSwarmStationType;
	
	/* Here are attributes there are also defined in station's class */
	@Element(name="priority", required=false)
	protected SwarmConfigPriority prioritySwarm;
	@Element(name="frequency", required=false)
	protected SwarmConfigFrequency frequencySwarm;
	@Element(name="necessity", required=false)
	protected SwarmConfigNecessity necessitySwarm;
	@Element(name="time", required=false)
	protected SwarmConfigTime timeSwarm;
	@Element(name="cycle", required=false)
	protected SwarmConfigCycle cycleSwarm;
	
	
	public int getIdSwarmStationType() {
		return idSwarmStationType;
	}
	public String getNameSwarmStationType() {
		return nameSwarmStationType;
	}
	public int getxSwarmStationType() {
		return xSwarmStationType;
	}
	public int getySwarmStationType() {
		return ySwarmStationType;
	}
	public int getCountSwarmStationType() {
		return countSwarmStationType;
	}
	public int getColorRedSwarmAgentType() {
		return colorRedSwarmAgentType;
	}
	public int getColorGreenSwarmAgentype() {
		return colorGreenSwarmAgentype;
	}
	
	public int getColorBlueSwarmAgentype() {
		return colorBlueSwarmAgentype;
	}
	public String getInterfaceSwarmStationType() {
		return interfaceSwarmStationType;
	}
	public SwarmStationTypeConfigSpace getSpaceSwarmStationType() {
		return spaceSwarmStationType;
	}
	public SwarmStationTypeConfigItem getItemSwarmStationType() {
		return itemSwarmStationType;
	}
	public SwarmConfigPriority getPrioritySwarm() {
		return prioritySwarm;
	}
	public SwarmConfigFrequency getFrequencySwarm() {
		return frequencySwarm;
	}
	public SwarmConfigNecessity getNecessitySwarm() {
		return necessitySwarm;
	}
	public SwarmConfigTime getTimeSwarm() {
		return timeSwarm;
	}
	public SwarmConfigCycle getCycleSwarm() {
		return cycleSwarm;
	}

}