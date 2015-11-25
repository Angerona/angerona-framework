package com.github.kreaturesfw.island.components;

import java.util.List;

import com.github.kreaturesfw.core.BaseAgentComponent;
import com.github.kreaturesfw.core.comp.Presentable;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class Battery extends BaseAgentComponent implements Presentable {

	protected int charge = 16;
	protected boolean damaged = false;

	public Battery() {}

	public Battery(Battery other) {
		super(other);
		this.charge = other.charge;
		this.damaged = other.damaged;
	}

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
		if (charge < 1)
			report("battery is empty");
		return charge;
	}

	public boolean isEmpty() {
		return charge < 1;
	}

	public void damage() {
		damaged = true;
		report("battery was damaged");
	}

	public boolean isDamaged() {
		return damaged;
	}

	@Override
	public BaseAgentComponent clone() {
		return new Battery(this);
	}

	@Override
	public String toString() {
		return "Battery [charge=" + charge + ", damaged=" + damaged + "]";
	}

	@Override
	public void getRepresentation(List<String> representation) {
		representation.add("Charge: " + this.getCharge());
		representation.add("Damaged: " + this.isDamaged());
	}

}
