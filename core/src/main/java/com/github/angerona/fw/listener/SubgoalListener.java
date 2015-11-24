package com.github.angerona.fw.listener;

import com.github.angerona.fw.Intention;

/**
 * 
 * @author Tim Janus
 */
public interface SubgoalListener {
	/**
	 * is called when a subgoal is finished.
	 * @param subgoal reference to the finished subgoal.
	 */
	void onSubgoalFinished(Intention subgoal);
}
