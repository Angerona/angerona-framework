package com.github.kreaturesfw.core.listener;

import com.github.kreaturesfw.core.legacy.Intention;

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
