package com.github.angerona.fw.island.operators;

import static com.github.angerona.fw.island.data.IslandDesires.FILL_BATTERY;
import static com.github.angerona.fw.island.data.IslandDesires.FIND_SHELTER;
import static com.github.angerona.fw.island.data.IslandDesires.FINISH_WORK;
import static com.github.angerona.fw.island.data.IslandDesires.SECURE_SITE;
import static com.github.angerona.fw.island.enums.Weather.THUNDERSTORM;

import java.util.Set;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandIntentionUpdateOperator extends AdvancedIntentionUpdateOperator {

	@Override
	protected Desire chooseIntention(PlanParameter param, Set<Desire> desires, Desire pursued) {
		Area area = param.getAgent().getComponent(Area.class);
		Battery battery = param.getAgent().getComponent(Battery.class);

		if ((battery != null && battery.getCharge() <= 4 || FILL_BATTERY.equals(pursued)) && desires.contains(FILL_BATTERY)) {
			// if battery low or last pursued intention was 'fill battery' then fill battery
			if (desires.contains(SECURE_SITE)) {
				// secure site before leaving
				return SECURE_SITE;
			} else {
				return FILL_BATTERY;
			}
		} else if (area.getWeather().getWeather(0) == THUNDERSTORM) {
			// if weather is dangerous then find shelter
			if (!area.isShelter() && !FIND_SHELTER.equals(pursued) && desires.contains(SECURE_SITE)) {
				// secure site before leaving
				return SECURE_SITE;
			} else if (desires.contains(FIND_SHELTER)) {
				return FIND_SHELTER;
			}
		} else if (desires.contains(FINISH_WORK)) {
			// otherwise just work
			return FINISH_WORK;
		}

		return null;
	}

}
