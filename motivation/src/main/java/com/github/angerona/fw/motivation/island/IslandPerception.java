package com.github.angerona.fw.motivation.island;

import com.github.angerona.fw.Perception;
import com.github.angerona.fw.motivation.island.enums.EnergyLevel;
import com.github.angerona.fw.motivation.island.enums.Location;
import com.github.angerona.fw.motivation.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandPerception implements Perception {

	private String receiverId;

	private EnergyLevel energyLevel;
	private Location currentLocation;
	private Weather currentWeather;
	private Weather weatherPrediction;

	public IslandPerception(String receiverId, EnergyLevel energyLevel, Location currentLocation, Weather currentWeather,
			Weather weatherPrediction) {
		this.receiverId = receiverId;
		this.energyLevel = energyLevel;
		this.currentLocation = currentLocation;
		this.currentWeather = currentWeather;
		this.weatherPrediction = weatherPrediction;
	}

	public EnergyLevel getEnergyLevel() {
		return energyLevel;
	}

	public Location getCurrentLocation() {
		return currentLocation;
	}

	public Weather getCurrentWeather() {
		return currentWeather;
	}

	public Weather getWeatherPrediction() {
		return weatherPrediction;
	}

	@Override
	public String getReceiverId() {
		return receiverId;
	}

	@Override
	public String toString() {
		return "[" + receiverId + ", " + energyLevel + ", " + currentLocation + ", " + currentWeather + ", " + weatherPrediction + "]";
	}

}
