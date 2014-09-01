package com.github.angerona.fw.motivation.island.comp;

import java.util.Arrays;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.motivation.island.enums.Location;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class Area extends BaseAgentComponent {

	protected Location location = Location.AT_SITE;
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

	public void build(int step) {
		for (int i = 0; i < 16; i++) {
			if (i < 8) {
				if (solid[i] < 8) {
					solid[i] = Math.min(solid[i] + step, 8);
					return;
				}
			} else {
				if (vulnurable[i - 8] < 8) {
					vulnurable[i - 8] = Math.min(vulnurable[i - 8] + step, 8);
					return;
				}
			}
		}
	}

	public String getExpansion() {
		return Arrays.toString(solid) + " " + Arrays.toString(vulnurable);
	}

	public int getOn_way() {
		return on_way;
	}

	public void setOn_way(int on_way) {
		this.on_way = on_way;
	}

	public boolean isSecured() {
		return secured;
	}

	public void setSecured(boolean secured) {
		this.secured = secured;
	}

	public boolean isShelter() {
		return location == Location.AT_HQ || location == Location.IN_CAVE;
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
