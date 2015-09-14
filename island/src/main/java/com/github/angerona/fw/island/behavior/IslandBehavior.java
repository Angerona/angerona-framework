package com.github.angerona.fw.island.behavior;

import static com.github.angerona.fw.island.enums.Location.AT_HQ;
import static com.github.angerona.fw.island.enums.Location.AT_SITE;
import static com.github.angerona.fw.island.enums.Location.IN_CAVE;
import static com.github.angerona.fw.island.enums.Location.ON_THE_WAY_1;
import static com.github.angerona.fw.island.enums.Location.ON_THE_WAY_2;
import static com.github.angerona.fw.island.enums.Location.ON_THE_WAY_3;
import static com.github.angerona.fw.island.enums.Weather.STORM_OR_RAIN;
import static com.github.angerona.fw.island.enums.Weather.THUNDERSTORM;

import java.security.SecureRandom;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;
import com.github.angerona.fw.island.data.IslandAction;
import com.github.angerona.fw.island.enums.Weather;
import com.github.angerona.fw.simple.behavior.SimpleBehavior;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandBehavior extends SimpleBehavior<IslandBehaviorParam> {

	private static final Logger LOG = LoggerFactory.getLogger(IslandBehavior.class);
	
	protected Generator generator = new Generator();

	protected Weather current;
	protected Weather next = generateWeather();
	protected Weather prediction;

	@Override
	public void sendAction(AngeronaEnvironment env, Action act) {
		if (act instanceof IslandAction) {
			boolean slow = current == STORM_OR_RAIN || current == THUNDERSTORM;

			Battery battery = act.getAgent().getComponent(Battery.class);
			Area area = act.getAgent().getComponent(Area.class);

			if(battery == null || area == null) {
				LOG.warn("agent has no battery or area component");
				return;
			}
			
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
	protected IslandBehaviorParam runEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean cycleCondition(AngeronaEnvironment env, Agent agent, IslandBehaviorParam param) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Perception createPerception(AngeronaEnvironment env, Agent agent, IslandBehaviorParam param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void postCycle(AngeronaEnvironment env, Agent agent, IslandBehaviorParam param) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @return the next weather
	 */
	protected Weather generateWeather() {
		return Weather.CLOUDS;
	}

	/**
	 * 
	 * @return the probability, that the weather prediction applies
	 */
	protected Weather prediction(Weather next) {
		return next;
	}

	protected class Generator {

		protected final Random random = new SecureRandom();

		public Generator() {
			random.setSeed(System.nanoTime());
		}

		/**
		 * 
		 * @param k
		 * @param n
		 * @return true with a probability of k/n
		 */
		public boolean chance(int k, int n) {
			if (n < 1) {
				throw new IllegalArgumentException("n must be greater than 0");
			}

			if (k > n) {
				throw new IllegalArgumentException("k must not be greater than n");
			}

			return random.nextInt(n) < k;
		}

		/**
		 * 
		 * @return a random weather occurrence
		 */
		public Weather next() {
			switch (random.nextInt(4)) {
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

}
