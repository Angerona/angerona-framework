package com.github.angerona.fw.motivation.operators.temp;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class Intentions extends BaseAgentComponent {

	private Desire selected;

	public Desire getSelected() {
		return selected;
	}

	public void setSelected(Desire selected) {
		this.selected = selected;
	}

	@Override
	public BaseAgentComponent clone() {
		Intentions cln = new Intentions();
		cln.selected = this.selected;
		return cln;
	}
	
	@Override
	public String toString() {
		// this is a unpleasant workaround
		return this.getClass().getSimpleName();
	}

}
