package com.github.angerona.fw.motivation.island.comp;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.motivation.island.enums.Location;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class Area extends BaseAgentComponent {

	protected Location location = Location.SITE;
	protected int[] solid = new int[8];
	protected int[] vulnurable = new int[8];
	protected boolean secured = false;
	protected int on_way = 0;

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public boolean isSecured() {
		return secured;
	}

	public void setSecured(boolean secured) {
		this.secured = secured;
	}

	public boolean isShelter() {
		return location == Location.HQ || location == Location.CAVE;
	}

	public void damage() {
		// TODO: implement
	}

	@Override
	public BaseAgentComponent clone() {
		Area cln = new Area();
		cln.location = this.location;
		System.arraycopy(this.solid, 0, cln.solid, 0, this.solid.length);
		System.arraycopy(this.vulnurable, 0, cln.vulnurable, 0, this.vulnurable.length);
		cln.secured = this.secured;
		cln.on_way = this.on_way;
		return cln;
	}

}
