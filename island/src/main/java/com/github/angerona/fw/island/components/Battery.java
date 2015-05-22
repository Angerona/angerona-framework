package com.github.angerona.fw.island.components;

import com.github.angerona.fw.BaseAgentComponent;

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

	public int charge(int c) {
		charge = Math.min(charge + c, 16);
		report("charge battery, remaining: " + charge);
		return charge;
	}

	public int discharge() {
		charge = Math.max(charge - 1, 0);
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
		return "Battery [charge=" + charge + ", damaged=" + damaged + "]";
	}

}
