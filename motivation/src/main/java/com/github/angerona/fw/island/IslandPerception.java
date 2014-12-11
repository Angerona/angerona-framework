package com.github.angerona.fw.island;

import com.github.angerona.fw.Perception;
import com.github.angerona.fw.island.enums.Location;
import com.github.angerona.fw.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandPerception implements Perception {

	private String receiverId;

	private int energyValue;
	private Location location;
	private Weather weather;
	private Weather prediction;
	private int remaining;
	private boolean secured;

	public IslandPerception(String receiverId, int energyValue, Location location, Weather weather, Weather prediction, int remaining,
			boolean secured) {
		this.receiverId = receiverId;
		this.energyValue = energyValue;
		this.location = location;
		this.weather = weather;
		this.prediction = prediction;
		this.remaining = remaining;
		this.secured = secured;
	}

	@Override
	public String getReceiverId() {
		return receiverId;
	}

	public int getEnergyValue() {
		return energyValue;
	}

	public Location getLocation() {
		return location;
	}

	public Weather getWeather() {
		return weather;
	}

	public Weather getPrediction() {
		return prediction;
	}

	public int getRemaining() {
		return remaining;
	}

	public boolean isSecured() {
		return secured;
	}

	@Override
	public String toString() {
		return "[" + receiverId + ", " + energyValue + ", " + location + ", " + weather + ", " + prediction + ", " + remaining + ", " + secured
				+ "]";
	}

}
