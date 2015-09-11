package com.github.angerona.fw.island.behavior;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.Angerona;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.def.DefaultBehavior;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class ParsingBehavior extends DefaultBehavior {

	private static final Logger LOG = LoggerFactory.getLogger(ParsingBehavior.class);

	protected boolean loaded = false;

	@Override
	public abstract void sendAction(AngeronaEnvironment env, Action act);

	@Override
	public void receivePerception(AngeronaEnvironment env, Perception percept) {}

	@Override
	public boolean runOneTick(AngeronaEnvironment env) {
		doingTick = true;
		angeronaReady = false;
		Angerona.getInstance().onTickStarting(env);
		somethingHappens = false;

		if (loaded) {
			somethingHappens = _runOnTick(env);
		} else {
			init(env);
			loaded = true;
			somethingHappens = true;
		}

		tick++;
		angeronaReady = true;
		doingTick = false;
		Angerona.getInstance().onTickDone(env);
		return somethingHappens;
	}

	protected abstract boolean _runOnTick(AngeronaEnvironment env);

	@Override
	public boolean run(AngeronaEnvironment env) {
		return super.run(env);
	}

	protected boolean init(AngeronaEnvironment env) {
	//	ParsableComponent pComp = null;
		File path = null;
		boolean success = true;

		for (Agent agnt : env.getAgents()) {
			for (AgentComponent comp : agnt.getComponents()) {
			//	if (comp instanceof ParsableComponent) {
			//		pComp = (ParsableComponent) comp;
				//	try {
					//	path = new File(env.getDirectory() + "/" + agnt.getName() + pComp.getFileSuffix());
					//	LOG.info("loading component from file: {}", path);
				//		pComp.loadFromFile(path);
				//	} catch (IOException e) {
				//		LOG.warn("failed loading from file", e);
				//		success = false;
				//	}
			//	}
			}
		}

		return success;
	}

}
