package com.github.angerona.fw.island.components;

import static com.github.angerona.fw.island.enums.Weather.THUNDERSTORM;

import java.util.Arrays;

import com.github.angerona.fw.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeatherChart {

	protected Weather[] weather = new Weather[8];
	protected int safeWindow = 0;

	protected WeatherChart() {}

	public WeatherChart(Weather current, Weather prediction, int remaining) {
		for (int t = 0; t < weather.length; t++) {
			if (t < remaining) {
				weather[t] = current;
			} else if (t < remaining + 4) {
				weather[t] = prediction;
			} else {
				weather[t] = null;
			}
		}

		if (current == THUNDERSTORM) {
			safeWindow = 0;
		} else if (prediction == THUNDERSTORM) {
			safeWindow = remaining;
		} else {
			safeWindow = remaining + 4;
		}
	}

	public Weather getWeather(int t) {
		if (t < 0 || t >= weather.length) {
			return null;
		}

		return weather[t];
	}

	public int getSafeWindow() {
		return safeWindow;
	}

	@Override
	protected WeatherChart clone() {
		WeatherChart cln = new WeatherChart();
		System.arraycopy(this.weather, 0, cln.weather, 0, this.weather.length);
		cln.safeWindow = this.safeWindow;
		return cln;
	}

	@Override
	public String toString() {
		return Arrays.toString(weather);
	}

}
