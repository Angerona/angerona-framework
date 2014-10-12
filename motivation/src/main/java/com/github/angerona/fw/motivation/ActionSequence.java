package com.github.angerona.fw.motivation;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.Action;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionSequence {

	protected List<Action> actions = new LinkedList<>();

	public int getLength() {
		return actions.size();
	}

}
