package com.github.angerona.fw.island.operators;

import static com.github.angerona.fw.island.data.IslandDesires.FILL_BATTERY;
import static com.github.angerona.fw.island.data.IslandDesires.FIND_SHELTER;
import static com.github.angerona.fw.island.data.IslandDesires.FINISH_WORK;
import static com.github.angerona.fw.island.data.IslandDesires.SECURE_SITE;

import com.github.angerona.fw.am.secrecy.operators.BaseGenerateOptionsOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;
import com.github.angerona.fw.logic.Desires;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandGenerateOptionsOperator extends BaseGenerateOptionsOperator {

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
			
			if (!area.isShelter()) {
				desires.add(FIND_SHELTER);
			}
			
			if (!area.isSecured()) {
				desires.add(SECURE_SITE);
			}
		}

		return 0;
	}

}
