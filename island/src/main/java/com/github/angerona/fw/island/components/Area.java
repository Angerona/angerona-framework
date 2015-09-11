package com.github.angerona.fw.island.components;

import java.util.Arrays;
import java.util.List;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.comp.Presentable;
import com.github.angerona.fw.island.data.WeatherChart;
import com.github.angerona.fw.island.enums.Location;

/**
 * the area component represents the agent's environment, thus the island, the
 * weather, and the progress of the site
 * 
 * @author Manuel Barbi
 * 
 */
public class Area extends BaseAgentComponent implements Presentable {

	private static final int PARTS = 8;
	private static final int INC = 8;

	protected Location location = Location.AT_HQ;
	protected int[] solid = new int[PARTS];
	protected int[] vulnerable = new int[PARTS];
	protected boolean secured = true;
	protected WeatherChart weather;

	public Area() {}

	public Area(Area other) {
		super(other);
		this.location = other.location;
		System.arraycopy(other.solid, 0, this.solid, 0, other.solid.length);
		System.arraycopy(other.vulnerable, 0, this.vulnerable, 0, other.vulnerable.length);
		this.secured = other.secured;
		if (other.weather != null)
			this.weather = other.weather.clone();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
		report("new location: " + location);
	}

	public void build(int step) {
		for (int i = 0; i < 2 * PARTS; i++) {
			if (i < PARTS) {
				if (solid[i] < INC) {
					solid[i] = Math.min(solid[i] + step, INC);
					report("assemble solid parts " + getExpansion());
					return;
				}
			} else {
				if (vulnerable[i - PARTS] < INC) {
					vulnerable[i - PARTS] = Math.min(vulnerable[i - PARTS] + step, INC);
					report("assemble vulnerable parts " + getExpansion());
					return;
				}
			}
		}

		if (isFinished()) {
			report("assembled all parts");
		}
	}

	public String getExpansion() {
		return Arrays.toString(solid) + " " + Arrays.toString(vulnerable);
	}

	public boolean isSecured() {
		return secured;
	}

	public void setSecured(boolean secured) {
		this.secured = secured;
		report("site is now secured: " + secured);
	}

	public boolean isShelter() {
		return location == Location.AT_HQ || location == Location.IN_CAVE;
	}

	public WeatherChart getWeather() {
		return weather;
	}

	public void setWeather(WeatherChart weather) {
		this.weather = weather;
	}

	public void damage() {
		int d = 3;

		for (int i = PARTS - 1; i >= 0; i--) {
			if (d-- > 0 && vulnerable[i] > 0) {
				vulnerable[i] = -INC;
			}
		}

		report("site was damaged");
	}

	public boolean isFinished() {
		for (int i = 0; i < PARTS; i++) {
			if (solid[i] < INC) {
				return false;
			}

			if (vulnerable[i] < INC) {
				return false;
			}
		}

		return true;
	}

	@Override
	public BaseAgentComponent clone() {
		return new Area(this);
	}

	@Override
	public void getRepresentation(List<String> representation) {
		representation.add("Location: " + this.getLocation());
		representation.add("Expandsion: " + this.getExpansion());
		representation.add("Secured: " + this.isSecured());
		representation.add("Weather: " + this.getWeather());
	}

}
