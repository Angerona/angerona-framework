package com.github.angerona.fw.motivation.operators.temp;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Intention;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class Intentions extends BaseAgentComponent {

	private Intention selected;

	public Intention getSelected() {
		return selected;
	}

	public void setSelected(Intention selected) {
		this.selected = selected;
	}

	@Override
	public BaseAgentComponent clone() {
		Intentions cln = new Intentions();
		cln.selected = this.selected;
		return cln;
	}

}
