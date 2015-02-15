package com.github.angerona.fw.island.comp;

import java.util.Arrays;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.island.WeatherChart;
import com.github.angerona.fw.island.enums.Location;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class Area extends BaseAgentComponent {

	private static final int PARTS = 8;
	private static final int INC = 8;

	protected Location location = Location.AT_HQ;
	protected int[] solid = new int[PARTS];
	protected int[] vulnerable = new int[PARTS];
	protected boolean secured = false;
	protected WeatherChart weather;

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
					report("assemble solid parts");
					return;
				}
			} else {
				if (vulnerable[i - PARTS] < INC) {
					vulnerable[i - PARTS] = Math.min(vulnerable[i - PARTS] + step, INC);
					report("assemble vulnerable parts");
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
		Area cln = new Area();
		cln.location = this.location;
		System.arraycopy(this.solid, 0, cln.solid, 0, this.solid.length);
		System.arraycopy(this.vulnerable, 0, cln.vulnerable, 0, this.vulnerable.length);
		cln.secured = this.secured;
		return cln;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
