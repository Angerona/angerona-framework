package com.github.angerona.fw.motivation.island;

import static com.github.angerona.fw.motivation.island.enums.ActionId.MOVE_TO_SITE;
import static com.github.angerona.fw.motivation.island.enums.Location.AT_HQ;
import static com.github.angerona.fw.motivation.island.enums.Location.AT_SITE;
import static com.github.angerona.fw.motivation.island.enums.Location.IN_CAVE;
import static com.github.angerona.fw.motivation.island.enums.Location.ON_THE_WAY_1;
import static com.github.angerona.fw.motivation.island.enums.Location.ON_THE_WAY_2;
import static com.github.angerona.fw.motivation.island.enums.Location.ON_THE_WAY_3;
import static com.github.angerona.fw.motivation.island.enums.Weather.CLOUDS;
import static com.github.angerona.fw.motivation.island.enums.Weather.SUN;

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

		if (act instanceof IslandAction) {
			boolean slow = current != CLOUDS && current != SUN;

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
					battery.charge(6);
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

				perception = new IslandPerception(agent.getName(), battery.getCharge(), area.getLocation(), current, prediction, (tick - 1 % 4),
						area.isSecured());
				agent.perceive(perception);
				LOG.debug("create perception: {}", perception);

				LOG.debug("call agent cycle");
				agent.cycle();
				sendAction(env, new IslandAction(agent, MOVE_TO_SITE));
			}

			LOG.debug("discarge battery");
			battery.discharge();
		}

		return somethingHappens;
	}

}
