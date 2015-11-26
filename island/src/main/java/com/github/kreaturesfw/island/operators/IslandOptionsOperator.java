package com.github.kreaturesfw.island.operators;

import static com.github.kreaturesfw.island.data.IslandDesires.FILL_BATTERY;
import static com.github.kreaturesfw.island.data.IslandDesires.FIND_SHELTER;
import static com.github.kreaturesfw.island.data.IslandDesires.FINISH_WORK;
import static com.github.kreaturesfw.island.data.IslandDesires.SECURE_SITE;

import com.github.kreaturesfw.core.bdi.operators.BaseGenerateOptionsOperator;
import com.github.kreaturesfw.core.bdi.operators.parameter.GenerateOptionsParameter;
import com.github.kreaturesfw.core.logic.Desires;
import com.github.kreaturesfw.island.components.Area;
import com.github.kreaturesfw.island.components.Battery;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandOptionsOperator extends BaseGenerateOptionsOperator {

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
