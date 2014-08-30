package com.github.angerona.fw.motivation.island;

import static com.github.angerona.fw.motivation.island.enums.EnergyLevel.FULL;
import static com.github.angerona.fw.motivation.island.enums.EnergyLevel.LOW;
import static com.github.angerona.fw.motivation.island.enums.EnergyLevel.MEDIUM;
import static com.github.angerona.fw.motivation.island.enums.EnergyLevel.PARTIAL;

import com.github.angerona.fw.motivation.island.enums.EnergyLevel;
import com.github.angerona.fw.motivation.island.enums.Location;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class EnvModul {

	private int battery = 15;
	private Location location = Location.SITE;
	private int[] solid = new int[8];
	private int[] vulnurable = new int[8];
	private boolean secured = false;
	private boolean damaged = false;
	private int on_way = 0;

	public int getBattery() {
		return battery;
	}

	public EnergyLevel getEnergy() {
		switch (battery) {
		case 15:
		case 14:
		case 13:
		case 12:
			return FULL;
		case 11:
		case 10:
		case 9:
		case 8:
			return PARTIAL;
		case 7:
		case 6:
		case 5:
		case 4:
			return MEDIUM;
		case 3:
		case 2:
		case 1:
		default:
			return LOW;
		}
	}

	public void discharge() {
		battery = Math.max(battery - 1, 0);
	}

	public void charge(int t) {
		battery = Math.min(battery + Math.abs(t), 15);
	}

	public Location getLocation() {
		return location;
	}

	public boolean isShelter() {
		return location == Location.HQ || location == Location.CAVE;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getSolid(int i) {
		return solid[i];
	}

	public void setSolid(int i, int val) {
		this.solid[i] = val;
	}

	public int getVulnurable(int i) {
		return vulnurable[i];
	}

	public void setVulnurable(int i, int val) {
		this.vulnurable[i] = val;
	}

	public boolean reverseVulnerable(int i) {
		if (vulnurable[i] > 0) {
			vulnurable[i] = -vulnurable[i];
			return true;
		}

		return false;
	}

	public boolean isSecured() {
		return secured;
	}

	public void setSecured(boolean secured) {
		this.secured = secured;
	}

	public boolean isDamaged() {
		return damaged;
	}

	public void setDamaged(boolean damaged) {
		this.damaged = damaged;
	}

	public int getOn_way() {
		return on_way;
	}

	public void setOn_way(int on_way) {
		this.on_way = on_way;
	}

}
