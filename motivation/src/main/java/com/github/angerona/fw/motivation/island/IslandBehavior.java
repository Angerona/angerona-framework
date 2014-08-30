package com.github.angerona.fw.motivation.island;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Angerona;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.def.DefaultBehavior;
import com.github.angerona.fw.motivation.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandBehavior extends DefaultBehavior {

	private static final Logger LOG = LoggerFactory.getLogger(IslandBehavior.class);

	private Map<String, EnvModul> moduls = new HashMap<>();
	private WeatherGenerator generator = new WeatherGenerator();

	private Weather current;
	private Weather next = generator.generate();
	private Weather prediction;

	@Override
	public void sendAction(AngeronaEnvironment env, Action act) {
		// TODO Auto-generated method stub
	}

	@Override
	public void receivePerception(AngeronaEnvironment env, Perception percept) {}

	@Override
	public boolean runOneTick(AngeronaEnvironment env) {
		doingTick = true;
		angeronaReady = false;

		if (tick++ % 4 == 0) {
			current = next;
			next = generator.generate();
			prediction = generator.prediction(next);
			LOG.debug("update weather: {}", current);
			LOG.debug("prediction: {}, next: {}", prediction, next);
		}

		Angerona.getInstance().onTickStarting(env);

		somethingHappens = false;
		Perception perception;

		EnvModul mod;
		for (Agent agent : env.getAgents()) {
			mod = moduls.get(agent.getName());

			if (mod == null) {
				LOG.debug("create new env modul for {}", agent.getName());
				mod = new EnvModul();
				moduls.put(agent.getName(), mod);
			}

			switch (current) {
			case SUN:
				if (!mod.isShelter()) {
					LOG.debug("charging with solar panel");
					mod.charge(2);
				}
				break;
			case TEMPEST:
				if (tick > 1 && generator.isLightning()) {
					if (generator.damageAgent()) {
						if (!mod.isShelter()) {
							LOG.debug("damage agent");
							mod.setDamaged(true);
						}
					} else {
						if (!mod.isSecured()) {
							for (int c = 0; c < generator.damageCount(); c++) {
								if (mod.reverseVulnerable(generator.damagePart())) {
									LOG.debug("damage electronics");
								}
							}
						}
					}
				}
				break;
			default: // do nothing
			}

			if (!mod.isDamaged()) {
				somethingHappens = true;

				if (mod.getBattery() > 0) {
					perception = new IslandPerception(agent.getName(), mod.getEnergy(), mod.getLocation(), current, prediction);
					agent.perceive(perception);
					LOG.debug("create perception: {}", perception);

					LOG.debug("call agent cycle");
					agent.cycle();
				}
			}

			LOG.debug("discarge battery");
			mod.discharge();
		}
		angeronaReady = true;

		doingTick = false;
		Angerona.getInstance().onTickDone(env);

		return somethingHappens;
	}

}
