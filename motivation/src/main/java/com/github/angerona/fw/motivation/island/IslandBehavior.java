package com.github.angerona.fw.motivation.island;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.motivation.basic.ParsingBehavior;
import com.github.angerona.fw.motivation.island.comp.Area;
import com.github.angerona.fw.motivation.island.comp.Battery;
import com.github.angerona.fw.motivation.island.enums.Location;
import com.github.angerona.fw.motivation.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class IslandBehavior extends ParsingBehavior {

	private static final Logger LOG = LoggerFactory.getLogger(IslandBehavior.class);

	protected Generator generator = new Generator();

	protected Weather current;
	protected Weather next = generateWeather();
	protected Weather prediction;

	protected boolean initialized = false;

	/**
	 * 
	 * @return the next weather
	 */
	protected abstract Weather generateWeather();

	/**
	 * 
	 * @return the probability, that the weather prediction applies
	 */
	protected abstract Weather prediction(Weather next);

	@Override
	public void sendAction(AngeronaEnvironment env, Action act) {
		act.getAgent();
	}

	@Override
	protected boolean _runOnTick(AngeronaEnvironment env) {
		if (tick % 4 == 1) {
			current = next;
			next = generateWeather();
			prediction = prediction(next);
			LOG.debug("update weather: {}", current);
			LOG.debug("prediction: {}, next: {}", prediction, next);
		}

		somethingHappens = false;
		Perception perception;
		Battery battery = null;
		Area area = null;

		for (Agent agent : env.getAgents()) {
			battery = agent.getComponent(Battery.class);
			area = agent.getComponent(Area.class);

			switch (current) {
			case SUN:
				if (!area.isShelter()) {
					LOG.debug("charging with solar panel");
					battery.charge(2);
				}
				break;
			case THUNDERSTORM:
				if (generator.chance(1, 8)) {
					if (generator.chance(1, 2)) {
						if (!area.isShelter()) {
							LOG.debug("damage agent");
							battery.damage();
						}
					} else {
						if (!area.isSecured()) {
							LOG.debug("damage site");
							area.damage();
							if (area.getLocation() == Location.AT_SITE) {
								LOG.debug("damage agent");
								battery.damage();
							}
						}
					}
				}
				break;
			default: // do nothing
			}

			if (!battery.isDamaged() && !battery.isEmpty()) {
				somethingHappens = true;

				perception = new IslandPerception(agent.getName(), battery.getCharge(), area.getLocation(), current, prediction, (tick % 4) - 1,
						area.isSecured());
				agent.perceive(perception);
				LOG.debug("create perception: {}", perception);

				LOG.debug("call agent cycle");
				agent.cycle();
			}

			LOG.debug("discarge battery");
			battery.discharge();
		}

		return somethingHappens;
	}

}
