package com.github.kreaturesfw.island.behavior;

import static com.github.kreaturesfw.island.enums.Location.AT_HQ;
import static com.github.kreaturesfw.island.enums.Location.AT_SITE;
import static com.github.kreaturesfw.island.enums.Location.IN_CAVE;
import static com.github.kreaturesfw.island.enums.Location.ON_THE_WAY_1;
import static com.github.kreaturesfw.island.enums.Location.ON_THE_WAY_2;
import static com.github.kreaturesfw.island.enums.Location.ON_THE_WAY_3;
import static com.github.kreaturesfw.island.enums.Weather.STORM_OR_RAIN;
import static com.github.kreaturesfw.island.enums.Weather.THUNDERSTORM;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.Action;
import com.github.kreaturesfw.core.Agent;
import com.github.kreaturesfw.core.AngeronaEnvironment;
import com.github.kreaturesfw.core.Perception;
import com.github.kreaturesfw.island.components.Area;
import com.github.kreaturesfw.island.components.Battery;
import com.github.kreaturesfw.island.data.IslandAction;
import com.github.kreaturesfw.island.data.IslandPerception;
import com.github.kreaturesfw.island.data.WeatherChart;
import com.github.kreaturesfw.island.enums.Location;
import com.github.kreaturesfw.island.enums.Weather;
import com.github.kreaturesfw.simple.behavior.Generator;
import com.github.kreaturesfw.simple.behavior.SimpleBehavior;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandBehavior extends SimpleBehavior {

	private static final Logger LOG = LoggerFactory.getLogger(IslandBehavior.class);
	public static final int DEFAULT_WEATHER_PERIOD = 4;
	public static final int DEFAULT_CHARGE = 6;

	protected static final Generator GENERATOR = new Generator();

	protected Weather current;
	protected Weather next = nextWeather();
	protected Weather prediction;

	protected int getDurationOfWeatherPeriod() {
		return DEFAULT_WEATHER_PERIOD;
	}

	protected int getChargePerTick() {
		return DEFAULT_CHARGE;
	}

	@Override
	public void sendAction(AngeronaEnvironment env, Action act) {
		if (act instanceof IslandAction) {
			boolean slow = current == STORM_OR_RAIN || current == THUNDERSTORM;

			Battery battery = act.getAgent().getComponent(Battery.class);
			Area area = act.getAgent().getComponent(Area.class);

			switch (((IslandAction) act).getId()) {
			case ASSEMBLE_PARTS:
				if (area.getLocation() == AT_SITE && !area.isSecured()) {
					area.build(slow ? 1 : 2);
				}
				break;
			case CHARGE_BATTERY:
				if (area.getLocation() == AT_HQ) {
					battery.charge(getChargePerTick());
				}
				break;
			case MOVE_TO_SITE:
				switch (area.getLocation()) {
				case AT_HQ:
					area.setLocation(slow ? ON_THE_WAY_1 : ON_THE_WAY_2);
					break;
				case ON_THE_WAY_1:
					area.setLocation(slow ? ON_THE_WAY_2 : ON_THE_WAY_3);
					break;
				case ON_THE_WAY_2:
					area.setLocation(slow ? ON_THE_WAY_3 : AT_SITE);
					break;
				case ON_THE_WAY_3:
					area.setLocation(AT_SITE);
					break;
				default: // ignore
				}
				break;
			case MOVE_TO_HQ:
				switch (area.getLocation()) {
				case AT_SITE:
					area.setLocation(slow ? ON_THE_WAY_3 : ON_THE_WAY_2);
					break;
				case ON_THE_WAY_3:
					area.setLocation(slow ? ON_THE_WAY_2 : ON_THE_WAY_1);
					break;
				case ON_THE_WAY_2:
					area.setLocation(slow ? ON_THE_WAY_1 : AT_HQ);
					break;
				case ON_THE_WAY_1:
					area.setLocation(AT_HQ);
					break;
				default: // ignore
				}
				break;
			case COVER_SITE:
				if (area.getLocation() == AT_SITE && !area.isSecured()) {
					area.setSecured(true);
				}
				break;
			case UNCOVER_SITE:
				if (area.getLocation() == AT_SITE && area.isSecured()) {
					area.setSecured(false);
				}
				break;
			case ENTER_CAVE:
				if (area.getLocation() == AT_SITE) {
					area.setLocation(IN_CAVE);
				}
				break;
			case LEAVE_CAVE:
				if (area.getLocation() == IN_CAVE) {
					area.setLocation(AT_SITE);
				}
				break;
			default:
				LOG.warn("unhandled action-id");
			}
		}
	}

	@Override
	protected void runEnvironment() {
		if (tick % getDurationOfWeatherPeriod() == 1) {
			current = next;
			next = nextWeather();
			prediction = prediction(next);
			LOG.debug("update weather: {}", current);
			LOG.debug("prediction: {}, next: {}", prediction, next);
		}
	}

	@Override
	protected boolean cycleCondition(AngeronaEnvironment env, Agent agent) {
		return !terminationCriterion(env, agent);
	}

	@Override
	protected Perception createPerception(AngeronaEnvironment env, Agent agent) {
		Area area = agent.getComponent(Area.class);
		Battery battery = agent.getComponent(Battery.class);

		area.setWeather(new WeatherChart(current, prediction, rem(tick)));

		return new IslandPerception(agent.getName(), battery.getCharge(), area.getLocation(), current, prediction, rem(tick), area.isSecured());
	}

	/**
	 * 
	 * @param tick
	 * @return the ticks, until new weather is generated
	 */
	protected int rem(int tick) {
		int dur = getDurationOfWeatherPeriod();
		int mod = dur - ((tick - 1) % dur);

		return mod;
	}

	@Override
	protected void postCycle(AngeronaEnvironment env, Agent agent) {
		Area area = agent.getComponent(Area.class);
		Battery battery = agent.getComponent(Battery.class);

		LOG.debug("discarge battery");
		battery.discharge();

		switch (current) {
		case SUN:
			chargeOnSun(battery, !area.isShelter(), 2);
			break;
		case THUNDERSTORM:
			damageOnThunderstorm(area, battery, GENERATOR.chance(1, 8));
			break;
		default: // do nothing
		}
	}

	protected void chargeOnSun(Battery battery, boolean charge, int amount) {
		if (charge) {
			battery.report("charging with solar panel");
			battery.charge(amount);
		}
	}

	protected void damageOnThunderstorm(Area area, Battery battery, boolean damage) {
		if (damage) {
			area.report("lightning stroke occured");

			if (GENERATOR.chance(1, 2)) {
				if (!area.isShelter()) {
					battery.damage();
				}
			} else {
				if (!area.isSecured()) {
					area.damage();
					if (area.getLocation() == Location.AT_SITE) {
						battery.damage();
					}
				}
			}
		}
	}

	@Override
	protected boolean terminationCriterion(AngeronaEnvironment env, Agent agent) {
		Area area = agent.getComponent(Area.class);
		Battery battery = agent.getComponent(Battery.class);

		return area.isFinished() || battery.isDamaged() || battery.isEmpty();
	}

	/**
	 * 
	 * @return the next weather
	 */
	protected Weather nextWeather() {
		return Weather.CLOUDS;
	}

	/**
	 * 
	 * @return the probability, that the weather prediction applies
	 */
	protected Weather prediction(Weather next) {
		return next;
	}

	/**
	 * 
	 * @return a random weather occurrence
	 */
	protected Weather randomWeather() {
		switch (GENERATOR.nextInt(4)) {
		case 1:
			return Weather.SUN;
		case 2:
			return Weather.STORM_OR_RAIN;
		case 3:
			return Weather.THUNDERSTORM;
		default:
			return Weather.CLOUDS;
		}
	}

}
