package com.github.kreatures.swarm.serialize;



import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="agentType")
public class SwarmAgentTypeConfig {
	@Attribute(name="id")
	protected int idSwarmAgentType;
	@Attribute(name="name")
	protected String nameSwarmAgentType;
	@Attribute(name="x", required=false)
	protected int xSwarmAgentType;
	@Attribute(name="y", required=false)
	protected int ySwarmAgentType;
	@Attribute(name="count")
	protected int countSwarmAgentType;
	@Attribute(name="interface", required=false)
	protected String interfaceSwarmAgentType;
	@Attribute(name="colorRed", required=false)
	protected int colorRedSwarmAgentType;
	@Attribute(name="colorGreen", required=false)
	protected int colorGreenSwarmAgentype;
	@Attribute(name="colorBlue", required=false)
	protected int colorBlueSwarmAgentype;
	
	/* Here are elements there belong only to a agentType. */
	@Element(name="size", required=false)
	protected SwarmAgentTypeConfigSize sizeSwarmAgentType;
	@Element(name="capacity", required=false)
	protected SwarmAgentTypeConfigCapacity capacitySwarmAgentType;
	@Element(name="speed", required=false)
	protected SwarmAgentTypeConfigSpeed speedSwarmAgentType;
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
	
	
	public int getIdSwarmAgentType() {
		return idSwarmAgentType;
	}
	public String getNameSwarmAgentType() {
		return nameSwarmAgentType;
	}
	public int getxSwarmAgentType() {
		return xSwarmAgentType;
	}
	public int getySwarmAgentType() {
		return ySwarmAgentType;
	}
	public int getCountSwarmAgentType() {
		return countSwarmAgentType;
	}
	public String getInterfaceSwarmAgentType() {
		return interfaceSwarmAgentType;
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
	public SwarmAgentTypeConfigSize getSizeSwarmAgentType() {
		return sizeSwarmAgentType;
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
	public SwarmAgentTypeConfigCapacity getCapacitySwarmAgentType() {
		return capacitySwarmAgentType;
	}
	public SwarmAgentTypeConfigSpeed getSpeedSwarmAgentType() {
		return speedSwarmAgentType;
	}
	
}
