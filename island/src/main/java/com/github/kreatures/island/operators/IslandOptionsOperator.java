package com.github.kreatures.island.operators;

import static com.github.kreatures.island.data.IslandDesires.FILL_BATTERY;
import static com.github.kreatures.island.data.IslandDesires.FIND_SHELTER;
import static com.github.kreatures.island.data.IslandDesires.FINISH_WORK;
import static com.github.kreatures.island.data.IslandDesires.SECURE_SITE;

import com.github.kreatures.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.kreatures.island.components.Area;
import com.github.kreatures.island.components.Battery;
import com.github.kreatures.core.logic.Desires;
import com.github.kreatures.simple.operators.OptionsOperator;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandOptionsOperator extends OptionsOperator {

	@Override
	protected Integer processImpl(GenerateOptionsParameter param) {
		Desires desires = param.getAgent().getComponent(Desires.class);
		Area area = param.getAgent().getComponent(Area.class);
		Battery battery = param.getAgent().getComponent(Battery.class);

		if (desires != null && area != null) {
			desires.clear();

			if (!area.isFinished()) {
				desires.add(FINISH_WORK);
			}

			if (battery != null && battery.getCharge() < 15) {
				desires.add(FILL_BATTERY);
			}

			desires.add(FIND_SHELTER);

			if (!area.isSecured()) {
				desires.add(SECURE_SITE);
			}
		}

		return 0;
	}

}
