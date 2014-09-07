package com.github.angerona.fw.motivation.island.comp;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.motivation.island.enums.EnergyLevel;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class Battery extends BaseAgentComponent {

	protected int charge = 16;
	protected boolean damaged = false;

	public int getCharge() {
		return charge;
	}

	public EnergyLevel getLevel() {
		if (charge > 10) {
			return EnergyLevel.FULL;
		}

		if (charge > 4) {
			return EnergyLevel.PARTIAL;
		}

		return EnergyLevel.LOW;
	}

	public int charge(int c) {
		charge = Math.min(charge + c, 15);
		report("charge battery, remaining: " + charge);
		return charge;
	}

	public int discharge() {
		charge--;
		report("discharge battery, remaining: " + charge);
		return charge;
	}

	public boolean isEmpty() {
		return charge < 1;
	}

	public void damage() {
		damaged = true;
		report("battery is damaged");
	}

	public boolean isDamaged() {
		return damaged;
	}

	@Override
	public BaseAgentComponent clone() {
		Battery cln = new Battery();
		cln.charge = this.charge;
		cln.damaged = this.damaged;
		return cln;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
